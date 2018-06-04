package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Immutable
public final class RoundTrackWrapper implements JSONable{

    private List<Collection<DiceWrapper>> dicesPerRound;

    public RoundTrackWrapper(List<Collection<DiceWrapper>> dicesPerRound) {
        this.dicesPerRound = new ArrayList<>(dicesPerRound);
    }

    public Collection<DiceWrapper> getDicesPerRound(int round){
        return new ArrayList<>(dicesPerRound.get(round));
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
