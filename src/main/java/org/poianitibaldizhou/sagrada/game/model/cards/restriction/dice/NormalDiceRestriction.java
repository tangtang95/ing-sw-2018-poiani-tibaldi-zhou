package org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice;

public class NormalDiceRestriction implements IDiceRestriction {
    @Override
    public boolean isCorrectNumberOfAdjacentDices(int numberOfAdjacentDices) {
        return numberOfAdjacentDices > 0;
    }
}
