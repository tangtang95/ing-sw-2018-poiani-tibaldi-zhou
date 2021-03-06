package org.poianitibaldizhou.sagrada.lobby.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.ILobbyFakeObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OVERVIEW: Represents a lobby as a set of users waiting for the game to start
 * <p>
 * This doesn't contains duplicates.
 * userList.size() >= 0 && userList.size() <= MAX_PLAYER
 * lobbyObserverMap.size() >= 0 && lobbyObserverMap.size() <= MAX_PLAYER
 */
public class Lobby {
    private List<User> userList;
    private String name;
    private boolean gameStarted;
    private final Map<String, ILobbyFakeObserver> lobbyObserverMap;

    public static final int MAX_PLAYER = 4;

    /**
     * Constructor.
     * Creates a new Lobby with a certain name.
     *
     * @param name lobby's name
     */
    public Lobby(String name) {
        this.name = name;
        userList = new ArrayList<>();
        lobbyObserverMap = new HashMap<>();
        gameStarted = false;
    }

    // GETTER

    /**
     * Returns the list of the users that are currently in the lobby.
     * List is copied in a new list.
     *
     * @return list of the users that are currently in the lobby
     */
    @Contract(pure = true)
    public List<User> getUserList() {
        return new ArrayList<>(userList);
    }

    /**
     * Returns true if the game is started, false otherwise
     *
     * @return true if the game is started, false otherwise
     */
    @Contract(pure = true)
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Return the name of the lobby
     *
     * @return name of the lobby
     */
    @Contract(pure = true)
    public String getName() {
        return name;
    }

    /**
     * Returns the observers that are listening this lobby
     *
     * @return observers listening this
     */
    public Map<String, ILobbyFakeObserver> getLobbyObserverMap() {
        return new HashMap<>(lobbyObserverMap);
    }

    // MODIFIER

    /**
     * Attach a new observer to the lobby
     *
     * @param token             token that identifies the observer
     * @param lobbyFakeObserver lobby fake observer that will listen this
     */
    public void attachObserver(String token, ILobbyFakeObserver lobbyFakeObserver) {
        lobbyObserverMap.putIfAbsent(token, lobbyFakeObserver);
    }

    /**
     * Detach a certain observer identified by token
     *
     * @param token identify the observer that will be detached
     */
    public void detachObserver(String token) {
        lobbyObserverMap.remove(token);
    }


    /**
     * Notify that an user joined the lobby.
     * If the number of users in the lobby is equals to MAX_PLAYER, return true
     *
     * @param user user joined
     * @return true if the lobby is full after the player join, false otherwise
     */
    public boolean join(User user) {
        if (userList.size() == MAX_PLAYER)
            throw new IllegalStateException();
        this.userList.add(user);
        lobbyObserverMap.forEach((key, value) -> value.onUserJoin(user));
        return userList.size() == MAX_PLAYER;
    }

    /**
     * Notify that an user left the lobby.
     *
     * @param user user left
     */
    public void leave(User user) {
        this.userList.remove(user);
        lobbyObserverMap.forEach((key, value) -> value.onUserExit(user));
    }

    /**
     * Notify that game is going to start
     */
    public void gameStart(String gameName) {
        gameStarted = true;
        lobbyObserverMap.forEach((key, value) -> value.onGameStart(gameName));
    }
}
