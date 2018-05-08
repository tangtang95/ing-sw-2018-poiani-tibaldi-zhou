package org.poianitibaldizhou.sagrada.game.model.constraint;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Objects;

/**
 * This class is a NullObject Pattern to avoid the check of null value
 */
@Immutable
public class NoConstraint implements IConstraint {

    /**
     * @param other another constraint to match
     * @return return always true because it has no constraint
     */
    @Override
    public boolean matches(IConstraint other) {
        return true;
    }

    @Override
    public int getIndexValue() {
        return -1;
    }

    /**
     * Return true if obj is a NoConstraint
     *
     * @param obj the other object to compare
     * @return true if obj is a NoConstraint
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof NoConstraint;
    }

    public String toString() {
        return " ";
    }

    public int hashCode() {
        return Objects.hash(NoConstraint.class);
    }
}
