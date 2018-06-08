package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Copy class of Card.
 * Abstract class that represents a generic card of game.
 */
@Immutable
public abstract class CardWrapper{

    /**
     * Card param for network protocol.
     */
    protected static final String JSON_NAME = "name";
    static final String JSON_DESCRIPTION = "description";
    static final String JSON_COLOR = "color";

    /**
     * Card name.
     */
    protected String name;

    /**
     * Card description.
     */
    protected String description;

    /**
     * Constructor.
     *
     * @param name card name.
     * @param description card description.
     */
    CardWrapper(String name, String description){
        this.name = name;
        this.description = description;
    }

    /**
     * @return the card description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the card name.
     */
    public String getName() {
        return name;
    }
}
