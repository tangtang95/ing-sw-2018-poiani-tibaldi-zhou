package org.poianitibaldizhou.sagrada.game.model.cards;

import java.util.ArrayList;
import java.util.List;

public class FrontBackSchemaCard {

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
}
