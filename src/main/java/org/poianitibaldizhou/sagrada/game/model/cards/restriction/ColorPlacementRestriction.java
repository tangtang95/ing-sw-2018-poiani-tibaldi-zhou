package org.poianitibaldizhou.sagrada.game.model.cards.restriction;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;

public class ColorPlacementRestriction implements IPlacementRestriction {
    @Override
    public boolean isPositionable(Tile tile, Dice dice) {
        return tile.checkConstraint(dice.getColorConstraint());
    }
}
