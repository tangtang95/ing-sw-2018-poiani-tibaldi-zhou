package org.poianitibaldizhou.sagrada.game.model.constraint;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * OVERVIEW: Represents a constraint regarding the color.
 */
@Immutable
public class ColorConstraint implements IConstraint {

    private final Color color;

    /**
     * Constructor.
     * Create a ColorConstraint with the color given
     *
     * @param color the color value for this specific object
     */
    public ColorConstraint(Color color) {
        this.color = color;
    }

    /**
     * Global method.
     * Return a list of all colorConstraints for each value of enum ColorWrapper
     *
     * @return a list of all colorConstraints
     */
    public static List<IConstraint> getAllColorConstraints() {
        List<IConstraint> allColorConstraints = new ArrayList<>();
        for (int i = 0; i < Color.values().length; i++) {
            allColorConstraints.add(new ColorConstraint(Color.values()[i]));
        }
        return allColorConstraints;
    }

    /**
     *@return color of the constraint
     */
    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    public boolean matches(IConstraint other) {
        if (other instanceof NoConstraint || !(other instanceof ColorConstraint))
            return true;
        ColorConstraint cc = (ColorConstraint) other;
        return color == cc.getColor();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndexValue() {
        return color.ordinal();
    }

    /**
     * Return true if the color of ColorConstraint is the same
     *
     * @param obj the other object to compares
     * @return true if they have the same color
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ColorConstraint && this.getColor() == ((ColorConstraint) obj).getColor();
    }

    @Override
    public String toString() {
        return color.name().substring(0, 1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor());
    }

}
