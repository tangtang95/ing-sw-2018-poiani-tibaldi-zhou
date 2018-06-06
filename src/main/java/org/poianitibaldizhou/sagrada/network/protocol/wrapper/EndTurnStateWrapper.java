package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

public class EndTurnStateWrapper implements IActionWrapper {
    /**
     * Convert a action in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        main.put(SharedConstants.TYPE, SharedConstants.END_TURN_ACTION);
        main.put(SharedConstants.BODY,new JSONObject());
        return main;
    }

    public static EndTurnStateWrapper toObject(JSONObject jsonObject) {
        return null;
    }
}
