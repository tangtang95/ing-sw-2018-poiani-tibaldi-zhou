package org.poianitibaldizhou.sagrada.game.model;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

/**
 * Enum of colors. The color are BLUE, YELLOW, RED, GREEN, PURPLE.
 */
public enum Color implements JSONable{
    BLUE,
    RED ,
    GREEN,
    YELLOW,
    PURPLE;


    /**
     * Covert a color to JSONObject.
     *
     * @return a color to JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        main.put(SharedConstants.TYPE, SharedConstants.COLOR);
        main.put(SharedConstants.BODY,this.name());
        return main;
    }

    /**
     * Convert a JSONObject to a color.
     *
     * @param jsonObject to convert.
     * @return the correct color.
     */
    public static Color toObject(JSONObject jsonObject) {
        return Color.valueOf(jsonObject.get(SharedConstants.BODY).toString());
    }
}
