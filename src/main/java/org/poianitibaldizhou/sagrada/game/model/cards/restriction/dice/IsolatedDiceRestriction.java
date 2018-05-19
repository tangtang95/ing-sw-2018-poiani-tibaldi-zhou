package org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice;

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
