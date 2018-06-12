package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Objects;

/**
 * Copy class of PrivateObjectiveCard in the game model.
 */
@Immutable
public final class PrivateObjectiveCardWrapper extends CardWrapper implements JSONable {

    /**
     * Card's color.
     */
    private final ColorWrapper color;

    /**
     * Constructor.
     *
     * @param name card name.
     * @param description card description.
     * @param color card color.
     */
    public PrivateObjectiveCardWrapper(String name, String description, ColorWrapper color) {
        super(name, description);
        this.color = color;
    }

    /**
     * @return the card color.
     */
    public ColorWrapper getColor() {
        return color;
    }

    /**
     * Convert a privateObjectiveCardWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject pocJSON = new JSONObject();
        pocJSON.put(JSON_NAME, this.getName());
        pocJSON.put(JSON_DESCRIPTION, this.getDescription());
        pocJSON.put(JSON_COLOR,this.getColor().name());
        main.put(SharedConstants.TYPE, SharedConstants.PRIVATE_OBJECTIVE_CARD);
        main.put(SharedConstants.BODY,pocJSON);
        return main;
    }

    /**
     * Convert a json string in a privateObjectiveCardWrapper object.
     *
     * @param jsonObject a JSONObject that contains a name of the privateObjectiveCardWrapper.
     * @return a privateObjectiveCardWrapper object or null if the jsonObject is wrong.
     */
    public static PrivateObjectiveCardWrapper toObject(JSONObject jsonObject) {
        return new PrivateObjectiveCardWrapper(
                (String) jsonObject.get(JSON_NAME),
                (String) jsonObject.get(JSON_DESCRIPTION),
                ColorWrapper.valueOf((String) jsonObject.get(JSON_COLOR))
        );
    }

    /**
     * @param o the other object to compare.
     * @return true if the PrivateObjectiveCardWrapper is the same object or
     * if it has the same color, name and description.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivateObjectiveCardWrapper)) return false;
        PrivateObjectiveCardWrapper that = (PrivateObjectiveCardWrapper) o;
        return getColor() == that.getColor() &&
                getName().equals(that.getName()) &&
                getDescription().equals(that.getDescription());
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {

        return Objects.hash(getColor(),getName(),getDescription());
    }
}
