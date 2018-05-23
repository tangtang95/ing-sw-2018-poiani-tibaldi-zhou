package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.Game;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class ResetState extends IStateGame {

    private Set<String> playersReady;

    public ResetState(Game game) {
        super(game);
        playersReady = new HashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // Nothing to notify
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
    public void readyGame(String token) throws InvalidActionException, RemoteException {
        if (playersReady.contains(token))
            throw new InvalidActionException();
        playersReady.add(token);
        if (playersReady.size() == game.getNumberOfPlayers())
            game.setState(new SetupPlayerState(game));
    }


}
