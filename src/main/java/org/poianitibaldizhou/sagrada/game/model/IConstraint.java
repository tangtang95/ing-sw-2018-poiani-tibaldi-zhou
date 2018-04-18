package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.MismatchingTypeOfConstraintException;

public interface IConstraint {
    public boolean matches(IConstraint other) throws MismatchingTypeOfConstraintException;
    public int getValue();
}
