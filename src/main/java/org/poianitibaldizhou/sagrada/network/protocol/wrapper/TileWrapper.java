package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Objects;

/**
 * Copy class of Tile in game model.
 */
@Immutable
public final class TileWrapper implements JSONable{

    /**
     * TileWrapper's constraint.
     * Constraint type:
     * - Color
     * - Number
     * - Null
     */
    private final String constraint;

    /**
     * DiceWrapper contained in the TileWrapper.
     */
    private DiceWrapper dice;

    /**
     * TileWrapper param for network protocol.
     */
    private static final String JSON_CONSTRAINT = "constraint";

    /**
     * Constructor.
     *
     * @param constraint tileWrapper constraint.
     */
    public TileWrapper(String constraint) {
        this.constraint = constraint;
        this.dice = null;
    }

    /**
     * Copy constructor.
     *
     * @param tile original TileWrapper.
     */
    private TileWrapper(TileWrapper tile){
        this.constraint = tile.constraint;
        this.dice = tile.dice;
    }

    /**
     * Constructor.
     *
     * @param constraint tileWrapper constraint.
     * @param dice diceWrapper contained.
     */
    TileWrapper(String constraint, DiceWrapper dice) {
        this.constraint = constraint;
        this.dice = dice;
    }

    /**
     * @return the tileWrapper constraint.
     */
    public String getConstraint() {
        return constraint;
    }

    /**
     * @return the diceWrapper contained in the tileWrapper.
     */
    public DiceWrapper getDice() {
        return dice;
    }

    /**
     * new Instance for deep copy.
     *
     * @param t original tileWrapper
     * @return new instance of the same tileWrapper.
     */
    public static TileWrapper newInstance(TileWrapper t) {
        return new TileWrapper(t);
    }

    /**
     * @return the tileWrapper to string -> constraint or dice.toString
     */
    @Override
    public String toString(){
        String val;
        if (dice != null)
            return dice.toString();
        if (constraint == null)
            val = " ";
        else
            val = constraint.substring(0,1);
        return " " + val + " ";
    }

    /**
     * Convert a TileWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject tileJSON =  new JSONObject();
        if(this.getDice() != null)
            tileJSON.put(SharedConstants.DICE, this.getDice().toJSON());
        try {
            int val = Integer.parseInt(constraint);
            tileJSON.put(JSON_CONSTRAINT, val);
        }catch (NumberFormatException e) {
            tileJSON.put(JSON_CONSTRAINT, constraint);
        }
        main.put(SharedConstants.TYPE, SharedConstants.TILE);
        main.put(SharedConstants.BODY,tileJSON);
        return main;
    }

    /**
     * Convert a json string in a TileWrapper object.
     *
     * @param jsonObject a JSONObject that contains a TileWrapper.
     * @return a tile object or null if the jsonObject is wrong.
     */
    public static TileWrapper toObject(JSONObject jsonObject) {
        Object object = jsonObject.get(JSON_CONSTRAINT);
        String constraintValue;
        if (object == null)
            constraintValue = null;
        else if (object instanceof Long || object instanceof Integer)
            constraintValue = String.valueOf(object.toString());
        else
            constraintValue = (String) object;

        if (jsonObject.containsKey(SharedConstants.DICE)) {
            DiceWrapper readDice = DiceWrapper.toObject(
                    (JSONObject) ((JSONObject) jsonObject.get(SharedConstants.DICE)).get(SharedConstants.BODY));
            return new TileWrapper(constraintValue, readDice);
        }
        return new TileWrapper(constraintValue);
    }

    /**
     * @param o the other object to compare.
     * @return true if the TileWrapper is the same object or the constraint and the diceWrapper are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TileWrapper)) return false;
        TileWrapper that = (TileWrapper) o;
        return Objects.equals(getConstraint(), that.getConstraint()) &&
                Objects.equals(getDice(), that.getDice());
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {

        return Objects.hash(getConstraint(), getDice());
    }
}
