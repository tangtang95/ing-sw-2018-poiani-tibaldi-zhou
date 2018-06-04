package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

import java.util.ArrayList;
import java.util.Collection;

@Immutable
public final class DraftPoolWrapper implements JSONable{

    private final Collection<DiceWrapper> dices;

    public DraftPoolWrapper(Collection<DiceWrapper> dices) {
        this.dices = new ArrayList<>(dices);
    }

    public Collection<DiceWrapper> getDices() {
        return new ArrayList<>(dices);
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return null;
    }
}
