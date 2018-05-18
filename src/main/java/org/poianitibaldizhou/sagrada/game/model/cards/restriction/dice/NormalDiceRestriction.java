package org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice;

public class NormalDiceRestriction implements IDiceRestriction {

    /**
     * This restriction allows a dice to be placed on a tile where there is at least one adjacent
     * (diagonally and orthogonally) dice
     *
     @param numberOfAdjacentDices number of adjacent (diagonally and orthogonally) dice
      * @return true if based on the number of adjacent dices this is a correct rule, false otherwise
     */
    @Override
    public boolean isCorrectNumberOfAdjacentDices(int numberOfAdjacentDices) {
        return numberOfAdjacentDices > 0;
    }
}
