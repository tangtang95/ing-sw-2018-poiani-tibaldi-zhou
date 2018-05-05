package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class LobbyController extends UnicastRemoteObject implements ILobbyController {
    private final transient Map<String, ILobbyView> viewMap = new HashMap<>();

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
    public synchronized String login(String username, ILobbyView view) throws RemoteException {
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
     * @throws RemoteException if no user's with token exists in the model or if there are some problems
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
     * @throws RemoteException
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

    private boolean authorize(String token, String username) throws RemoteException {
        User user = database.getUserByToken(token);
        return user.getName().equals(username);
    }
}
