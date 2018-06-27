package org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;

/**
 * OVERVIEW: This type of constraint represents the fact that the tile constraint must be
 * ignored when placing a dice on it.
 */
public class NoPlacementRestriction implements IPlacementRestriction {

    /**
     * This doesn't define any restriction of the placement
     *
     * @param tile the tile on which the dice is going to be placed
     * @param dice the dice to be placed
     * @return always true
     */
    @Override
    public boolean isPositionable(Tile tile, Dice dice) {
        return true;
    }
}
