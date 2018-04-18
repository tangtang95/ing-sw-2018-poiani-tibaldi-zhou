package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.DiceInvalidNumberException;

public class Dice {
    private NumberConstraint numberConstraint;
    private ColorConstraint colorConstraint;

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 6;

    public Dice(NumberConstraint numberConstraint, ColorConstraint colorConstraint) throws DiceInvalidNumberException {
        setNumber(numberConstraint.getNumber());
        this.colorConstraint = colorConstraint;
    }

    public Dice(int number, Color color) throws DiceInvalidNumberException {
        setNumber(number);
        colorConstraint = new ColorConstraint(color);
    }

    private void setNumber(int number) throws DiceInvalidNumberException {
        if(number < 1 || number > 6)
            throw new DiceInvalidNumberException();
        numberConstraint = new NumberConstraint(number);
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
}
