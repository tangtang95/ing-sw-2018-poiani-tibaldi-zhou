package org.poianitibaldizhou.sagrada.game.model;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class Dice {
    private final NumberConstraint numberConstraint;
    private final ColorConstraint colorConstraint;

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 6;


    /**
     * Constructor.
     * Creates a Dice with a certain NumberConstraint and ColorConstraint.
     * @param numberConstraint dice's number constraint
     * @param colorConstraint dice's color constraint
     *
     */
    public Dice(NumberConstraint numberConstraint, ColorConstraint colorConstraint) {
        if(numberConstraint.getNumber() < MIN_VALUE || numberConstraint.getNumber() > MAX_VALUE)
            throw new IllegalArgumentException("Invalid number: number must be in range [1,6]");
        this.numberConstraint = numberConstraint;
        this.colorConstraint = colorConstraint;
    }

    /**
     * Constructor.
     * Creates a Dice of a certain number and color.
     *
     * @param number dice's number
     * @param color dice's color
     */
    public Dice(int number, Color color)  {
        this(new NumberConstraint(number), new ColorConstraint(color));
    }

    public int getNumber() {
        return numberConstraint.getNumber();
    }

    public Color getColor() {
        return colorConstraint.getColor();
    }

    public NumberConstraint getNumberConstraint() {
        return numberConstraint;
    }

    public ColorConstraint getColorConstraint() {
        return colorConstraint;
    }

    @Override
    public boolean equals(Object oth){
        if(!(oth instanceof Dice))
            return false;
        Dice o = (Dice)oth;
        return o.getColor() == this.getColor() && o.getNumber() == this.getNumber();
    }

    @Override
    public String toString() {
        return "Dice("+numberConstraint.getNumber()+","+colorConstraint.getColor()+")";
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
}
