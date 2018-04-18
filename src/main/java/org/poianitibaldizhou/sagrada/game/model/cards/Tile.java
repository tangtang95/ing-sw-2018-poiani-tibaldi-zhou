package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.TileFilledException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

public class Tile{

    private IConstraint constraint;
    private Dice dice;

    public Tile(){
        constraint = null;
        dice = null;
    }

    public Tile(IConstraint constraint){
        dice = null;
        this.constraint = constraint;
    }

    public Tile(Tile tile) {
        dice = tile.dice;
        constraint = tile.constraint;
    }

    public void setDice(Dice dice) throws TileFilledException{
        if(this.dice != null)
            throw new TileFilledException("A dice is already occupying the tile");
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

    public boolean isDicePositionable(Dice dice) throws RuleViolationException {
        if(this.dice == null) {
            if (constraint == null)
                return true;
            else if (constraint != null){
                if(checkConstraint(dice.getNumberConstraint()) || checkConstraint(dice.getColorConstraint()))
                    return true;
            }
        }
        throw new RuleViolationException("There is already a dice on that tile");
    }

    public IConstraint getConstraint() {
        return constraint;
    }

    public boolean checkConstraint(IConstraint other) {
        return constraint.matches(other);
    }

}
