package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

public enum CommandFlow implements JSONable{
    MAIN, SUB, REPEAT, DICE_ALREADY_PLACED(400), TURN_CHECK_FAILED(400), EMPTY_ROUNDTRACK(400),
    EMPTY_DRAFTPOOL(400), DICE_CANNOT_BE_PLACED_ANYWHERE(400), NOT_EXISTING_DICE_OF_CERTAIN_COLOR,
    EMPTY_SCHEMACARD(400), NOT_DICE_IN_DRAFTPOOL(400), EMPTY_DICEBAG(400), PLACEMENT_ALREADY_DONE(400);

    private int val;

    CommandFlow(int val) {
        this.val = val;
    }

    CommandFlow() {
        this.val = 200;
    }

    public int getProtocolNumber() {
        return val;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        main.put(SharedConstants.TYPE, SharedConstants.COMMAND_FLOW);
        main.put(SharedConstants.BODY,this.name());
        return main;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return CommandFlow.valueOf(jsonObject.get(SharedConstants.BODY).toString());
    }
}
