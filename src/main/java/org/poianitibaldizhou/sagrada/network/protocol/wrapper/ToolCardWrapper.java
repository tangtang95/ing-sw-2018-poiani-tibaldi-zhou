package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.graphics.objects.Color;

@Immutable
public final class ToolCardWrapper  extends CardWrapper{

    private final Color color;
    private final int token;

    public ToolCardWrapper(String name, String description, Color color, int token) {
        super(name, description);
        this.color = color;
        this.token = token;
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
