package org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice;

/**
 * OVERVIEW: Represents the restriction of placing a dice on the schema card with a certain number of adjacent dices.
 */
public interface IDiceRestriction{

    /**
     * Checks if the number of adjacent dices is correct, according to the concrete implementation of this restriction
     * interface.
     *
     * @param numberOfAdjacentDices number of adjacent dices that must be verified and checked
     * @return true if adjacent dices is the correct number for the concrete implementation of this interface
     */
    boolean isCorrectNumberOfAdjacentDices(int numberOfAdjacentDices);
}
