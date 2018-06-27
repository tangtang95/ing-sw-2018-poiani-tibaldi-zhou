package org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice;

import org.jetbrains.annotations.Contract;

/**
 * OVERVIEW: This represents the type of restriction of the placement of a certain dice on the schema card. It can be
 * both isolated (this means that the dice must be placed in a position that doesn't have other adjacent dices) or normal
 * (i.e. not isolated). This type of restriction doesn't regard the color or number restrictions of a certain cell.
 */
public enum DiceRestrictionType {
    NORMAL(new NormalDiceRestriction()),
    ISOLATED(new IsolatedDiceRestriction());

    private IDiceRestriction diceRestriction;

    /**
     * Constructor.
     *
     * @param diceRestriction restriction associated with the enum value
     */
    DiceRestrictionType(IDiceRestriction diceRestriction){
        this.diceRestriction = diceRestriction;
    }

    /**
     * Returns the restriction associated with the enum value.
     *
     * @return restriction associated with the enum value
     */
    @Contract(pure = true)
    public IDiceRestriction getDiceRestriction(){
        return diceRestriction;
    }
}
