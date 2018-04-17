package org.poianitibaldizhou.sagrada.game.model.card;

public abstract class Card {

    private String name;
    private String description;

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
