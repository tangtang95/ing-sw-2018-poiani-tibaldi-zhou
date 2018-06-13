package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Copy of FrontBackSchemaCard of the game model.
 */
@Immutable
public final class FrontBackSchemaCardWrapper implements JSONable{

    /**
     * List of two schemaCardWrapper, represent the two face of schemaCard.
     */
    private final List<SchemaCardWrapper> schemaCards;

    /**
     * Constructor.
     */
    public FrontBackSchemaCardWrapper() {
        this.schemaCards = new ArrayList<>();
    }

    /**
     * Constructor.
     */
    public FrontBackSchemaCardWrapper(List<SchemaCardWrapper> schemaCardWrappers) {
        this.schemaCards = schemaCardWrappers;
    }

    /**
     * @return the front schemaCardWrapper.
     */
    public SchemaCardWrapper getFrontSchemaCard() {
        return schemaCards.get(0);
    }

    /**
     * @return the back of schemaCardWrapper.
     */
    public SchemaCardWrapper getBackSchemaCard() {
        return schemaCards.get(1);
    }

    /**
     * @return the frontBack of schemaCardWrapper.
     */
    public List<SchemaCardWrapper> getSchemaCards() {
        return schemaCards;
    }

    /**
     * Convert a FrontBackSchemaCardWrapper in a JSONObject.
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
     * Convert a json string in a FrontBackSchemaCardWrapper object.
     *
     * @param jsonObject a JSONObject that contains a FrontBackSchemaCard.
     * @return a FrontBackSchemaCardWrapper object.
     */
    public static FrontBackSchemaCardWrapper toObject(JSONObject jsonObject) {
        JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
        FrontBackSchemaCardWrapper frontBackSchemaCardWrapper = new FrontBackSchemaCardWrapper();
        for (Object o:jsonArray) {
            JSONObject schemaCard = (JSONObject) ((JSONObject) o).get(SharedConstants.BODY);
            frontBackSchemaCardWrapper.schemaCards.add(SchemaCardWrapper.toObject(schemaCard));
        }
        return frontBackSchemaCardWrapper;
    }

    /**
     * @param o the other object to compare.
     * @return true if the FrontBackSchemaCardWrapper is the same object or if it has the same
     * list of SchemaCardWrapper.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrontBackSchemaCardWrapper)) return false;
        FrontBackSchemaCardWrapper that = (FrontBackSchemaCardWrapper) o;
        return Objects.equals(getSchemaCards(), that.getSchemaCards());
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSchemaCards());
    }
}
