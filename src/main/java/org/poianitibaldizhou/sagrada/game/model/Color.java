package org.poianitibaldizhou.sagrada.game.model;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

public enum Color implements JSONable{
    BLUE,
    RED ,
    GREEN,
    YELLOW,
    PURPLE;


    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        main.put(SharedConstants.TYPE, SharedConstants.COLOR);
        main.put(SharedConstants.BODY,this.name());
        return main;
    }

    public static Color toObject(JSONObject jsonObject) {
        return Color.valueOf(jsonObject.get(SharedConstants.BODY).toString());
    }
}
