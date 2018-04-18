package org.poianitibaldizhou.sagrada.game.model;

public class NumberConstraint implements IConstraint {
    private int number;

    public NumberConstraint(int number) {
        this.number = number;
    }

    @Override
    public boolean matches(IConstraint other) {
        if(!(other instanceof NumberConstraint)) {
            return false;
        }
        NumberConstraint nc = (NumberConstraint) other;
        return number == nc.getNumber();
    }

    @Override
    public int getValue() {
        return number - 1;
    }

    public int getNumber() {
        return number;
    }
}
