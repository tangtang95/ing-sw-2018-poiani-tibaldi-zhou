package org.poianitibaldizhou.sagrada.game.model.cards;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationType;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;

public class Tile{

    private final IConstraint constraint;
    private Dice dice = null;

    /**
     * Constructor: create a tile with no constraint on it
     */
    public Tile(){
        constraint = new NoConstraint();
    }

    /**
     * Constructor: create a tile with the constraint passed.
     * If constraint is null, constraint is set to NoConstraint.
     * @param constraint the constraint of the tile
     */
    public Tile(IConstraint constraint){
        this.constraint = constraint == null ? new NoConstraint(): constraint;
    }

    /**
     * Constructor: create a copy of a tile (deep copy because dice and constraint are immutable)
     * @param tile the tile to copy
     */
    public Tile(Tile tile) {
        dice = tile.dice;
        constraint = tile.constraint;
    }

    /**
     *
     * @param dice the dice to place on
     * @throws RuleViolationException if this.getNeededDice() != null
     */
    public void setDice(Dice dice) throws RuleViolationException {
        setDice(dice, TileConstraintType.NUMBER_COLOR);
    }

    /**
     *
     * @param dice the dice to place on
     * @param type the constraint to check when placing the dice
     * @throws RuleViolationException if this.getNeededDice() != null
     */
    public void setDice(Dice dice, TileConstraintType type) throws RuleViolationException {
        if(this.dice != null)
            throw new RuleViolationException(RuleViolationType.TILE_FILLED);
        if(isDicePositionable(dice, type))
            this.dice = dice;
    }

    /**
     *
     * @return the dice placed on the tile
     */
    @Contract(pure = true)
    public Dice getDice() {
        return dice;
    }

    /**
     *
     * @return the dice removed from the tile
     */
    public Dice removeDice() {
        Dice removedDice = dice;
        dice = null;
        return removedDice;
    }

    /**
     *
     * @param dice the dice to check if positionable
     * @return true if the dice can be placed with number and color constraint
     * @throws RuleViolationException if this.getNeededDice() != null
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice) {
        return isDicePositionable(dice, TileConstraintType.NUMBER_COLOR);
    }

    /**
     *
     * @param dice the dice to check if positionable
     * @param type the constraint to check when placing the dice
     * @return true if the dice can be placed with number and color constraint
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, TileConstraintType type) {
        switch (type){
            case NUMBER:
                return checkConstraint(dice.getNumberConstraint());
            case COLOR:
                return checkConstraint(dice.getColorConstraint());
            case NUMBER_COLOR:
                return (checkConstraint(dice.getNumberConstraint()) && checkConstraint(dice.getColorConstraint()));
            case NONE:
                return true;
        }
        return false;
    }

    /**
     *
     * @return the constraint of the tile
     */
    @Contract(pure = true)
    public IConstraint getConstraint() {
        return constraint;
    }

    /**
     *
     * @param other another constraint to check
     * @return true if the constraint of the tile matches with the other
     */
    @Contract(pure = true)
    public boolean checkConstraint(IConstraint other) {
        return constraint.matches(other);
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Tile))
            return false;
        Tile other = (Tile) obj;
        return this.getDice().equals(other.getDice()) && this.getConstraint().equals(other.getConstraint());
    }

}
