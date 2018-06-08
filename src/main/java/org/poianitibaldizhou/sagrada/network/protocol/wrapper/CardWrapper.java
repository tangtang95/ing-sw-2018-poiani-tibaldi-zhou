package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Copy class of Card.
 *
 */
@Immutable
public abstract class CardWrapper{

    /**
     * Card param for network protocol.
     */
    protected static final String JSON_NAME = "name";
    static final String JSON_DESCRIPTION = "description";
    static final String JSON_COLOR = "color";

    protected String name;
    protected String description;

    public CardWrapper(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
