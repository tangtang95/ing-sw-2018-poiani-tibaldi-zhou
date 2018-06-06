package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

@Immutable
public final class ToolCardWrapper  extends CardWrapper implements JSONable{

    /**
     * ToolCard param for network protocol.
     */
    private static final String JSON_TOKENS = "token";

    private final ColorWrapper color;
    private final int token;

    public ToolCardWrapper(String name, String description, ColorWrapper color, int token) {
        super(name, description);
        this.color = color;
        this.token = token;
    }

    public int getTokens() {
        return token;
    }


    /**
     * Convert a toolCardWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject toolCardJSON = new JSONObject();
        toolCardJSON.put(JSON_NAME, this.getName());
        toolCardJSON.put(JSON_TOKENS, this.getTokens());
        main.put(SharedConstants.TYPE, SharedConstants.TOOL_CARD);
        main.put(SharedConstants.BODY,toolCardJSON);
        return main;
    }

    /**
     * Convert a json string in a toolCardWrapper object.
     *
     * @param jsonObject a JSONObject that contains a name of the toolCardWrapper.
     * @return a ToolCardWrapper object or null if the jsonObject is wrong.
     */
    public static ToolCardWrapper toObject(JSONObject jsonObject) {
        return new ToolCardWrapper(
                (String)jsonObject.get(JSON_NAME),
                (String) jsonObject.get(JSON_DESCRIPTION),
                ColorWrapper.valueOf(jsonObject.get(JSON_COLOR).toString()),
                Integer.parseInt(jsonObject.get(JSON_TOKENS).toString())
        );
    }


    public ColorWrapper getColor() {
        return color;
    }

    public int getToken() {
        return token;
    }
}
