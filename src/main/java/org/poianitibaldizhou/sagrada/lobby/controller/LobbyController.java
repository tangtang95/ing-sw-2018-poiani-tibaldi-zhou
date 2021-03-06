package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.LobbyNetworkManager;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ServerGetMessage;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @see ILobbyController
 */
public class LobbyController extends UnicastRemoteObject implements ILobbyController {
    private final transient LobbyManager lobbyManager;

    private final transient ServerGetMessage networkGetItem;

    private final transient LobbyNetworkManager lobbyNetworkManager;

    /**
     * Constructor.
     * Creates a new lobby controller with a lobby manager, that helps the controller in handling the interactions
     * of the clients with the model
     *
     * @param lobbyManager lobby manager that helps the controller in handling the interactions of the clients
     *                     with the model
     * @throws RemoteException due to the UnicastRemoteObject
     */
    public LobbyController(LobbyManager lobbyManager) throws RemoteException {
        super();
        this.lobbyManager = lobbyManager;
        this.lobbyNetworkManager = lobbyManager.getLobbyNetworkManager();
        networkGetItem = new ServerGetMessage();
    }

    // INTERFACES METHOD

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String login(String message, ILobbyView view) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String username = networkGetItem.getUserName(message);
        String token;

        lobbyNetworkManager.clearObserver();

        try {
            token = lobbyManager.login(username);
        } catch (IllegalArgumentException e) {
            view.err(ServerMessage.USER_ALREADY_EXIST);
            return serverCreateMessage.createTokenMessage("").buildMessage();
        }

        lobbyNetworkManager.putView(token, view);

        try {
            view.ack(ServerMessage.YUO_ARE_LOGGED + username);
        } catch (IOException e) {
            return serverCreateMessage.reconnectErrorMessage();
        }
        return serverCreateMessage.createTokenMessage(token).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void leave(String message) throws IOException {
        final String token = networkGetItem.getToken(message);
        final String username = networkGetItem.getUserName(message);

        if (isAuthorize(token, username))
            return;

        lobbyNetworkManager.clearObserver();

        lobbyManager.userLeaveLobby(lobbyManager.getUserByToken(token));

        lobbyNetworkManager.getViewByToken(token).ack(ServerMessage.LOBBY_LEFT);
        lobbyNetworkManager.removeView(token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void join(String message, ILobbyObserver lobbyObserver) throws IOException {
        final String token = networkGetItem.getToken(message);
        final String username = networkGetItem.getUserName(message);

        if (isAuthorize(token, username)) {
            throw new IOException(ServerMessage.AUTHORIZATION_FAILED);
        }

        lobbyNetworkManager.clearObserver();

        lobbyManager.userJoinLobby(lobbyObserver, lobbyManager.getUserByToken(token));
        try {
            lobbyNetworkManager.getViewByToken(token).ack(ServerMessage.YUO_ARE_IN_LOBBY);
        } catch (IOException e) {
            lobbyManager.getLobbyObserverManager().signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsersInLobby() {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        lobbyNetworkManager.clearObserver();
        return serverCreateMessage.createUserList(lobbyManager.getLobbyUsers()).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTimeout() {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        lobbyNetworkManager.clearObserver();
        return serverCreateMessage.createTimeoutMessage(formatTimeout(lobbyManager.getTimeToTimeout())).buildMessage();
    }

    // PRIVATE METHODS


    /**
     * Returns true if token matches username, false otherwise.
     *
     * @param token    user's token
     * @param username user's username
     * @return true if token matches username, false otherwise
     */
    private boolean isAuthorize(String token, String username) {
        User user;
        user = lobbyManager.getUserByToken(token);
        return !user.getName().equals(username);
    }

    /**
     * Converts a time given in milliseconds to a String in format mm:ss.
     *
     * @param timeout time given in milliseconds
     * @return string representing timeout in format mm:ss
     */
    private String formatTimeout(long timeout) {
        Date date = new Date(timeout);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LobbyController)) return false;
        if (!super.equals(o)) return false;
        LobbyController that = (LobbyController) o;
        return Objects.equals(lobbyNetworkManager, that.lobbyNetworkManager) &&
                Objects.equals(lobbyManager, that.lobbyManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyNetworkManager, lobbyManager);
    }
}
