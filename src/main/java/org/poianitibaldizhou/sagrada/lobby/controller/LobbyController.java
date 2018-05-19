package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.INetworkObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LobbyController extends UnicastRemoteObject implements ILobbyController {
    private final transient Map<String, INetworkObserver> viewMap = new HashMap<>();

    private final transient LobbyManager database;

    public LobbyController() throws RemoteException {
        super();
        database = new LobbyManager();
    }


    /**
     * Implements the login of an User with an username and a view.
     * If an User is already logged with username, returns an empty token (login is thus failed).
     *
     * @param username user's name
     * @param view user's view
     * @return login's token
     * @throws RemoteException if an username with a certain view is already logged or if there are
     *                         some problems with the communication architecture
     */
    @Override
    public synchronized String login(String username, INetworkObserver view) throws RemoteException {
        String token = "";
        try {
            token = database.login(username);
        } catch(RemoteException re) {
            view.err("Another user is logged with this username. Please, choose a new username.");
            return token;
        }
        viewMap.put(token, view);
        view.ack("You are now logged as: " + username);
        return token;
    }

    /**
     * Implements the logout of an User by his token.
     *
     * @param token user's token
     * @throws RemoteException if no user's with token exists in the game or if there are some problems
     *                          with the communication architecture
     */
    @Override
    public synchronized void logout(String token) throws RemoteException {
        database.logout(token);
        viewMap.get(token).ack("Logged out");
        viewMap.remove(token);
    }

    /**
     * An user identified with token and username leaves the lobby.
     *
     * @param token user's token
     * @param username user's name
     * @throws RemoteException if authorization fails or if there are problems in the communication
     * architecture
     */
    @Override
    public synchronized void leave(String token, String username) throws RemoteException {
        if(!authorize(token, username))
            throw new RemoteException("Authorization failed");
        try {
            database.userLeaveLobby(database.getUserByToken(token));
        } catch(RemoteException re) {
            viewMap.get(token).err("Can't leave the lobby.");
            return;
        }
        viewMap.get(token).ack("Lobby left");
    }

    /**
     * An user identified by token and username join the lobby. It also carries an lobbyObserver
     * in order to receive notification of what happens.
     * If the user has already joined, signals an error message to the user's view.
     *
     * @param token user's token
     * @param username user's name
     * @param lobbyObserver observer of the lobby for the client
     * @throws RemoteException the remote exception
     */
    @Override
    public synchronized void join(String token, String username, ILobbyObserver lobbyObserver) throws RemoteException {
        if(!authorize(token, username))
            throw new RemoteException("Authorization failed");
        try {
            database.userJoinLobby(lobbyObserver, database.getUserByToken(token));
        } catch (RemoteException re) {
            viewMap.get(token).err("You have already joined the lobby.");
            return;
        }
        viewMap.get(token).ack("You're now in the lobby");
    }

    /**
     * Requests the list of users that are currently in the lobby
     *
     * @param token requesting user's token
     * @throws RemoteException network communication error
     */
    @Override
    public void requestUsersInLobby(String token) throws RemoteException {
        try {
            viewMap.get(token).ack(database.getLobbyUsers().toString());
        } catch (RemoteException re) {
            viewMap.get(token).err("No lobby is active. Join to create one.");
        }
    }

    /**
     * Requests the time needed to reach timeout in milliseconds
     *
     * @param token requesting user's token
     * @throws RemoteException network communication error
     */
    @Override
    public void requestTimeout(String token) throws RemoteException {
        try {
            viewMap.get(token).ack(formatTimeout(database.getTimeToTimeout()));
        } catch (RemoteException re){
            viewMap.get(token).err("None lobby is active. Join to create one.");
        }
    }

    /**
     * Returns true if token matches username, false otherwise.
     *
     * @param token user's token
     * @param username user's username
     * @return true if token matches username, false otherwise
     */
    private boolean authorize(String token, String username) {
        User user;
        try {
            user = database.getUserByToken(token);
        } catch(RemoteException re) {
            return false;
        }
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
        DateFormat formatter =  new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LobbyController)) return false;
        if (!super.equals(o)) return false;
        LobbyController that = (LobbyController) o;
        return Objects.equals(viewMap, that.viewMap) &&
                Objects.equals(database, that.database);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), viewMap, database);
    }
}
