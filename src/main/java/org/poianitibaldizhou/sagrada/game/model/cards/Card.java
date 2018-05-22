package org.poianitibaldizhou.sagrada.game.model.cards;

public abstract class Card {

    private final String name;
    private final String description;

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
