package org.poianitibaldizhou.sagrada.game.model.cards;

/**
 * OVERVIEW: Represent a card with a certain name and description
 */
public abstract class Card{

    private final String name;
    private final String description;

    /**
     * Card param for network protocol.
     */
    protected static final String JSON_NAME = "name";
    protected static final String JSON_DESCRIPTION = "description";
    protected static final String JSON_COLOR = "color";

    /**
     * Constructor that creates a card with a name and a description
     * @param name card's name
     * @param description card's description
     */
    protected Card(String name, String description){
        this.name = name;
        this.description = description;
    }

    /**
     * @return name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * @return description of the card
     */
    public String getDescription() {
        return description;
    }

}
