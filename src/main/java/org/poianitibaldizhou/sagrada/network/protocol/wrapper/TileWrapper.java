package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Objects;


@Immutable
public final class TileWrapper implements JSONable{

    private final String constraint;
    private DiceWrapper dice;

    /**
     * TileWrapper param for network protocol.
     */
    private static final String JSON_CONSTRAINT = "constraint";

    public TileWrapper(String constraint) {
        this.constraint = constraint;
        this.dice = null;
    }

    private TileWrapper(TileWrapper tile){
        this.constraint = tile.constraint;
        this.dice = tile.dice;
    }

    public static TileWrapper newInstance(TileWrapper t) {
        return new TileWrapper(t);
    }

    public DiceWrapper getDice() {
        return dice;
    }

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
        tileJSON.put(JSON_CONSTRAINT, constraint);
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
        TileWrapper tile;
        Object object = jsonObject.get(JSON_CONSTRAINT);
        String constraintValue;
        if (object == null)
            constraintValue = null;
        else if (object instanceof Long || object instanceof Integer)
            constraintValue = String.valueOf(object.toString());
        else
            constraintValue = (String) object;
        tile = new TileWrapper(constraintValue);

        if (jsonObject.containsKey(SharedConstants.DICE)) {
            DiceWrapper readDice = DiceWrapper.toObject(
                    (JSONObject) ((JSONObject) jsonObject.get(SharedConstants.DICE)).get(SharedConstants.BODY));
            tile.setDice(readDice);
        }
        return tile;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setDice(DiceWrapper dice) {
        this.dice = dice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TileWrapper)) return false;
        TileWrapper that = (TileWrapper) o;
        return Objects.equals(getConstraint(), that.getConstraint()) &&
                Objects.equals(getDice(), that.getDice());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getConstraint(), getDice());
    }
}
