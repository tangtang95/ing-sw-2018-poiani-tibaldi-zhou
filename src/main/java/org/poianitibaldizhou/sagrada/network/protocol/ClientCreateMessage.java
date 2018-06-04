package org.poianitibaldizhou.sagrada.network.protocol;

public class ClientCreateMessage {
    private JSONClientProtocol jsonClientProtocol;

    public ClientCreateMessage() {
        jsonClientProtocol = new JSONClientProtocol();
    }

    public ClientCreateMessage createGameNameMessage(String gameName) {
        jsonClientProtocol.appendMessage(SharedConstants.GAME_NAME_KEY, gameName);
        return this;
    }

    public ClientCreateMessage createTokenMessage(String token) {
        jsonClientProtocol.appendMessage(SharedConstants.TOKEN_KEY, token);
        return this;
    }


    public ClientCreateMessage createUsernameMessage(String username) {
        jsonClientProtocol.appendMessage(SharedConstants.USER_NAME_STRING, username);
        return this;
    }

    public String buildMessage() {
        String temp = jsonClientProtocol.buildMessage();
        jsonClientProtocol = new JSONClientProtocol();
        return temp;
    }
}
