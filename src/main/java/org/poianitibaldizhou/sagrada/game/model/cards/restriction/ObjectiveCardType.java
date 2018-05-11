package org.poianitibaldizhou.sagrada.game.model.cards.restriction;

import org.jetbrains.annotations.Contract;

public enum ObjectiveCardType {
    NUMBER(new NumberPlacementRestriction()),
    COLOR(new ColorPlacementRestriction()),
    NUMBER_COLOR(new NumberColorPlacementRestriction()),
    NONE(new NoPlacementRestriction());

    private IPlacementRestriction placementRestriction;

    ObjectiveCardType(IPlacementRestriction placementRestriction){
        this.placementRestriction = placementRestriction;
    }

    @Contract(pure = true)
    public IPlacementRestriction getPlacementRestriction(){
        return placementRestriction;
    }
}
