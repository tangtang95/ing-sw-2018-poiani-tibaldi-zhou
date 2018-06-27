package org.poianitibaldizhou.sagrada.game.model.cards;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * OVERVIEW: Represents a card with two schema on it.
 */
public class FrontBackSchemaCard implements JSONable {

    private List<SchemaCard> schemaCards;

    /**
     * Constructor.
     * Creates a front back schema card.
     * Schemas are not present yet.
     */
    public FrontBackSchemaCard() {
        this.schemaCards = new ArrayList<>();
    }

    /**
     * Set the schema card on this.
     *
     * @param schemaCard schema card that will be added
     */
    public void setSchemaCard(SchemaCard schemaCard) {
        this.schemaCards.add(SchemaCard.newInstance(schemaCard));
    }

    /**
     * Return the number of schemas present in this
     *
     * @return
     */
    public int size() {
        return schemaCards.size();
    }

    /**
     * @return list containing all the schemas that have been added to this
     */
    public List<SchemaCard> getSchemaCards() {
        return schemaCards;
    }

    /**
     * Check that this contains schemaCard
     *
     * @param schemaCard schema card that will be checked
     * @return true if this contains schemaCard
     */
    public boolean contains(SchemaCard schemaCard) {
        return schemaCards.contains(schemaCard);
    }

    /**
     * Creates a new instance of front back schema card.
     * Deep copy implemented.
     *
     * @param frontBackSchemaCard front back schema card that needs to be copied and instancied
     * @return a new instance of frontBackSchemaCard
     */
    public static FrontBackSchemaCard newInstance(FrontBackSchemaCard frontBackSchemaCard) {
        FrontBackSchemaCard schemaCardList = new FrontBackSchemaCard();
        for (SchemaCard s : frontBackSchemaCard.getSchemaCards())
            schemaCardList.setSchemaCard(SchemaCard.newInstance(s));
        return schemaCardList;
    }

    /**
     * Returns the frontal schema
     *
     * @return frontal schema
     */
    public SchemaCard getFrontSchemaCard() {
        return schemaCards.get(0);
    }

    /**
     * Return the back schema
     *
     * @return back schema
     */
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
        frontBack.put(SharedConstants.BODY, jsonArray);
        main.put(SharedConstants.TYPE, SharedConstants.FRONT_BACK_SCHEMA_CARD);
        main.put(SharedConstants.BODY, frontBack);
        return main;
    }

    /**
     * Return null because the client never send a front back schema card
     *
     * @param jsonObject a JSONObject that contains a FrontBackSchemaCard.
     * @return null
     */
    public static FrontBackSchemaCard toObject(JSONObject jsonObject) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrontBackSchemaCard)) return false;
        FrontBackSchemaCard that = (FrontBackSchemaCard) o;
        return getFrontSchemaCard().equals(that.getFrontSchemaCard()) &&
                getBackSchemaCard().equals(that.getBackSchemaCard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSchemaCards());
    }
}
