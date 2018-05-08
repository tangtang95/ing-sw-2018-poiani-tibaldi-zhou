package org.poianitibaldizhou.sagrada.game.model.constraint;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.Dice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Immutable
public class NumberConstraint implements IConstraint {
    private int number;

    /**
     * Constructor.
     * Create a NumberConstraint with a given number between Dice.MIN_VALUE and Dice.MAX_VALUE
     *
     * @param number number of the dice
     */
    public NumberConstraint(int number) {
        if (number < Dice.MIN_VALUE || number > Dice.MAX_VALUE)
            throw new IllegalArgumentException("Invalid number: number must be in range [" +
                    Dice.MIN_VALUE + "," + Dice.MAX_VALUE + "]");
        this.number = number;
    }

    /**
     * Global method.
     * Return a list of all numberConstraint from Dice.MIN_VALUE to Dice.MAX_VALUE
     *
     * @return a list of all numberConstraint
     */
    public static List<IConstraint> getAllNumberConstraint() {
        List<IConstraint> allNumberConstraint = new ArrayList<>();
        for (int i = Dice.MIN_VALUE; i <= Dice.MAX_VALUE; i++) {
            allNumberConstraint.add(new NumberConstraint(i));
        }
        return allNumberConstraint;
    }

    /**
     * @param other another constraint to match
     * @return false only if has the same constraint type and its value is different,
     * in the other cases it returns true
     */
    @Override
    @Contract(pure = true)
    public boolean matches(IConstraint other) {
        if (other instanceof NoConstraint || !(other instanceof NumberConstraint)) {
            return true;
        }
        NumberConstraint nc = (NumberConstraint) other;
        return number == nc.getNumber();
    }

    @Override
    public int getIndexValue() {
        return getNumber() - 1;
    }

    @Contract(pure = true)
    public int getNumber() {
        return number;
    }

    /**
     * Return true if the number of NumberConstraint is the same
     *
     * @param obj the other object to compares
     * @return true if they have the same number
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NumberConstraint))
            return false;
        return this.getNumber() == ((NumberConstraint) obj).getNumber();
    }

    public String toString() {
        return "" + number;
    }

    public int hashCode() {
        return Objects.hash(getNumber());
    }
}
