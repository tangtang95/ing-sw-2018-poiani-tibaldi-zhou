package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.LobbyNetworkManager;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ServerGetMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * OVERVIEW: Lobby controller of the MVC pattern for lobby stage of Sagrada.
 * Clients interact with this class for accessing, logging, leaving the lobby, that is the place in which
 * the players wait for the game to start
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
    public synchronized String login(String message, IView view) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String username = networkGetItem.getUserName(message);
        String token;

        lobbyNetworkManager.clearObserver();

        try {
            token = lobbyManager.login(username);
        } catch (IllegalArgumentException e) {
            view.err("An user with this username already exists");
            return serverCreateMessage.createTokenMessage("").buildMessage();
        }

        lobbyNetworkManager.putView(token, view);

        try {
            view.ack("You are now logged as: " + username);
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

        if (!authorize(token, username))
            return;

        lobbyNetworkManager.clearObserver();

        lobbyManager.userLeaveLobby(lobbyManager.getUserByToken(token));

        lobbyNetworkManager.getViewByToken(token).ack("Lobby left");
        lobbyNetworkManager.removeView(token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void join(String message, ILobbyObserver lobbyObserver) throws IOException {
        final String token = networkGetItem.getToken(message);
        final String username = networkGetItem.getUserName(message);

        if (!authorize(token, username)) {
            throw new IOException("Authorization failed");
        }

        lobbyNetworkManager.clearObserver();

        lobbyManager.userJoinLobby(lobbyObserver, lobbyManager.getUserByToken(token));
        try {
            lobbyNetworkManager.getViewByToken(token).ack("You're now in the lobby");
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
    private boolean authorize(String token, String username) {
        User user;
        user = lobbyManager.getUserByToken(token);
        return user.getName().equals(username);
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
