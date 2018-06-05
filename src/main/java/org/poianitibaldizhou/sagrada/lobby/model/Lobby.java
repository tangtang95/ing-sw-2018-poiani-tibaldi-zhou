package org.poianitibaldizhou.sagrada.lobby.model;

import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyFakeObserver;
import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby implements Serializable {
    private List<User> userList;
    private String name;
    private boolean gameStarted;
    private final transient Map<String, ILobbyFakeObserver> lobbyObserverMap;

    public static final int MAX_PLAYER = 8;

    /**
     * Constructor.
     * Creates a new Lobby with a certain name.
     * A lobby represents of user that can contains duplicate.
     *
     * @param name lobby's name
     */
    public Lobby(String name) {
        this.name = name;
        userList = new ArrayList<>();
        lobbyObserverMap = new HashMap<>();
        gameStarted = false;
    }

    public int getPlayerNum() {
        return userList.size();
    }

    public List<User> getUserList() {
        return new ArrayList<>(userList);
    }

    public int getNumberOfPlayer() {
        return userList.size();
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public String getName() {
        return name;
    }

    public void attachObserver(String token, ILobbyFakeObserver lobbyFakeObserver) {
        lobbyObserverMap.putIfAbsent(token, lobbyFakeObserver);
    }

    public void detachObserver(String token) {
        lobbyObserverMap.remove(token);
    }

    public Map<String, ILobbyFakeObserver> getLobbyObserverMap() {
        return new HashMap<>(lobbyObserverMap);
    }

    /**
     * Notify that an user joined the lobby.
     * If the number of users in the lobby is equals to MAX_PLAYER, return true
     * If it detects an exception in notifying user leave, it signals to lobby manager
     * that an user has disconnected.
     *
     * @param user user joined
     * @return true if the lobby is full after the player join, false otherwise
     */
    public boolean join(User user) {
        this.userList.add(user);
        lobbyObserverMap.forEach((key, value) -> value.onUserJoin(user));
        return userList.size() == MAX_PLAYER;
    }

    /**
     * Notify that an user left the lobby.
     * If it detects an exception in notifying user leave, it signals to lobby manager
     * that an user has disconnected.
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
