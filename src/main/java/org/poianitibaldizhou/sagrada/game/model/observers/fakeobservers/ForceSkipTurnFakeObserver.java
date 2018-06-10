package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IStateFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.Map;

public class ForceSkipTurnFakeObserver implements IStateFakeObserver {

    private GameObserverManager gameObserverManager;

    public ForceSkipTurnFakeObserver(GameObserverManager gameObserverManager) {
        this.gameObserverManager = gameObserverManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame() {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(int round, User roundUser) {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(int round, int turn, User roundUser, User turnUser) {
        if(gameObserverManager.getDisconnectedPlayer().contains(turnUser.getToken())) {
            try {
                gameObserverManager.getGame().forceSkipTurn();
            } catch (InvalidActionException e) {
                throw new IllegalStateException();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(int round, User roundUser) {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(User roundUser) {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(User turnUser) {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(User turnUser) {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(User turnUser) {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints) {
        // DO NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(User winner) {
        // DO NOTHING
    }
}
