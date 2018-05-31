package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.json.simple.JSONObject;

public interface JSONable {
    JSONObject toJSON();
    Object toObject(JSONObject jsonObject);
}
