package org.poianitibaldizhou.sagrada.game.model.cards;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

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

    @Override
    public JSONObject toJSON() {
        // TODO
        return null;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return null;
    }
}
