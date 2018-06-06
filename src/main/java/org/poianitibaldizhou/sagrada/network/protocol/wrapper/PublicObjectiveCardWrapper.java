package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

@Immutable
public final class PublicObjectiveCardWrapper extends CardWrapper implements JSONable{

    private final int point;

    /**
     * PublicObjectiveCardWrapper param for network protocol.
     */
    private static final String JSON_CARD_POINT = "cardPoint";

    public PublicObjectiveCardWrapper(String name, String description, int point) {
        super(name, description);
        this.point = point;
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

    public int getCardPoint() {
        return point;
    }
}
