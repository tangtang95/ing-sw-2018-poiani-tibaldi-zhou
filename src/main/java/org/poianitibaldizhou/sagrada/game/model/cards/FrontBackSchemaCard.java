package org.poianitibaldizhou.sagrada.game.model.cards;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.ArrayList;
import java.util.List;

public class FrontBackSchemaCard implements JSONable{

    private List<SchemaCard> schemaCards;

    public FrontBackSchemaCard() {
        this.schemaCards = new ArrayList<>();
    }

    public void setSchemaCard(SchemaCard schemaCard) {
        this.schemaCards.add(schemaCard);
    }

    public int size() {
        return schemaCards.size();
    }

    public List<SchemaCard> getSchemaCards() {
        return schemaCards;
    }

    public boolean contains(SchemaCard schemaCard) {
        return schemaCards.contains(schemaCard);
    }

    public static FrontBackSchemaCard newInstance( FrontBackSchemaCard frontBackSchemaCard) {
       FrontBackSchemaCard schemaCardList = new FrontBackSchemaCard();
       for (SchemaCard s : frontBackSchemaCard.getSchemaCards())
           schemaCardList.setSchemaCard(SchemaCard.newInstance(s));
       return schemaCardList;
    }

    public SchemaCard getFrontSchemaCard() {
        return schemaCards.get(0);
    }

    public SchemaCard getBackSchemaCard() {
        return schemaCards.get(1);
    }

    /**
     * Convert a FrontBackSchemaCard in a JSONObject.
     *
     * @return a JSONObject.
     */

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject frontBack = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (SchemaCard s : this.getSchemaCards())
            jsonArray.add(s.toJSON());
        frontBack.put(SharedConstants.TYPE, SharedConstants.COLLECTION);
        frontBack.put(SharedConstants.BODY,jsonArray);
        main.put(SharedConstants.TYPE, SharedConstants.FRONT_BACK_SCHEMA_CARD);
        main.put(SharedConstants.BODY,frontBack);
        return main;
    }

    /**
     * Convert a json string in a FrontBackSchemaCard object.
     *
     * @param jsonObject a JSONObject that contains a FrontBackSchemaCard.
     * @return a FrontBackSchemaCard object.
     */
    public static FrontBackSchemaCard toObject(JSONObject jsonObject) {
        /*This method is empty because the client never send a publicObjectiveCard*/
        return null;
    }
}
