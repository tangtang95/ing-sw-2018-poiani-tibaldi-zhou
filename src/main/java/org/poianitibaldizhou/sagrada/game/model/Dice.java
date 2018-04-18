package org.poianitibaldizhou.sagrada.game.model;

public class Dice {
    NumberConstraint nc;
    ColorConstraint cc;

    public Dice(NumberConstraint nc, ColorConstraint cc) {
        this.nc = nc;
        this.cc = cc;
    }

    public Dice(int number, Color color) {
        nc = new NumberConstraint(number);
        cc = new ColorConstraint(color);
    }

    public int getNumber() {
        return nc.getNumber();
    }

    public Color getColor() {
        return cc.getColor();
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
