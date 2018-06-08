package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

/**
 * Enum of colorWrapper, copy of Color class in model.
 */
public enum ColorWrapper implements JSONable{
    GREEN, RED, YELLOW, BLUE, PURPLE;

    /**
     * Convert a ColorWrapper in a JSONObject.
     * @return a JSONObject.
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
     * Convert a JSONObject in a ColorWrapper.
     *
     * @param jsonObject to convert in a ColorWrapper.
     * @return a ColorWrapper.
     */
    public static ColorWrapper toObject(JSONObject jsonObject) {
        return ColorWrapper.valueOf(jsonObject.get(SharedConstants.BODY).toString());
    }
}
