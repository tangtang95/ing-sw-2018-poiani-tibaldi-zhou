package org.poianitibaldizhou.sagrada.lobby.model;

import org.poianitibaldizhou.sagrada.ManagerMediator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyManager {

    private final List<User> users;
    private Lobby lobby;
    private Thread timeoutThread;
    private Runnable timeout;
    private long timeoutStart;
    private ManagerMediator managerMediator;

    // TODO read timeout DELAY_TIME from file (better check sagrada instruction), for now DELAY_TIME=30s
    private static final long DELAY_TIME = 60000;

    /**
     * Constructor.
     * Create a manager for the lobby
     *
     * @param managerMediator
     */
    public LobbyManager(ManagerMediator managerMediator) {
        this.managerMediator = managerMediator;
        this.managerMediator.setLobbyManager(this);

        users = new ArrayList<>();
        lobby = null;
        timeout = () -> {
            try {
                Thread.sleep(DELAY_TIME);
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "TimeoutThread interrupted");
                Thread.currentThread().interrupt();
            }
            handleTimeout();
        };
    }

    private synchronized void setTimeout() {
        timeoutThread = new Thread(timeout);
        timeoutThread.start();
        timeoutStart = System.currentTimeMillis();
    }

    /**
     * Creates a new lobby
     */
    private synchronized void createLobby() {
        lobby = new Lobby(UUID.randomUUID().toString());
        setTimeout();
    }

    private synchronized void createGame() {
        managerMediator.createMultiPlayerGame(users);
        lobby.gameStart();
    }

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
        if(lobby == null)
            createLobby();
        if(lobby.getUserList().contains(user))
            throw new RemoteException("User has already joined the lobby.");
        if(lobby.isGameStarted())
            createLobby();
        lobby.observeLobby(lobbyObserver);
        if(lobby.join(user)) {
            createGame();
            createLobby();
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
        if(lobby.isGameStarted())
            throw new RemoteException("The lobby is started");
        lobby.leave(user);
        if(lobby.getNumberOfPlayer() == 0) {
            lobby = null;
            timeoutThread = null;
        }
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
     * Implements user logout on server.
     * If the user is in a lobby, it leaves.
     *
     *
     * @param token user's token
     * @throws RemoteException if no user with token exists
     */
    public synchronized void logout(String token) throws RemoteException {
        User user = this.getUserByToken(token);

        if(lobby != null && lobby.getUserList().contains(user))
            this.userLeaveLobby(user);
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

    public List<User> getLobbyUsers() throws RemoteException {
        if(lobby == null)
            throw new RemoteException("No lobby active");
        return lobby.getUserList();
    }


    /**
     * Handles timeout. When timeout is signaled, if the number of players in the lobby
     * are greater or equal then 2, the game starts, otherwise timeout gets restarted.
     */
    public synchronized void handleTimeout() {
        if(lobby != null && lobby.getPlayerNum() >= 2) {
            createGame();
            createLobby();
        } else {
            setTimeout();
        }
    }

    /**
     * Returns time missing to reach timeout.
     * @return time in millis
     */
    public synchronized long getTimeToTimeout() throws RemoteException {
        if(lobby == null)
            throw new RemoteException("No lobby Active");
        long currTime = System.currentTimeMillis();
        return DELAY_TIME - (currTime-timeoutStart);
    }

    public long getDelayTime() {
        return DELAY_TIME;
    }
}