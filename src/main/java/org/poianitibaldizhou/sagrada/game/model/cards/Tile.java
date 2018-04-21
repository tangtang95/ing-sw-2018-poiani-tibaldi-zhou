package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.exception.ConstraintTypeException;
import org.poianitibaldizhou.sagrada.exception.TileFilledException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.NoConstraint;

public class Tile{

    private final IConstraint constraint;
    private Dice dice = null;

    public Tile(){
        constraint = new NoConstraint();
    }

    public Tile(IConstraint constraint){
        this.constraint = constraint;
    }

    public Tile(Tile tile) {
        dice = tile.dice;
        constraint = tile.constraint;
    }

    public void setDice(Dice dice) throws ConstraintTypeException, TileFilledException {
        if(this.dice != null)
            throw new TileFilledException("A dice is already occupying the tile");
        if(isDicePositionable(dice, ConstraintType.NUMBERCOLOR))
            this.dice = dice;
    }

    public void setDice(Dice dice, ConstraintType type) throws TileFilledException, ConstraintTypeException {
        if(this.dice != null)
            throw new TileFilledException("A dice is already occupying the tile");
        if(isDicePositionable(dice, type))
            this.dice = dice;
    }

    public Dice getDice() {
        return dice;
    }

    public Dice removeDice() {
        Dice removedDice = dice;
        dice = null;
        return removedDice;
    }

    public boolean isDicePositionable(Dice dice) throws ConstraintTypeException {
        return isDicePositionable(dice, ConstraintType.NUMBERCOLOR);
    }

    public boolean isDicePositionable(Dice dice, ConstraintType type) throws ConstraintTypeException {
        if (this.dice == null) {
            switch (type) {
                case NUMBER:
                    return checkConstraint(dice.getNumberConstraint());
                case COLOR:
                    return checkConstraint(dice.getColorConstraint());
                case NUMBERCOLOR:
                    return (checkConstraint(dice.getNumberConstraint()) && checkConstraint(dice.getColorConstraint()));
                case NONE:
                    return true;
                case ISOLATED:
                    throw new ConstraintTypeException("Invalid constraint type");
            }
        }
        return false;
    }

    public IConstraint getConstraint() {
        return constraint;
    }

    public boolean checkConstraint(IConstraint other) {
        return constraint.matches(other);
    }

}
