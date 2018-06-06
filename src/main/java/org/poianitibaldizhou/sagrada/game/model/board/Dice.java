package org.poianitibaldizhou.sagrada.game.model.board;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.io.Serializable;
import java.util.Objects;

/**
 * OVERVIEW: Dice: represents a dice with a certain number and a certain color.
 * Numbers are integer in the interval [MIN_VALUE, MAX_VALUE].
 */
@Immutable
public class Dice implements Serializable, JSONable{

    private final NumberConstraint numberConstraint;
    private final ColorConstraint colorConstraint;

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 6;

    /**
     * Dice param for network protocol.
     */
    private static final String JSON_VALUE = "value";
    private static final String JSON_COLOR = "color";

    /**
     * Constructor.
     * Creates a Dice with a certain NumberConstraint and ColorConstraint.
     *
     * @param numberConstraint dice's number constraint
     * @param colorConstraint  dice's color constraint
     */
    public Dice(NumberConstraint numberConstraint, ColorConstraint colorConstraint) {
        if (numberConstraint.getNumber() < MIN_VALUE || numberConstraint.getNumber() > MAX_VALUE)
            throw new IllegalArgumentException("Invalid number: number must be in range [" +
                    MIN_VALUE + "," + MAX_VALUE + "]");
        this.numberConstraint = numberConstraint;
        this.colorConstraint = colorConstraint;
    }

    /**
     * Constructor.
     * Creates a Dice of a certain number and color.
     *
     * @param number dice's number
     * @param color  dice's color
     */
    public Dice(int number, Color color) {
        this(new NumberConstraint(number), new ColorConstraint(color));
    }

    // GETTER
    @Contract(pure = true)
    public int getNumber() {
        return numberConstraint.getNumber();
    }

    @Contract(pure = true)
    public Color getColor() {
        return colorConstraint.getColor();
    }

    @Contract(pure = true)
    public NumberConstraint getNumberConstraint() {
        return numberConstraint;
    }

    @Contract(pure = true)
    public ColorConstraint getColorConstraint() {
        return colorConstraint;
    }

    /**
     * Check if this dice is similar to another dice; in other words, if the dices have same color or same number
     *
     * @param other the other dice to compare
     * @return true if the dices have same color or same number
     */
    @Contract(pure = true)
    public boolean isSimilar(@NotNull Dice other) {
        return this.getColorConstraint().equals(other.getColorConstraint()) ||
                this.getNumberConstraint().equals(other.getNumberConstraint());
    }

    @Contract(pure = true)
    public boolean hasSameColor(Dice other) {
        return other != null && this.getColorConstraint().equals(other.getColorConstraint());
    }

    @Contract(pure = true)
    public boolean hasSameNumber(Dice other){
        return other != null && this.getNumberConstraint().equals(other.getNumberConstraint());
    }

    /**
     * Returns a dice of the same color of this, but the number is the one at the opposite
     * of the current face.
     *
     * @return the current dice inverted
     */
    public Dice pourOverDice() {
        return new Dice(MAX_VALUE + 1 - this.numberConstraint.getNumber(), this.colorConstraint.getColor());
    }

    @Override
    public boolean equals(Object oth) {
        if (!(oth instanceof Dice))
            return false;
        Dice o = (Dice) oth;
        return o.getColor() == this.getColor() && o.getNumber() == this.getNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(Dice.class, numberConstraint.getNumber(), colorConstraint.getColor());
    }

    @Override
    public String toString() {
        return "" + numberConstraint.toString() + "/" + colorConstraint.toString();
    }

    /**
     * Convert a dice in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
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
     * Convert a json string in a dice object.
     *
     * @param jsonObject a JSONObject that contains a dice.
     * @return a Dice object.
     */
    public static Dice toObject(JSONObject jsonObject) {
        return new Dice(Integer.parseInt(jsonObject.get(JSON_VALUE).toString()),
                Color.valueOf((String) jsonObject.get(JSON_COLOR)));
    }

}
