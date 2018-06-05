package org.poianitibaldizhou.sagrada.graphics.model;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.util.List;

public class LobbyModel {

    private ConnectionManager connectionManager;
    private final String username;
    private String token;

    public LobbyModel(String username, ConnectionManager connectionManager) {
        this.username = username;
        this.connectionManager = connectionManager;
    }

    public void login(IView view, ILobbyObserver observer) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();

        String request = builder.createUsernameMessage(username).buildMessage();
        String response = connectionManager.getLobbyController().login(request, view);
        token = parser.getToken(response);

        request = builder.createTokenMessage(token).createUsernameMessage(username).buildMessage();
        connectionManager.getLobbyController().join(request, observer);
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public List<UserWrapper> requestGetUsers() throws IOException {
        ClientGetMessage parser = new ClientGetMessage();

        String response = connectionManager.getLobbyController().getUsersInLobby();
        return parser.getListOfUserWrapper(response);
    }

    public void leave() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createUsernameMessage(username).buildMessage();
        connectionManager.getLobbyController().leave(request);
        connectionManager.close();
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
