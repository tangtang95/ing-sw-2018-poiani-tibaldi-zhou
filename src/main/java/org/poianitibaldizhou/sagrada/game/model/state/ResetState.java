package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.Game;

import java.util.HashSet;
import java.util.Set;

/**
 * OVERVIEW: Represents the reset state of the game. This can be seen as the initial state of a game.
 */
public class ResetState extends IStateGame {

    private Set<String> playersReady;

    /**
     * Constructor.
     * Creates the reset state for a certain game
     *
     * @param game game that is in this reset state
     */
    public ResetState(Game game) {
        super(game);
        playersReady = new HashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        game.getStateObservers().forEach((key, value) -> value.onWaitingForPlayer());
    }


    /**
     * {@inheritDoc}
     * <p>
     * Add the token of the user to the playersReady and if the size of playersReady is equals to the number
     * of players in the game, the game goes to the next state: SetupPlayerState
     *
     * @param token the token of the user who wants to ready for the game
     * @throws InvalidActionException if the token user has already readied before
     */
    @Override
    public void readyGame(String token) throws InvalidActionException {
        if (playersReady.contains(token))
            throw new InvalidActionException();
        playersReady.add(token);
        if (playersReady.size() == game.getNumberOfPlayers())
            game.setState(new SetupPlayerState(game));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceGameTerminationBeforeStarting() {
        game.getStateObservers().forEach((key, value) -> value.onGameTerminationBeforeStarting());
        game.terminateGame();
    }
}
