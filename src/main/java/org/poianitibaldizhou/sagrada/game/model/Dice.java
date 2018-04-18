package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.DiceNumberException;

public class Dice {
    NumberConstraint nc;
    ColorConstraint cc;

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 6;

    public Dice(NumberConstraint nc, ColorConstraint cc) throws DiceNumberException {
        setNumber(nc.getNumber());
        this.cc = cc;
    }

    public Dice(int number, Color color) throws DiceNumberException {
        setNumber(number);
        cc = new ColorConstraint(color);
    }

    private void setNumber(int number) throws DiceNumberException {
        if(number < 1 || number > 6)
            throw new DiceNumberException();
        nc = new NumberConstraint(number);
    }

    public int getNumber() {
        return nc.getNumber();
    }

    public Color getColor() {
        return cc.getColor();
    }

    public NumberConstraint getNumberConstraint() {
        return nc;
    }

    public ColorConstraint getColorConstraint() {
        return cc;
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

    public ColorConstraint getCc() {
        return cc;
    }
}
