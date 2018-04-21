package org.poianitibaldizhou.sagrada.game.model.cards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;

@Immutable
public final class ConstraintType {
    private final TileConstraintType tileConstraint;
    private DiceConstraintType diceConstraint;

    public ConstraintType(TileConstraintType tileConstraint, DiceConstraintType diceConstraint){
        this.tileConstraint = tileConstraint;
        this.diceConstraint = diceConstraint;
    }

    @Contract(pure = true)
    public TileConstraintType getTileConstraint() {
        return tileConstraint;
    }

    @Contract(pure = true)
    public DiceConstraintType getDiceConstraint() {
        return diceConstraint;
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof ConstraintType))
            return false;
        ConstraintType otherConstraint = (ConstraintType) other;
        return this.getTileConstraint() == otherConstraint.getTileConstraint()
                && this.getDiceConstraint() == otherConstraint.getDiceConstraint();
    }
}
