package org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;

public interface IPlacementRestriction {
    boolean isPositionable(Tile tile, Dice dice);
}
