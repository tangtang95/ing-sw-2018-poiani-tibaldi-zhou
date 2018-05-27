package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.IView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILobbyController extends Remote {
    String login(String username, IView view) throws RemoteException;

    void leave(String token, String username) throws RemoteException;
    void join(String token, String username, ILobbyObserver lobbyObserver) throws RemoteException;

    void requestUsersInLobby(String token) throws RemoteException;
    void requestTimeout(String token) throws RemoteException;
}
