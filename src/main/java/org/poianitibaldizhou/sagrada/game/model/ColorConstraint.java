package org.poianitibaldizhou.sagrada.game.model;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

@Immutable
public class ColorConstraint implements IConstraint {
    private final Color color;

    /**
     * Constructor.
     * Create a ColorConstraint with the color given
     * @param color the color value for this specific object
     */
    public ColorConstraint(Color color) {
        this.color = color;
    }

    /**
     * Global method.
     * Return a list of all colorConstraints for each value of enum Color
     * @return a list of all colorConstraints
     */
    public static List<IConstraint> getAllColorConstraints(){
        List<IConstraint> allColorConstraints = new ArrayList<>();
        for (int i = 0; i < Color.values().length; i++) {
            allColorConstraints.add(new ColorConstraint(Color.values()[i]));
        }
        return allColorConstraints;
    }

    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    /**
     *
     * @param other another constraint to match
     * @return false only if has the same constraint type and its value is different,
     *         in the other cases it returns true
     */
    @Override
    @Contract(pure = true)
    public boolean matches(IConstraint other) {
        if(other instanceof NoConstraint || !(other instanceof ColorConstraint))
            return true;
        ColorConstraint cc = (ColorConstraint) other;
        return color == cc.getColor();

    }

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
    public boolean equals(Object obj){
        if(!(obj instanceof ColorConstraint))
            return false;
        return this.getColor() == ((ColorConstraint) obj).getColor();
    }

}
