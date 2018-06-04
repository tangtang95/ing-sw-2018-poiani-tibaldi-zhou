package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

@Immutable
public abstract class CardWrapper implements JSONable{

    /**
     * Card param for network protocol.
     */
    protected static final String JSON_NAME = "name";
    protected static final String JSON_DESCRIPTION = "description";
    protected static final String JSON_COLOR = "color";

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
