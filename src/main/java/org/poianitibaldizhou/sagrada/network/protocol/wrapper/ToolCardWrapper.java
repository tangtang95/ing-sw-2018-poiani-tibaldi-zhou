package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Objects;

/**
 * Copy of class ToolCard in game model.
 */
@Immutable
public final class ToolCardWrapper  extends CardWrapper implements JSONable{

    /**
     * ToolCard param for network protocol.
     */
    private static final String JSON_TOKENS = "token";

    /**
     * ToolCardWrapper's color.
     */
    private final ColorWrapper color;

    /**
     * number of token on the toolCardWrapper.
     */
    private final int token;

    /**
     * Constructor.
     *
     * @param name card name
     * @param description card description.
     * @param color card color.
     * @param token number of token on toolCard.
     */
    public ToolCardWrapper(String name, String description, ColorWrapper color, int token) {
        super(name, description);
        this.color = color;
        this.token = token;
    }

    /**
     * @return the toolCardWrapper color.
     */
    public ColorWrapper getColor() {
        return color;
    }

    /**
     * @return the token on toolCardWrapper.
     */
    public int getToken() {
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
        toolCardJSON.put(JSON_TOKENS, this.getToken());
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

    /**
     * @param o the other object to compare.
     * @return true if the ToolCardWrapper is the same object or
     * if it has the same color, name, description and number of token.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToolCardWrapper)) return false;
        ToolCardWrapper that = (ToolCardWrapper) o;
        return getToken() == that.getToken() &&
                getColor() == that.getColor()&&
                getName().equals(that.getName()) &&
                getDescription().equals(that.getDescription());
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {

        return Objects.hash(getColor(), getToken(), getName(),getDescription());
    }
}
