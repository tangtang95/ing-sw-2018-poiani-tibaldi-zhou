package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;

public class RoundEndState extends IStateGame {

    protected RoundEndState(Game game) {
        super(game);
    }

    @Override
    public void nextRound() {
        if(game.getNumberOfActualRound() < game.getNumberOfRounds())
            game.setState(new RoundStartState(game));
        else
            game.setState(new EndGameState(game));
    }

}
