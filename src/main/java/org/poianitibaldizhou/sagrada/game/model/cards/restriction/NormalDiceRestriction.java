package org.poianitibaldizhou.sagrada.game.model.cards.restriction;

public class NormalDiceRestriction implements IDiceRestriction {
    @Override
    public boolean isCorrectNumberOfAdjacentDices(int numberOfAdjacentDices) {
        return numberOfAdjacentDices > 0;
    }
}
