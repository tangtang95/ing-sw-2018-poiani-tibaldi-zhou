package org.poianitibaldizhou.sagrada.game.model.players;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

/**
 * OVERVIEW: Represents the outcome of the game for a certain player. This include even
 * the state "in game", if the game is not terminated yet.
 */
public enum Outcome implements JSONable{
    WIN,
    LOSE,
    IN_GAME;

    /**
     * Covert a outcome to JSONObject.
     *
     * @return a outcome to JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        main.put(SharedConstants.TYPE, SharedConstants.OUTCOME);
        main.put(SharedConstants.BODY,this.name());
        return main;
    }
}
