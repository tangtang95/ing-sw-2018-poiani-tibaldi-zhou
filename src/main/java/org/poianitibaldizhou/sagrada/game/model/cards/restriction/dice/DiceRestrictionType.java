package org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice;

import org.jetbrains.annotations.Contract;

public enum DiceRestrictionType {
    NORMAL(new NormalDiceRestriction()),
    ISOLATED(new IsolatedDiceRestriction());

    private IDiceRestriction diceRestriction;

    DiceRestrictionType(IDiceRestriction diceRestriction){
        this.diceRestriction = diceRestriction;
    }

    @Contract(pure = true)
    public IDiceRestriction getDiceRestriction(){
        return diceRestriction;
    }
}
