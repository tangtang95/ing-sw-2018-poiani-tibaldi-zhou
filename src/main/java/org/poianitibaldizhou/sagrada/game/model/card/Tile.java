package org.poianitibaldizhou.sagrada.game.model.card;

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

    public void setDice(Dice dice) throws TileFilledException {
        if(dice != null)
            throw new TileFilledException();
        this.dice = dice;
    }

    public Dice getDice() {
        return dice;
    }

    public Dice removeDice() {
        Dice clone = dice;
        dice = null;
        return clone;
    }

    public IConstraint getConstraint() {
        return constraint;
    }

    public boolean checkConstraint(IConstraint other){
        return constraint.matches(other);
    }

}
