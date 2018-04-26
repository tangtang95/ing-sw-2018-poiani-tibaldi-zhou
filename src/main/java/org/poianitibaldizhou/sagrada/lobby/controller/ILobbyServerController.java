package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILobbyServerController extends Remote {
    String login(String username, ILobbyView view) throws RemoteException;
    void logout(String token) throws RemoteException;
    void leave(String token, String username) throws RemoteException;
    void join(String token, String username, ILobbyObserver lobbyObserver) throws RemoteException;
}
