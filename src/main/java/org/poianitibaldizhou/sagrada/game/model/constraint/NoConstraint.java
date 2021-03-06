package org.poianitibaldizhou.sagrada.game.model.constraint;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.util.Objects;

/**
 * This class is a NullObject Pattern to avoid the check of null value
 */
@Immutable
public class NoConstraint implements IConstraint {

    /**
     * {@inheritDoc}
     * @return always true, because it matches with everything
     */
    @Override
    public boolean matches(IConstraint other) {
        return true;
    }


    /**
     * @throws IllegalStateException always, cannot be implemented for NoConstraint
     */
    @Override
    public int getIndexValue() {
        throw new IllegalStateException(ServerMessage.NO_CONSTRAINT_ILLEGAL_STATE);
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
