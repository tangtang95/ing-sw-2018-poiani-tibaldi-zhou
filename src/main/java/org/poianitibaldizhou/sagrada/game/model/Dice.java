package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.DiceInvalidNumberException;

public class Dice {
    private final NumberConstraint numberConstraint;
    private final ColorConstraint colorConstraint;

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 6;

    public Dice(NumberConstraint numberConstraint, ColorConstraint colorConstraint) throws DiceInvalidNumberException {
        int number = numberConstraint.getNumber();
        if(number < MIN_VALUE || number > MAX_VALUE)
            throw new DiceInvalidNumberException();
        this.numberConstraint = new NumberConstraint(number);
        this.colorConstraint = colorConstraint;
    }

    public Dice(int number, Color color) throws DiceInvalidNumberException {
        if(number < MIN_VALUE || number > MAX_VALUE)
            throw new DiceInvalidNumberException();
        numberConstraint = new NumberConstraint(number);
        colorConstraint = new ColorConstraint(color);
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
        if(o.getColor() == this.getColor() && o.getNumber() == this.getNumber())
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Dice("+numberConstraint.getNumber()+","+colorConstraint.getColor()+")";
    }

    public Dice pourOverDice() {
        try {
            return new Dice(7 - this.numberConstraint.getNumber(), this.colorConstraint.getColor());
        } catch(DiceInvalidNumberException dine) {
            dine.printStackTrace();
        }
        return null;
    }
}
