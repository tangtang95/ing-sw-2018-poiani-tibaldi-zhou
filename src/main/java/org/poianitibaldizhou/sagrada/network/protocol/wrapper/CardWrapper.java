package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

@Immutable
public abstract class CardWrapper implements JSONable{

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
