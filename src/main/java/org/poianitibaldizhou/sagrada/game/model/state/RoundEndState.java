package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;

public class RoundEndState extends IStateGame {

    /**
     * Constructor.
     * Create a RoundEndState: when the current round is ending there are some operations to do written in the
     * nextRound method
     *
     * @param game the game needed for changing state and other things to do
     */
     RoundEndState(Game game) {
        super(game);
    }

    /**
     * Add all the remaining dices of the DraftPool to the RoundTrack and clear all dices of DraftPool;
     * Then if the current round is less than the numberOfRounds the game will set a new RoundStartState, otherwise
     * it will set a EndGameState
     *
     */
    @Override
    public void nextRound() {
        game.getRoundTrack().addDicesToRound(game.getDraftPool().getDices(),game.getCurrentRound());
        game.getDraftPool().clearPool();
        game.getRoundTrack().nextRound();
        game.rotateCurrentPlayerRound();

        if(game.getCurrentRound() < game.getNumberOfRounds())
            game.setState(new RoundStartState(game));
        else
            game.setState(new EndGameState(game));
    }

}
