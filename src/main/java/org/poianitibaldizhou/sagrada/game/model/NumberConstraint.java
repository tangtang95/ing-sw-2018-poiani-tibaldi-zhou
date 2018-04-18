package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.MismatchingTypeOfConstraintException;

public class NumberConstraint implements IConstraint {
    private int number;

    public NumberConstraint(int number) {
        this.number = number;
    }

    @Override
    public boolean matches(IConstraint other) throws MismatchingTypeOfConstraintException {
        if(!(other instanceof NumberConstraint)) {
            throw new MismatchingTypeOfConstraintException();
        }
        NumberConstraint nc = (NumberConstraint) other;
        if(nc.getNumber() == this.num)
            return true;
        return false;
    }

    @Override
    public int getValue() {
        return number;
    }

    public int getNumber() {
        return number;
    }
}
