package org.poianitibaldizhou.sagrada.network.protocol;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;

import java.util.List;

public class ServerCreateMessage {

    private final JSONServerProtocol jsonServerProtocol;

    public ServerCreateMessage() {
        jsonServerProtocol = new JSONServerProtocol();
    }

    public ServerCreateMessage createDiceMessage(Dice dice){
        jsonServerProtocol.appendMessage(SharedConstants.DICE_KEY,dice);
        return this;
    }

    public ServerCreateMessage createDiceList(List<Dice> diceList) {
        jsonServerProtocol.appendMessage(SharedConstants.DICE_LIST_KEY,
                diceList);
        return this;
    }

    @Override
    public String toString() {
        return jsonServerProtocol.buildMessage();
    }
}
