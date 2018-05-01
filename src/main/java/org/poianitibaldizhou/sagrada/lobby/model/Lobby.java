package org.poianitibaldizhou.sagrada.lobby.model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Lobby implements Serializable{
    private List<User> userList;
    private String name;

    private static final int MAX_PLAYER = 4;

    private final transient List<ILobbyObserver> lobbyObservers;

    /**
     * Constructor.
     * Creates a new Lobby with a certain name.
     *
     * @param name lobby's name
     */
    public Lobby(String name) {
        this.name = name;
        userList = new ArrayList<>();
        lobbyObservers = new ArrayList<>();
    }

    public List<User> getUserList() {
        return new ArrayList<>(userList);
    }

    public int getNumberOfPlayer() {
        return userList.size();
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
     * If the number of users in the lobby is equals to MAX_PLAYER, starts the game.
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

        if(userList.size() == MAX_PLAYER) {
            gameStart();
            return true;
        }

        return false;
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
        for(ILobbyObserver lo:lobbyObservers)
            try {
                lo.onGameStart();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

}
