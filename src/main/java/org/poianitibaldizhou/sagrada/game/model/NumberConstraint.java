package org.poianitibaldizhou.sagrada.game.model;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class NumberConstraint implements IConstraint {
    private int number;

    public NumberConstraint(int number) {
        this.number = number;
    }

    @Override
    public boolean matches(IConstraint other) {
        if(other instanceof NoConstraint)
            return true;
        if(!(other instanceof NumberConstraint)) {
            return true;
        }
        NumberConstraint nc = (NumberConstraint) other;
        return number == nc.getNumber();
    }

    @Override
    public int getIndexValue() {
        return number - 1;
    }

    public int getNumber() {
        return number;
    }
}
