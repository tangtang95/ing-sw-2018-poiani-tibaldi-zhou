package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

/**
 * OVERVIEW: Represents the flow of the command when executing a tool card. The flow permits to proceed in the default
 * way or in a sub way, furthermore it is possible to repeat a certain command, or even to stop the execution for various
 * reasons.
 */
public enum CommandFlow implements JSONable{
    MAIN, SUB, REPEAT, DICE_ALREADY_PLACED(400), TURN_CHECK_FAILED(400), EMPTY_ROUND_TRACK(400),
    EMPTY_DRAFT_POOL(400), DICE_CANNOT_BE_PLACED_ANYWHERE(400), NOT_EXISTING_DICE_OF_CERTAIN_COLOR(400),
    EMPTY_SCHEMA_CARD(400), NOT_DICE_IN_DRAFT_POOL(400), EMPTY_DICE_BAG(400), PLACEMENT_ALREADY_DONE(400);

    private int protocolNumber;

    /**
     * Constructor. Creates a command flow with a certain protocol number.
     * @param protocolNumber protocol number of the enum constant.
     */
    CommandFlow(int protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    /**
     * Constructor. Creates a command flow with the default protocol number of 200.
     */
    CommandFlow() {
        this.protocolNumber = 200;
    }

    /**
     * Returns the protocol number associated with the enum value
     * @return protocol number associated with the enum value
     */
    public int getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        main.put(SharedConstants.TYPE, SharedConstants.COMMAND_FLOW);
        main.put(SharedConstants.BODY,this.name());
        return main;
    }

    /**
     * Creates a command flow from a json object
     * @param jsonObject json object that represents the enum
     * @return command flow converted from jsonObject
     */
    public static CommandFlow toObject(JSONObject jsonObject) {
        return CommandFlow.valueOf(jsonObject.get(SharedConstants.BODY).toString());
    }
}
