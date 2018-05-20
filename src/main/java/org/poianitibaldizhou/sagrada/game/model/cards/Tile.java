package org.poianitibaldizhou.sagrada.game.model.cards;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationType;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: The constraint of the tile is always != null (if there is no constraint
 * there is an object of NoConstraint)
 */
public class Tile {

    private final IConstraint constraint;
    private Dice dice = null;


    /**
     * Constructor: create a tile with no constraint on it
     */
    public Tile() {
        constraint = new NoConstraint();
    }

    /**
     * Constructor: create a tile with the constraint passed.
     * If constraint is null, constraint is set to NoConstraint.
     *
     * @param constraint the constraint of the tile
     */
    public Tile(IConstraint constraint) {
        this.constraint = constraint == null ? new NoConstraint() : constraint;
    }

    //GETTER
    /**
     * @return the dice placed on the tile
     */
    @Contract(pure = true)
    public Dice getDice() {
        return dice;
    }

    /**
     * @param dice the dice to check if positionable
     * @return true if the dice can be placed with number and color constraint
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice) {
        return isDicePositionable(dice, PlacementRestrictionType.NUMBER_COLOR);
    }

    /**
     * @param dice the dice to check if positionable
     * @param type the constraint to check when placing the dice
     * @return true if the dice can be placed with number and color constraint
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, PlacementRestrictionType type) {
        return type.getPlacementRestriction().isPositionable(this, dice);
    }

    /**
     * @return the constraint of the tile
     */
    @Contract(pure = true)
    public IConstraint getConstraint() {
        return constraint;
    }

    /**
     * @param other another constraint to check
     * @return true if the constraint of the tile matches with the other
     */
    @Contract(pure = true)
    public boolean checkConstraint(IConstraint other) {
        return constraint.matches(other);
    }

    //MODIFIER
    /**
     * @param dice the dice to place on
     * @throws RuleViolationException if this.getNeededDice() != null
     */
    public void setDice(Dice dice) throws RuleViolationException {
        setDice(dice, PlacementRestrictionType.NUMBER_COLOR);
    }

    /**
     * @param dice            the dice to place on
     * @param restrictionType the constraint to check when placing the dice
     * @throws RuleViolationException if this.getNeededDice() != null
     */
    public void setDice(Dice dice, PlacementRestrictionType restrictionType) throws RuleViolationException {
        if (this.dice != null)
            throw new RuleViolationException(RuleViolationType.TILE_FILLED);
        if (!isDicePositionable(dice, restrictionType))
            throw new RuleViolationException(RuleViolationType.TILE_UNMATCHED);
        this.dice = dice;
    }

    /**
     * @return the dice removed from the tile
     */
    public Dice removeDice() {
        Dice removedDice = dice;
        dice = null;
        return removedDice;
    }

    /**
     * Override of equals
     *
     * @param obj the other object to compare
     * @return true if they have the same dice and the same constraint, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Tile))
            return false;
        Tile other = (Tile) obj;
        if ((getDice() == null && other.getDice() != null) || (getDice() != null && other.getDice() == null))
            return false;
        else if (getDice() == null && other.getDice() == null)
            return this.getConstraint().equals(other.getConstraint());
        return this.getDice().equals(other.getDice()) && this.getConstraint().equals(other.getConstraint());
    }

    /**
     * Hashcode based on dice and constraint (the same tile has the same hashcode)
     *
     * @return the hashcode of this specific tile
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDice(), getConstraint());
    }

    @Override
    public String toString() {
        if (dice != null)
            return dice.toString();
        return " " + constraint.toString() + " ";
    }

    public static Tile newInstance(@NotNull Tile tile) {
        Tile newTile = new Tile(tile.getConstraint());
        Dice oldDice = tile.getDice();
        if (oldDice != null)
            try {
                newTile.setDice(new Dice(oldDice.getNumber(), oldDice.getColor()), PlacementRestrictionType.NONE);
            } catch (RuleViolationException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Shouldn't happen, There is no restriction");
            }
        return newTile;
    }
}
