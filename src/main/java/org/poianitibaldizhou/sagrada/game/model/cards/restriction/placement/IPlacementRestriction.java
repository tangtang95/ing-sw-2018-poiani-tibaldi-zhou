package org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;

/**
 * OVERVOEW: Represents the placement restriction of a dice on the schema card. This doesn't regard the number of
 * adjacent dices, but the constraint associated with the tile.
 */
public interface IPlacementRestriction {

    /**
     * Checks if dice respects and follows the tile's restrictions.
     *
     * @param tile tile that provides the restrictions
     * @param dice dice that need to be respect tile's constraints.
     * @return true if the dice is positionable on tile, false otherwise
     */
    boolean isPositionable(Tile tile, Dice dice);
}
