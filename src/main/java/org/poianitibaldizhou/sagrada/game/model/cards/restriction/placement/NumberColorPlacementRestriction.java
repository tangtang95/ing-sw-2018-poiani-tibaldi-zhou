package org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;

/**
 * OVERVIEW: Represents the number and color  placement restriction on the schema card. This mean
 * that both tile color constraint and tile number constraint must be respected.
 */
public class NumberColorPlacementRestriction implements IPlacementRestriction {

    /**
     * This restriction check the colorConstraint and numberConstraint of the tile and dice
     *
     * @param tile the tile on which the dice is going to be placed
     * @param dice the dice to be placed
     * @return true if the dice is positionable based on this restriction, false otherwise
     */
    @Override
    public boolean isPositionable(Tile tile, Dice dice) {
        return tile.checkConstraint(dice.getNumberConstraint()) && tile.checkConstraint(dice.getColorConstraint());
    }
}
