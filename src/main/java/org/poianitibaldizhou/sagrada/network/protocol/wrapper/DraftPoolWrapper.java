package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.JSONClientProtocol;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Immutable
public final class DraftPoolWrapper implements JSONable{

    /**
     * draftPoolWrapper param for network protocol.
     */
    private static final String JSON_DICE_LIST = "diceList";

    private final Collection<DiceWrapper> dices;

    public DraftPoolWrapper(Collection<DiceWrapper> dices) {
        this.dices = new ArrayList<>(dices);
    }

    public Collection<DiceWrapper> getDices() {
        return new ArrayList<>(dices);
    }

    /**
     * Convert a draftPoolWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        /*This method is empty because the client never send a publicObjectiveCard*/
        return null;

    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        JSONClientProtocol jsonClientProtocol = new JSONClientProtocol();
        JSONArray jsonArray = (JSONArray) jsonObject.get(JSON_DICE_LIST);
        List<DiceWrapper> diceWrapperList = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject dice = (JSONObject) o;
            diceWrapperList.add((DiceWrapper) jsonClientProtocol.convertToObject(dice));
        }
        return null;
    }
}
