package org.poianitibaldizhou.sagrada.lobby.model;

import org.poianitibaldizhou.sagrada.MediatorManager;
import org.poianitibaldizhou.sagrada.ServerSettings;
import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.observers.LobbyFakeObserver;
import org.poianitibaldizhou.sagrada.network.LobbyNetworkManager;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: Lobby manager for handling the lobby stage of Sagrada.
 * This do not contains duplicate.
 */
public class LobbyManager {

    private final List<User> users;

    private Lobby lobby;
    private Thread timeoutThread;
    private Runnable timeout;
    private long timeoutStart;
    private MediatorManager managerMediator;
    private LobbyObserverManager lobbyObserverManager;

    private LobbyNetworkManager lobbyNetworkManager;

    public static final long DELAY_TIME = ServerSettings.getLobbyTimeout();

    /**
     * Constructor.
     * Create a manager for the lobby
     *
     * @param managerMediator acts as an mediator between this and the server game manager
     */
    public LobbyManager(MediatorManager managerMediator) {
        this.managerMediator = managerMediator;

        this.lobbyObserverManager = null;
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

        lobbyNetworkManager = new LobbyNetworkManager(this);
    }

    public Lobby getLobby() {
        return lobby;
    }

    public LobbyNetworkManager getLobbyNetworkManager() {
        return lobbyNetworkManager;
    }

    /**
     * Set timeout: it starts the thread related with timeout and save the start time.
     */
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
        lobbyObserverManager = new LobbyObserverManager();
        setTimeout();
    }

    private synchronized void createGame() {
        String gameName = managerMediator.createMultiPlayerGame(lobby.getUserList());
        lobby.gameStart(gameName);
        lobby.getUserList().forEach(users::remove);
    }

    /**
     * Returns the user with the matching token
     *
     * @param token token of the requested user
     * @return user matching with token
     * @throws IllegalArgumentException if none user with token exists
     */
    public synchronized User getUserByToken(String token) {
        for (User u : users)
            if (u.getToken().equals(token))
                return u;
        throw new IllegalArgumentException("None user with this token is present");
    }

    /**
     * An user join the lobby.
     * If the lobby is full after the player join, a new lobby is created.
     *
     * @param lobbyObserver observing the lobby for the client
     * @param user          user joining
     * @throws IllegalArgumentException if user has already joined the lobby
     */
    public synchronized void userJoinLobby(ILobbyObserver lobbyObserver, User user) {
        if (lobby == null)
            createLobby();
        if (lobby.getUserList().contains(user))
            throw new IllegalArgumentException("User has already joined the lobby.");
        if (lobby.isGameStarted())
            createLobby();
        if (!users.contains(user))
            throw new IllegalArgumentException("Need to login");

        lobbyObserverManager.addToken(user.getToken());
        lobby.attachObserver(user.getToken(), new LobbyFakeObserver(user.getToken(), lobbyObserver, lobbyObserverManager));

        if (lobby.join(user)) {
            createGame();
            createLobby();
        }
    }

    /**
     * An user leave the lobby
     *
     * @param user user leaving
     * @throws IllegalArgumentException if the user is not present in the lobby or if the lobby is started
     */
    public synchronized void userLeaveLobby(User user) {
        if (lobby == null || !lobby.getUserList().contains(user))
            throw new IllegalArgumentException("Can't leave because user is not in the lobby");
        if (lobby.isGameStarted())
            throw new IllegalStateException("The lobby is started");
        lobby.leave(user);
        lobbyObserverManager.removeToken(user.getToken());
        lobby.detachObserver(user.getToken());
        logout(user.getToken());
        if (lobby.getUserList().isEmpty()) {
            lobby = null;
            timeoutThread = null;
            lobbyObserverManager = null;
        }
    }

    /**
     * Disconnects an user.
     * It differs from the user leave because this detach the observers before of signal the disconnection
     * to the client
     *
     * @param token token of the player detected as disconnected
     */
    public synchronized void userDisconnects(String token) {
        if (lobby == null || !lobby.getUserList().contains(getUserByToken(token)))
            throw new IllegalArgumentException("Can't leave because user is not in the lobby");
        if (lobby.isGameStarted())
            throw new IllegalStateException("The lobby is started");
        lobby.detachObserver(token);
        lobby.leave(getUserByToken(token));
        logout(token);
    }

    /**
     * Implements user login on server.
     * Before trying to logging in the user, the clients are pinged to detect disconnections in
     * order to allow the connection with the name of someone who has been detected as
     * disconnected.
     *
     * @param username user's name
     * @return user's token
     * @throws IllegalArgumentException if an user with username is already logged
     */
    public synchronized String login(String username) {
        for (User u : users) {
            if (u.getName().equals(username)) {
                throw new IllegalArgumentException("User already logged: " + username);
            }
        }
        if (managerMediator.isAlreadyPlayingAGame(username))
            throw new IllegalArgumentException("User already logged: " + username);
        String token = String.valueOf(username.hashCode());
        User user = new User(username, token);
        users.add(user);
        return token;
    }

    /**
     * Implements user logout on server.
     * If the user is in a lobby, it leaves.
     *
     * @param token user's token
     * @throws IllegalArgumentException if no user with token exists
     */
    private synchronized void logout(String token) {
        User user = this.getUserByToken(token);

        if (lobby != null && lobby.getUserList().contains(user))
            this.userLeaveLobby(user);
        for (User u : users) {
            if (u.getToken().equals(token)) {
                users.remove(u);
                return;
            }
        }

        throw new IllegalArgumentException("No user with this token exists. Impossible to logout");
    }

    /**
     *
     * @return list of the user in the lobby
     */
    public List<User> getLobbyUsers() {
        if (lobby == null)
            throw new IllegalStateException("No lobby active");
        return lobby.getUserList();
    }

    /**
     *
     * @return list of the player that have logged in
     */
    public List<User> getLoggedUser() {
        return new ArrayList<>(users);
    }

    /**
     * Returns true if there is an istance of a lobby active.
     * The lobby is active if there at least one player.
     *
     * @return true if there a lobby active, false otherwise.
     */
    public boolean isLobbyActive() {
        return !(lobby == null);
    }

    /**
     * Handles timeout. When timeout is signaled, if the number of players in the lobby
     * are greater or equal then 2, the game starts, otherwise timeout gets restarted.
     */
    public synchronized void handleTimeout() {
        if (lobby != null && lobby.getUserList().size() >= 2) {
            createGame();
            createLobby();
        } else {
            setTimeout();
        }
    }

    /**
     *
     * @return lobby observer manager for the lobby
     */
    public LobbyObserverManager getLobbyObserverManager() {
        return lobbyObserverManager;
    }

    /**
     * Returns time missing to reach timeout.
     *
     * @return time in millis
     */
    public synchronized long getTimeToTimeout() {
        if (lobby == null)
            throw new IllegalStateException("No lobby Active");
        long currTime = System.currentTimeMillis();
        return DELAY_TIME - (currTime - timeoutStart);
    }

}