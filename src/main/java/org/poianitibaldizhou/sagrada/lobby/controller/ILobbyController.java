package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.IView;

import java.io.IOException;
import java.rmi.Remote;

public interface ILobbyController extends Remote {
    String login(String username, IView view) throws IOException;

    void leave(String token, String username) throws IOException;
    void join(String token, String username, ILobbyObserver lobbyObserver) throws IOException;

    void requestUsersInLobby(String token) throws IOException;
    void requestTimeout(String token) throws IOException;
}
