package org.poianitibaldizhou.sagrada.lobby.model;

import com.sun.org.apache.bcel.internal.generic.ILOAD;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Lobby implements Serializable {
    private List<User> userList;
    private String name;
    private boolean gameStarted;
    private LobbyManager lobbyManager;

    public static final int MAX_PLAYER = 8;

    /**
     * Constructor.
     * Creates a new Lobby with a certain name.
     * A lobby represents of user that can contains duplicate.
     *
     * @param name lobby's name
     */
    public Lobby(String name, LobbyManager lobbyManager) {
        this.name = name;
        this.lobbyManager = lobbyManager;
        userList = new ArrayList<>();
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

    /**
     * Notify that an user joined the lobby.
     * If the number of users in the lobby is equals to MAX_PLAYER, return true
     * If it detects an exception in notifying user leave, it signals to lobby manager
     * that an user has disconnected.
     *
     * @param user user joined
     * @return true if the lobby is full after the player join, false otherwise
     * @throws RemoteException
     */
    public boolean join(User user) throws RemoteException {
        this.userList.add(user);

        System.out.println("Observer size : " + lobbyManager.getLobbyObserver().size());

        List<ILobbyObserver> removeList = new ArrayList<>();

        for (ILobbyObserver lo : lobbyManager.getLobbyObserver()) {
            try {
                lo.onUserJoin(user);
            } catch (IOException re) {
                removeList.add(lo);
            }
        }

        for(ILobbyObserver observer : removeList)
            lobbyManager.userDisconnectedDetected(observer);

        return userList.size() == MAX_PLAYER;
    }

    /**
     * Notify that an user left the lobby.
     * If it detects an exception in notifying user leave, it signals to lobby manager
     * that an user has disconnected.
     *
     * @param user user left
     */
    public void leave(User user) throws RemoteException {
        this.userList.remove(user);
        List<ILobbyObserver> removeList = new ArrayList<>();
        for (ILobbyObserver lo : lobbyManager.getLobbyObserver()) {
            try {
                lo.onUserExit(user);
            } catch (IOException e) {
                removeList.add(lo);
            }
        }

        for(ILobbyObserver observer : removeList)
            lobbyManager.userDisconnectedDetected(observer);
    }

    /**
     * Notify that game is going to start
     */
    public void gameStart(String gameName) {
        gameStarted = true;

        for (ILobbyObserver lo : lobbyManager.getLobbyObserver()) {
            try {
                lo.onGameStart(gameName);
            } catch (IOException e) {}
        }
    }
}
