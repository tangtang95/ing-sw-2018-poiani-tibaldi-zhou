package org.poianitibaldizhou.sagrada.network.protocol;

import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;

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

    public ClientCreateMessage createValueMessage(Integer value) {
        jsonClientProtocol.appendMessage(SharedConstants.INTEGER, value);
        return this;
    }

    public ClientCreateMessage createDiceMessage(DiceWrapper dice) {
        jsonClientProtocol.appendMessage(SharedConstants.DICE, dice);
        return this;
    }

    public ClientCreateMessage createColorMessage(ColorWrapper color) {
        jsonClientProtocol.appendMessage(SharedConstants.COLOR, color);
        return this;
    }


    public ClientCreateMessage createAnswerMessage(Boolean bool) {
        jsonClientProtocol.appendMessage(SharedConstants.BOOLEAN, bool);
        return this;
    }

    public ClientCreateMessage createPositionMessage(PositionWrapper positionWrapper) {
        jsonClientProtocol.appendMessage(SharedConstants.POSITION, positionWrapper);
        return this;
    }
}
