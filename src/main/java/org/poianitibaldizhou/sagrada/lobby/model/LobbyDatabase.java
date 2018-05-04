package org.poianitibaldizhou.sagrada.lobby.model;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyDatabase {
    private static LobbyDatabase database;

    private static final Logger LOGGER = Logger.getLogger(LobbyDatabase.class.getName());

    private LobbyDatabase() {
        createLobby(UUID.randomUUID().toString());
    }

    public synchronized static LobbyDatabase getInstance() {
        if (database == null)
            database = new LobbyDatabase();
        return database;
    }

    private final Collection<User> users = new ArrayList<>();
    private Lobby lobby;

    /**
     * Returns an user with a specified token.
     *
     * @param token user's token
     * @return User with specified token
     * @throws RemoteException if no such user with specified token exists
     */
    public synchronized User getUserByToken(String token) throws RemoteException {
        for(User u: users)
            if(u.getToken().equals(token))
                return u;
        throw new RemoteException("No such user with specified token exists.");
    }

    /**
     * An user join the lobby.
     * If the lobby is full after the player join, a new lobby is created.
     *
     * @param lobbyObserver observing the lobby for the client
     * @param user user joining
     * @throws RemoteException if user has already joined the lobby
     */
    public synchronized void userJoinLobby(ILobbyObserver lobbyObserver, User user) throws RemoteException {
        LOGGER.log(Level.INFO, user.getName());
        if(lobby.getUserList().contains(user))
            throw new RemoteException("User has already joined the lobby.");
        lobby.observeLobby(lobbyObserver);
        if(lobby.join(user)) {
            lobby = new Lobby(UUID.randomUUID().toString());
        }
    }

    /**
     * An user leave the lobby
     *
     * @param user user leaving
     * @throws RemoteException if the user is not present in the lobby
     */
    public synchronized void userLeaveLobby(User user) throws RemoteException {
        if(!lobby.getUserList().contains(user))
            throw new RemoteException("Can't leave because user is not in the lobby");
        lobby.leave(user);
    }

    /**
     * Creates the current lobby that need to be filled.
     *
     * @param name lobby's name
     */
    private synchronized void createLobby(String name) {
        lobby = new Lobby(name);
    }

    /**
     * Implements user login on server.
     *
     * @param username user's name
     * @return user's token
     * @throws RemoteException if an user with username is already logged
     */
    public synchronized String login(String username) throws RemoteException {
        for (User u : users) {
            if (u.getName().equals(username)) {
                throw new RemoteException("User already logged: " + username);
            }
        }
        String token = UUID.randomUUID().toString();
        User user = new User(username, token);
        users.add(user);
        return token;
    }

    /**
     * Implements user logout on server via login.
     *
     * @param token user's token
     * @throws RemoteException if no user with token exists
     */
    public synchronized void logout(String token) throws RemoteException {
        for (User u : users) {
            if (u.getToken().equals(token)) {
                users.remove(u);
                return;
            }
        }

        throw new RemoteException("No user with this token exists. Impossible to logout");
    }

    /**
     * Returns the number of logged users.
     *
     * @return number of logged user
     */
    public int usersSize() {
        return users.size();
    }
}