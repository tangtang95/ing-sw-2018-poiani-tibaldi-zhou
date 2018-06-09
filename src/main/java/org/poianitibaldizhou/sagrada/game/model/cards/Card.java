package org.poianitibaldizhou.sagrada.game.model.cards;

public abstract class Card{

    private final String name;
    private final String description;

    /**
     * Card param for network protocol.
     */
    protected static final String JSON_NAME = "name";
    protected static final String JSON_DESCRIPTION = "description";
    protected static final String JSON_COLOR = "color";

    protected Card(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
