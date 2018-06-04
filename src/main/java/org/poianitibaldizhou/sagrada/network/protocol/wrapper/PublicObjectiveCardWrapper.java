package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;

@Immutable
public final class PublicObjectiveCardWrapper extends CardWrapper{

    public PublicObjectiveCardWrapper(String name, String description) {
        super(name, description);
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return null;
    }
}
