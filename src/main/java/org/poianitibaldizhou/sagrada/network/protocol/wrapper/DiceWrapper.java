package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Objects;

/**
 * Copy of Dice class in game model.
 */
@Immutable
public final class DiceWrapper implements JSONable{

    /**
     * DiceWrapper param for network protocol.
     */
    private static final String JSON_VALUE = "value";
    private static final String JSON_COLOR = "color";

    /**
     * DiceWrapper parameter.
     */
    public static final int MAX_VALUE = 6;
    public static final int MIN_VALUE = 1;

    /**
     * Dice's number.
     */
    private final int number;

    /**
     * Dice,s color.
     */
    private final ColorWrapper color;

    /**
     * Constructor.
     *
     * @param color of dice.
     * @param number of dice.
     */
    public DiceWrapper(ColorWrapper color, int number) {
        this.color = color;
        this.number = number;
    }


    /**
     * @return the dice number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return the dice color.
     */
    public ColorWrapper getColor() {
        return color;
    }

    /**
     * Convert a diceWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings({"unchecked", "Duplicates"})
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject diceJSON = new JSONObject();
        diceJSON.put(JSON_VALUE, this.getNumber());
        diceJSON.put(JSON_COLOR, this.getColor().name());
        main.put(SharedConstants.TYPE, SharedConstants.DICE);
        main.put(SharedConstants.BODY,diceJSON);
        return main;
    }

    /**
     * Convert a json string in a diceWrapper object.
     *
     * @param jsonObject a JSONObject that contains a dice.
     * @return a DiceWrapper object.
     */
    public static DiceWrapper toObject(JSONObject jsonObject) {
        return new DiceWrapper(ColorWrapper.valueOf((String) jsonObject.get(JSON_COLOR)),
                Integer.parseInt(jsonObject.get(JSON_VALUE).toString()));
    }

    /**
     * @return a dice to string -> number/color
     */
    @Override
    public String toString() {
        return "" + number + "/" + Objects.requireNonNull(color).name().substring(0,1);
    }

    /**
     * @param o the other object to compare.
     * @return true if the DiceWrapper is the same object or if it has the same number and the same color.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiceWrapper)) return false;
        DiceWrapper that = (DiceWrapper) o;
        return getNumber() == that.getNumber() &&
                getColor() == that.getColor();
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getColor());
    }
}
