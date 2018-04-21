package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;

public class TurnState extends IStateGame{
    protected TurnState(Game game) {
        super(game);
    }

    @Override
    public void nextRound() {
        super.nextRound();
    }

    @Override
    public void throwDices() {
        super.throwDices();
    }

    @Override
    public void nextTurn() {
        super.nextTurn();
    }
}
