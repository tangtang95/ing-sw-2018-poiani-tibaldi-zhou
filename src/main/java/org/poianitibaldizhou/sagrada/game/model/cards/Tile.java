package org.poianitibaldizhou.sagrada.game.model.cards;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationType;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.ServerNetworkProtocol;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: The constraint of the tile is always != null (if there is no constraint
 * there is an object of NoConstraint)
 */
public class Tile implements Serializable, JSONable{

    private final IConstraint constraint;
    private Dice dice;
    private final transient ServerNetworkProtocol protocol = new ServerNetworkProtocol();

    /**
     * Tile param for network protocol.
     */
    private static final String JSON_CONSTRAINT = "constraint";

    /**
     * Constructor: create a tile with no constraint on it
     */
    public Tile() {
        constraint = new NoConstraint();
        dice = null;
    }

    /**
     * Constructor: create a tile with the constraint passed.
     * If constraint is null, constraint is set to NoConstraint.
     *
     * @param constraint the constraint of the tile
     */
    public Tile(IConstraint constraint) {
        this.constraint = constraint == null ? new NoConstraint() : constraint;
        this.dice = null;
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
        return Objects.hash(Tile.class, getDice(), getConstraint());
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

    /**
     * Convert a tile in a JSONObject.
     *
     * @return a JSONObject.
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject tileJSON =  new JSONObject();
        if(this.getDice() != null)
            tileJSON.put(SharedConstants.DICE, this.getDice().toJSON());
        if (this.getConstraint() instanceof ColorConstraint)
            tileJSON.put(JSON_CONSTRAINT, ((ColorConstraint) this.getConstraint()).getColor().name());
        else if (this.getConstraint() instanceof NumberConstraint)
            tileJSON.put(JSON_CONSTRAINT, ((NumberConstraint) this.getConstraint()).getNumber());
        else
            tileJSON.put(JSON_CONSTRAINT, null);
        main.put(SharedConstants.TYPE, SharedConstants.TILE);
        main.put(SharedConstants.BODY,tileJSON);
        return main;
    }

    /**
     * Convert a json string in a Tile object.
     *
     * @param jsonObject a JSONObject that contains a Tile.
     * @return a tile object or null if the jsonObject is wrong.
     */
    @Override
    public Object toObject(JSONObject jsonObject) {
        Tile tile;
        try {
            if (jsonObject.get(JSON_CONSTRAINT) != null)
                tile = new Tile(new NumberConstraint(Integer.parseInt(jsonObject.get(JSON_CONSTRAINT).toString())));
            else
                tile = new Tile(new NoConstraint());
        } catch (NumberFormatException e) {
            tile = new Tile( new ColorConstraint(Color.valueOf((String) jsonObject.get(JSON_CONSTRAINT))));
        }
        try {
            if (jsonObject.containsKey(SharedConstants.DICE)) {
                Dice readDice = (Dice) protocol.convertToObject(
                        (JSONObject) jsonObject.get(SharedConstants.DICE));
                tile.setDice(readDice);
            }
        } catch (RuleViolationException e) {
            return null;
        }
        return tile;
    }
}
