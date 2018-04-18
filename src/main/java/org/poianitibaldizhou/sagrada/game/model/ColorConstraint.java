package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.MismatchingTypeOfConstraintException;

public class ColorConstraint implements IConstraint {
    Color color;

    public ColorConstraint(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean matches(IConstraint other) throws MismatchingTypeOfConstraintException {
        if(!(other instanceof ColorConstraint)) {
            throw new MismatchingTypeOfConstraintException();
        }
        ColorConstraint cc = (ColorConstraint) other;
        if(cc.getColor() == this.color)
            return true;
        return false;

    }
}
