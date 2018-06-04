package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.LobbyObserverManager;
import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.network.protocol.ServerNetworkProtocol;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LobbyController extends UnicastRemoteObject implements ILobbyController {
    private final transient Map<String, IView> viewMap = new HashMap<>();

    private final transient LobbyManager lobbyManager;

    private final transient ServerNetworkProtocol networkGetItem;

    public LobbyController(LobbyManager lobbyManager) throws RemoteException {
        super();
        this.lobbyManager = lobbyManager;

        networkGetItem = new ServerNetworkProtocol();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String login(String message, IView view) throws IOException {
        String username = networkGetItem.getUserName(message);
        String token;

        try {
            token = lobbyManager.login(username);
        } catch(IllegalArgumentException e) {
            view.err("An user with this username already exists");
            return "";
        }

        clearObserver();

        viewMap.put(token, view);

        try {
            view.ack("You are now logged as: " + username);
        } catch(IOException e) {
            handleIOException(token);
        }

        // TODO make this return with network protocol
        return token;
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

        clearObserver();

        lobbyManager.userLeaveLobby(lobbyManager.getUserByToken(token));

        viewMap.get(token).ack("Lobby left");
        viewMap.remove(token);
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

        clearObserver();

        lobbyManager.userJoinLobby(lobbyObserver, lobbyManager.getUserByToken(token));
        try {
            viewMap.get(token).ack("You're now in the lobby");
        } catch(IOException e) {
            lobbyManager.getLobbyObserverManager().signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsersInLobby() throws IOException {
        // TODO make this return with network protocol
        clearObserver();
        return lobbyManager.getLobbyUsers().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTimeout() throws IOException {
        // TODO make this return with network protocol
        clearObserver();
        return (formatTimeout(lobbyManager.getTimeToTimeout()));
    }

    private void handleIOException(String token) {
        LobbyObserverManager lobbyObserverManager = lobbyManager.getLobbyObserverManager();
        lobbyObserverManager.signalDisconnection(token);
    }

    private void clearObserver() {
        LobbyObserverManager lobbyObserverManager = lobbyManager.getLobbyObserverManager();
        lobbyObserverManager.getDisconnectedUserNotNotified().forEach(token -> {
            lobbyManager.userLeaveLobby(lobbyManager.getUserByToken(token));
            lobbyObserverManager.disconnectionNotified(token);
            viewMap.remove(token);
        });
    }

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
        return Objects.equals(viewMap, that.viewMap) &&
                Objects.equals(lobbyManager, that.lobbyManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), viewMap, lobbyManager);
    }
}
