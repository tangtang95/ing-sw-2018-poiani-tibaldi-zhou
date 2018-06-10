package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Copy of DraftPool class of game model.
 */
@Immutable
public final class DraftPoolWrapper implements JSONable{

    /**
     * draftPoolWrapper param for network protocol.
     */
    private static final String JSON_DICE_LIST = "diceList";

    /**
     * list of diceWrapper in the draftPool.
     */
    private final List<DiceWrapper> dices;

    /**
     * Constructor.
     *
     * @param dices list of dice in the draftPoolWrapper.
     */
    public DraftPoolWrapper(Collection<DiceWrapper> dices) {
        this.dices = new ArrayList<>(dices);
    }

    /**
     * @return the diceWrappers in the draftPoolWrapper
     */
    public List<DiceWrapper> getDices() {
        return new ArrayList<>(dices);
    }

    /**
     * @param index number of diceWrapper to get.
     * @return the diceWrapper required.
     */
    public DiceWrapper getDice(int index) {
        return dices.get(index);
    }

    /**
     * @return the size of the draftPoolWrapper.
     */
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

    /**
     * @return DraftPoolWrapper to string -> diceList.toString()
     */
    @Override
    public String toString() {
        return dices.toString();
    }

    /**
     * @param o the other object to compare.
     * @return true if the DraftPoolWrapper is the same object or if it has the same list of DiceWrapper.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DraftPoolWrapper)) return false;
        DraftPoolWrapper that = (DraftPoolWrapper) o;
        return Objects.equals(getDices(), that.getDices());
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDices());
    }
}
