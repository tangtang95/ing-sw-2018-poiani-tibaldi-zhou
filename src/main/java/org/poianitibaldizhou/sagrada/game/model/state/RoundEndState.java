package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Direction;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;

public class RoundEndState extends IStateGame {

    private Player currentPlayer;

    /**
     * Constructor.
     * Create a RoundEndState: when the current round is ending there are some operations to do written in the
     * nextRound method
     *
     * @param game the game needed for changing state and other things to do
     */
     RoundEndState(Game game, Player currentPlayer) {
        super(game);
        this.currentPlayer = currentPlayer;
    }

    /**
     * Add all the remaining dices of the DraftPool to the RoundTrack and clear all dices of DraftPool;
     * Then if the current round is less than the numberOfRounds the game will set a new RoundStartState, otherwise
     * it will set a EndGameState
     *
     */
    @Override
    public void nextRound() {
        game.getRoundTrack().addDicesToCurrentRound(game.getDraftPool().getDices());
        game.getDraftPool().clearPool();
        game.getRoundTrack().nextRound();
        Player nextPlayer = game.getPlayers().get(
                game.getNextIndexOfPlayer(currentPlayer, Direction.CLOCKWISE));

        if(game.getCurrentRound() < game.getNumberOfRounds())
            game.setState(new RoundStartState(game, nextPlayer));
        else
            game.setState(new EndGameState(game, currentPlayer));
    }

}
