package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.MismatchingTypeOfConstraintException;

public class NumberConstraint implements IConstraint {
    private int num;

    public NumberConstraint(int x) {
        this.num = x;
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

    public int getNumber() {
        return num;
    }
}
