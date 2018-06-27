package org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice;

/**
 * OVERVIEW: Represents the isolated dice restriction during the placement phase of a dice on the schema card.
 * This means the the dice must be placed with the restriction of 0 adjacent dices.
 */
public class IsolatedDiceRestriction implements IDiceRestriction {

    /**
     * This restriction allows a dice to be placed on a tile where there is no dice near
     *
     * @param numberOfAdjacentDices number of adjacent (diagonally and orthogonally) dice
     * @return true if based on the number of adjacent dices this is a correct rule, false otherwise
     */
    @Override
    public boolean isCorrectNumberOfAdjacentDices(int numberOfAdjacentDices) {
        return numberOfAdjacentDices == 0;
    }
}
