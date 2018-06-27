package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.Direction;
import org.poianitibaldizhou.sagrada.game.model.Game;

/**
 * OVERVIEW: Represents the state in which a certain round has terminated.
 */
public class RoundEndState extends IStateGame implements ICurrentRoundPlayer {

    private Player currentRoundPlayer;
    private int currentRound;

    /**
     * Constructor.
     * Create a RoundEndState: when the current round is ending there are some operations to do written in the
     * nextRound method
     *
     * @param game the game needed for changing state and other things to do
     */
    public RoundEndState(Game game, int currentRound, Player currentRoundPlayer) {
        super(game);
        this.currentRound = currentRound;
        this.currentRoundPlayer = currentRoundPlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        game.getStateObservers().forEach((key, value) -> value.onRoundEnd(currentRound, currentRoundPlayer.getUser()));
        nextRound();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Add all the remaining dices of the DraftPool to the RoundTrack and clear all dices of DraftPool;
     * Then if the current round is less than the numberOfRounds the game will set a new RoundStartState, otherwise
     * it will set a EndGameState
     */
    @Override
    public void nextRound() {
        game.addRemainingDiceToRoundTrack(currentRound);
        game.clearDraftPool();

        Player nextPlayer = game.getNextPlayer(currentRoundPlayer, Direction.CLOCKWISE);
        if (currentRound < RoundTrack.LAST_ROUND)
            game.setState(new RoundStartState(game, currentRound + 1, nextPlayer));
        else
            game.setState(new EndGameState(game, currentRoundPlayer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCurrentRoundPlayer() {
        return currentRoundPlayer;
    }

}
