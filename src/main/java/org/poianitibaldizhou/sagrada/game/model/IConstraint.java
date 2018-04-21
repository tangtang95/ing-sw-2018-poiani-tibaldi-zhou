package org.poianitibaldizhou.sagrada.game.model;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public interface IConstraint {
    /**
     *
     * @param other another constraint to match
     * @return false only if has the same constraint type and its value is different,
     *         in the other cases it returns true
     */
    public boolean matches(IConstraint other);

    /**
     *
     * @return index value useful in an array for counting the number of constraint founded
     */
    public int getIndexValue();
}
