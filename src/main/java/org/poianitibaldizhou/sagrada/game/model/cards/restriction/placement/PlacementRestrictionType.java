package org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public enum PlacementRestrictionType {
    NUMBER(new NumberPlacementRestriction()),
    COLOR(new ColorPlacementRestriction()),
    NUMBER_COLOR(new NumberColorPlacementRestriction()),
    NONE(new NoPlacementRestriction());

    private IPlacementRestriction placementRestriction;

    PlacementRestrictionType(IPlacementRestriction placementRestriction){
        this.placementRestriction = placementRestriction;
    }

    @Contract(pure = true)
    public IPlacementRestriction getPlacementRestriction(){
        return placementRestriction;
    }
}
