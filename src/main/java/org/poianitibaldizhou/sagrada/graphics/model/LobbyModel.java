package org.poianitibaldizhou.sagrada.graphics.model;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.io.IOException;

public class LobbyModel {

    private ConnectionManager connectionManager;
    private final String username;
    private String token;

    public LobbyModel(String username, ConnectionManager connectionManager) {
        this.username = username;
        this.connectionManager = connectionManager;
    }

    public void login(IView view, ILobbyObserver observer) throws IOException {
        token = connectionManager.getLobbyController().login(username, view);
        connectionManager.getLobbyController().join(token, username, observer);
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void requestGetUsers() throws IOException {
        //TODO IMPLEMENT
        connectionManager.getLobbyController().requestUsersInLobby(token);
    }

    public void leave() throws IOException {
        connectionManager.getLobbyController().leave(token, username);
        connectionManager.close();
    }
}
