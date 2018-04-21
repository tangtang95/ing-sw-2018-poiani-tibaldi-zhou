package org.poianitibaldizhou.sagrada.game.model;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * This class is a NullObject Pattern to avoid the check of null value
 */
@Immutable
public class NoConstraint implements IConstraint{
    @Override
    public boolean matches(IConstraint other) {
        return true;
    }

    @Override
    public int getIndexValue() {
        return -1;
    }
}
