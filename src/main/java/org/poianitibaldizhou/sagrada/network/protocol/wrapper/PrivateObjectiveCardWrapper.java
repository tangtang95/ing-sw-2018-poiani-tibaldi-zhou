package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.graphics.objects.Color;

@Immutable
public final class PrivateObjectiveCardWrapper extends CardWrapper {

    private final Color color;

    public PrivateObjectiveCardWrapper(String name, String description, Color color) {
        super(name, description);
        this.color = color;
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
