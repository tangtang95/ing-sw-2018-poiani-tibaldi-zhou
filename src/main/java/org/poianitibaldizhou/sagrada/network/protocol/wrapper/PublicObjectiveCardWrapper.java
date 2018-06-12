package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.JSONable;

import java.util.Objects;

/**
 * Copy class of PublicObjectiveCard in the model.
 */
@Immutable
public final class PublicObjectiveCardWrapper extends CardWrapper implements JSONable{

    /**
     * Card's point.
     */
    private final int point;

    /**
     * PublicObjectiveCardWrapper param for network protocol.
     */
    private static final String JSON_CARD_POINT = "cardPoint";

    /**
     * Constructor.
     *
     * @param name card name.
     * @param description card description.
     * @param point card point.
     */
    public PublicObjectiveCardWrapper(String name, String description, int point) {
        super(name, description);
        this.point = point;
    }

    /**
     * @return the card point.
     */
    public int getCardPoint() {
        return point;
    }

    /**
     * Convert a publicObjectiveCardWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        /*This method is empty because the client never send a publicObjectiveCard*/
        return null;
    }

    /**
     * Convert a json string in a publicObjectiveCardWrapper object.
     *
     * @param jsonObject a JSONObject that contains a name of the publicObjectiveCardWrapper.
     * @return a publicObjectiveCardWrapper object or null if the jsonObject is wrong.
     */
    public static PublicObjectiveCardWrapper toObject(JSONObject jsonObject) {
        return new PublicObjectiveCardWrapper(
                (String) jsonObject.get(JSON_NAME),
                (String) jsonObject.get(JSON_DESCRIPTION),
                Integer.parseInt(jsonObject.get(JSON_CARD_POINT).toString())
        );
    }

    /**
     * @param o the other object to compare.
     * @return true if the PublicObjectiveCardWrapper is the same object or
     * if it has the same card point, name and description.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicObjectiveCardWrapper)) return false;
        PublicObjectiveCardWrapper that = (PublicObjectiveCardWrapper) o;
        return point == that.point &&
                getName().equals(that.getName()) &&
                getDescription().equals(that.getDescription());
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {

        return Objects.hash(point,getName(),getDescription());
    }
}
