package org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement;

import org.jetbrains.annotations.Contract;

/**
 * OVERVIEW: Represents the enum of the available placement restriction of a dice on the schema card. This type of
 * restriction regards the color and number that the tile requires.
 */
public enum PlacementRestrictionType {
    NUMBER(new NumberPlacementRestriction()),
    COLOR(new ColorPlacementRestriction()),
    NUMBER_COLOR(new NumberColorPlacementRestriction()),
    NONE(new NoPlacementRestriction());

    private IPlacementRestriction placementRestriction;

    /**
     * Constructor
     * @param placementRestriction placement restriction associated with the enum
     */
    PlacementRestrictionType(IPlacementRestriction placementRestriction){
        this.placementRestriction = placementRestriction;
    }

    /**
     * Returns the placement restriction associated with the enum
     *
     * @return placement restriction associated with the enum 
     */
    @Contract(pure = true)
    public IPlacementRestriction getPlacementRestriction(){
        return placementRestriction;
    }
}
