package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

@Immutable
public final class PrivateObjectiveCardWrapper extends CardWrapper implements JSONable {

    private final ColorWrapper color;

    public PrivateObjectiveCardWrapper(String name, String description, ColorWrapper color) {
        super(name, description);
        this.color = color;
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
    @Override
    public Object toObject(JSONObject jsonObject) {
        return new PrivateObjectiveCardWrapper(
                (String) jsonObject.get(JSON_NAME),
                (String) jsonObject.get(JSON_DESCRIPTION),
                ColorWrapper.valueOf((String) jsonObject.get(JSON_COLOR))
        );
    }

    /**
     * Fake constructor.
     */
    @SuppressWarnings("unused")
    private PrivateObjectiveCardWrapper() {
        super(null,null);
        this.color = null;
    }

    public ColorWrapper getColor() {
        return color;
    }
}
