package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
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

    private final List<DiceWrapper> dices;

    public DraftPoolWrapper(Collection<DiceWrapper> dices) {
        this.dices = new ArrayList<>(dices);
    }

    public List<DiceWrapper> getDices() {
        return new ArrayList<>(dices);
    }

    public DiceWrapper getDice(int index) {
        return dices.get(index);
    }

    public int size(){return dices.size();}

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

    /**
     * Convert a json string in a draftPoolWrapper object.
     *
     * @param jsonObject a JSONObject that contains a draftPool.
     * @return a draftPoolWrapper object.
     */
    public static DraftPoolWrapper toObject(JSONObject jsonObject) {
        JSONObject diceList = (JSONObject) jsonObject.get(JSON_DICE_LIST);
        JSONArray jsonArray = (JSONArray) diceList.get(SharedConstants.BODY);
        Collection<DiceWrapper> diceWrapperList = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject dice = (JSONObject) ((JSONObject) o).get(SharedConstants.BODY);
            diceWrapperList.add(DiceWrapper.toObject(dice));
        }
        return new DraftPoolWrapper(diceWrapperList);
    }

    @Override
    public String toString() {
        return dices.toString();
    }
}
