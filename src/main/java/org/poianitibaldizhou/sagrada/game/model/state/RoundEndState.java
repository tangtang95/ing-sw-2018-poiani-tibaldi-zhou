package org.poianitibaldizhou.sagrada.game.model.state;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.observers.IStateObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import java.rmi.RemoteException;
import java.util.Map;

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
    RoundEndState(Game game, int currentRound, Player currentRoundPlayer) {
        super(game);
        this.currentRound = currentRound;
        this.currentRoundPlayer = currentRoundPlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        game.getStateObservers().forEach((key, value) -> {
            try {
                value.onRoundEnd(currentRound, currentRoundPlayer.getUser());
            } catch (RemoteException e) {
                game.getStateObservers().remove(key);
            }
        });
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

    @Contract(pure = true)
    public int getCurrentRound() {
        return currentRound;
    }


    @Override
    public Player getCurrentRoundPlayer() {
        return currentRoundPlayer;
    }

}
