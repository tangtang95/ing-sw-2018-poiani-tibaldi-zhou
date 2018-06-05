package org.poianitibaldizhou.sagrada.game.model.state;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import java.rmi.RemoteException;

public class RoundStartState extends IStateGame implements ICurrentRoundPlayer {

    private Player currentRoundPlayer;
    private int currentRound;

    /**
     * Constructor.
     * Create the state of RoundStartGame
     *
     * @param game               the game to consider
     * @param currentRound       the current round of the game
     * @param currentRoundPlayer the player who is the first player of the round
     */
    public RoundStartState(Game game, int currentRound, Player currentRoundPlayer) {
        super(game);
        this.currentRoundPlayer = currentRoundPlayer;
        this.currentRound = currentRound;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        throwDices();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Inject the number of dices based on the numberOfPlayers or the type of game (single player or multi player) and
     * set the state of the game to the TurnState
     */
    @Override
    public void throwDices() {
        game.addDicesToDraftPoolFromDiceBag();
        game.setState(new TurnState(game, currentRound, currentRoundPlayer, currentRoundPlayer, true));
    }

    @Contract(pure = true)
    public Player getCurrentRoundPlayer() {
        return currentRoundPlayer;
    }

}
