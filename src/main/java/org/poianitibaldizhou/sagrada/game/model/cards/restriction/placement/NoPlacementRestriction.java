package org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;

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
