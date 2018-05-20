package org.poianitibaldizhou.sagrada.lobby.model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Lobby implements Serializable{
    private List<User> userList;
    private String name;
    private boolean gameStarted;

    public static final int MAX_PLAYER = 4;

    private final transient List<ILobbyObserver> lobbyObservers;

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
        lobbyObservers = new ArrayList<>();
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
     * Adds an observer to the lobby.
     * @param observer ILobbyObserver to add
     */
    public void observeLobby(ILobbyObserver observer) {
        lobbyObservers.add(observer);
    }

    /**
     * Notify that an user joined the lobby.
     * If the number of users in the lobby is equals to MAX_PLAYER, return true
     *
     * @param user user joined
     * @return true if the lobby if full after the player join, false otherwise@throws RemoteException
     */
    public boolean join(User user) {
        this.userList.add(user);

        for(ILobbyObserver lo:lobbyObservers) {
            try {
                lo.onUserJoin(user);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return userList.size() == MAX_PLAYER;
    }

    /**
     * Notify that an user left the lobby.
     * @param user user left
     */
    public void leave(User user) {
        this.userList.remove(user);

        for(ILobbyObserver lo:lobbyObservers) {
            try {
                lo.onUserExit(user);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Notify that game is going to start
     */
    public void gameStart() {
        gameStarted = true;
        for(ILobbyObserver lo:lobbyObservers)
            try {
                lo.onGameStart();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

}
