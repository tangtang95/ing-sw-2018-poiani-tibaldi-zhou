package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.DiceInvalidNumberException;

public class Dice {
    private final NumberConstraint numberConstraint;
    private final ColorConstraint colorConstraint;

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 6;

    /**
     * Constructor.
     * Creates a Dice with a certain NumberConstraint and ColorConstraint.
     * @param numberConstraint dice's number contraint
     * @param colorConstraint dice's color constraint
     * @throws DiceInvalidNumberException if number contraints represents a number greater then MAX_VALUE
     *                                    or lesser then MIN_VALUE
     *
     */
    public Dice(NumberConstraint numberConstraint, ColorConstraint colorConstraint) throws DiceInvalidNumberException {
        int number = numberConstraint.getNumber();
        if(number < MIN_VALUE || number > MAX_VALUE)
            throw new DiceInvalidNumberException();
        this.numberConstraint = new NumberConstraint(number);
        this.colorConstraint = colorConstraint;
    }

    /**
     * Constructor.
     * Creates a Dice of a certain number and color.
     *
     * @param number dice's number
     * @param color dice's color
     * @throws DiceInvalidNumberException if number if greater then MAX_VALUE or lesser then MIN_VALUE
     */
    public Dice(int number, Color color) throws DiceInvalidNumberException {
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
        return (o.getColorConstraint() == this.getColorConstraint() && o.getNumber() == this.getNumber())? true:false;
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
        try {
            return new Dice(MAX_VALUE + 1 - this.numberConstraint.getNumber(), this.colorConstraint.getColor());
        } catch(DiceInvalidNumberException dine) {
            dine.printStackTrace();
        }
        return null;
    }
}
