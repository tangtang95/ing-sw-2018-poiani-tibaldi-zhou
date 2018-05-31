package org.poianitibaldizhou.sagrada.game.model.cards;

import java.io.Serializable;

public abstract class Card implements Serializable{

    private final String name;
    private final String description;

    /**
     * Card param for network protocol.
     */
    protected static final String JSON_NAME = "name";
    protected static final String JSON_DESCRIPTION = "description";
    protected static final String JSON_TOKEN = "token";
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
