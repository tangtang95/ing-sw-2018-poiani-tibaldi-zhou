package org.poianitibaldizhou.sagrada.game.model.constraint;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.io.Serializable;

@Immutable
public interface IConstraint extends Serializable{
    /**
     * @param other another constraint to match
     * @return false only if has the same constraint type and its value is different,
     * in the other cases it returns true
     */
    boolean matches(IConstraint other);

    /**
     * @return index value useful in an array to count the number of constraint founded
     */
    int getIndexValue();
}
