package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.Lobby;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyDatabase;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServerControllerSocket implements ILobbyServerController{

    private final Map<String, ILobbyView> viewMap = new HashMap<>();

    private transient final LobbyDatabase database;

    private static final Logger LOGGER = Logger.getLogger(GameServerControllerRMI.class.getName());

    public GameServerControllerSocket() throws RemoteException {
        database = LobbyDatabase.getInstance();
    }

    @Override
    public synchronized String login(String username, ILobbyView view) throws RemoteException {
        String token = database.login(username);
        viewMap.put(token, view);
        return token;
    }

    @Override
    public synchronized void logout(String token) throws RemoteException {

    }

    @Override
    public synchronized void leave(String token, String username) throws RemoteException {

    }

    @Override
    public synchronized Lobby join(String token, String username, ILobbyObserver lobbyObserver) throws RemoteException {
        if(!authorize(token, username))
            throw new RemoteException("Authorization failed");
        LOGGER.log(Level.INFO, "joining");
        return database.userJoinLobby(lobbyObserver, database.getUserByToken(token));
    }

    private boolean authorize(String token, String username) throws RemoteException {
        User user = database.getUserByToken(token);

        return user.getName().equals(username);
    }
}
