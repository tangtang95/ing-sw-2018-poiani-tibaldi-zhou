package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.JSONClientProtocol;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.ArrayList;
import java.util.List;

public class FrontBackSchemaCardWrapper implements JSONable{

    private final List<SchemaCardWrapper> schemaCards;

    public FrontBackSchemaCardWrapper() {
        this.schemaCards = new ArrayList<>();
    }

    public SchemaCardWrapper getFrontSchemaCard() {
        return schemaCards.get(0);
    }

    public SchemaCardWrapper getBackSchemaCard() {
        return schemaCards.get(1);
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
    @Override
    public Object toObject(JSONObject jsonObject) {
        JSONClientProtocol clientProtocol = new JSONClientProtocol();
        JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
        FrontBackSchemaCardWrapper frontBackSchemaCardWrapper = new FrontBackSchemaCardWrapper();
        for (Object o:jsonArray) {
            JSONObject schemaCard = (JSONObject) o;
            frontBackSchemaCardWrapper.schemaCards.add((SchemaCardWrapper) clientProtocol.convertToObject(schemaCard));
        }
        return frontBackSchemaCardWrapper;
    }

    public List<SchemaCardWrapper> getSchemaCards() {
        return schemaCards;
    }
}
