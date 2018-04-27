package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.Lobby;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyDatabase;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.lobby.socket.HandleClient;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServerControllerRMI extends UnicastRemoteObject implements ILobbyServerController{
    private final Map<String, ILobbyView> viewMap = new HashMap<>();

    private transient final LobbyDatabase database;

    private static final Logger LOGGER = Logger.getLogger(GameServerControllerRMI.class.getName());

    public GameServerControllerRMI() throws RemoteException {
        super();
        database = LobbyDatabase.getInstance();
    }


    /**
     * Implements the login of an User with an username and a view.
     *
     * @param username user's name
     * @param view user's view
     * @return login's token
     * @throws RemoteException if an username with a certain view is already logged or if there are
     *                         some problems with the communication architecture
     */
    @Override
    public String login(String username, ILobbyView view) throws RemoteException {
        String token = database.login(username);
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
    public void logout(String token) throws RemoteException {
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
    public void leave(String token, String username) throws RemoteException {
        if(!authorize(token, username))
            throw new RemoteException("Authorization failed");
        database.userLeaveLobby(database.getUserByToken(token));
        viewMap.get(token).ack("Lobby left");
    }

    /**
     * An user identified by token and username join the lobby. It also carries an lobbyObserver
     * in order to receive notification of what happens.
     *
     * @param token user's token
     * @param username user's name
     * @param lobbyObserver observer of the lobby for the client
     * @throws RemoteException
     */
    @Override
    public Lobby join(String token, String username, ILobbyObserver lobbyObserver) throws RemoteException {
        if(!authorize(token, username))
            throw new RemoteException("Authorization failed");
        Lobby lobby = database.userJoinLobby(lobbyObserver, database.getUserByToken(token));
        viewMap.get(token).ack("You're now in the lobby");
        return lobby;
    }


    private boolean authorize(String token, String username) throws RemoteException {
        User user = database.getUserByToken(token);

        return user.getName().equals(username);
    }
}
