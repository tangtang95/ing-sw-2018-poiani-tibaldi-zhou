package org.poianitibaldizhou.sagrada.game.model;

public class NumberConstraint implements IConstraint {
    private int num;

    public NumberConstraint(int x) {
        this.num = x;
    }

    @Override
    public boolean matches(IConstraint other) {
        return false;
    }

    public int getNumber() {
        return num;
    }
}
