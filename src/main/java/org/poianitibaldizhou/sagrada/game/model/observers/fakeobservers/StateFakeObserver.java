package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.util.Map;

public class StateFakeObserver implements IStateObserver {

    private String token;
    private ObserverManager observerManager;
    private IStateObserver realObserver;

    public StateFakeObserver(String token, ObserverManager observerManager, IStateObserver observer) {
        if (observer instanceof StateFakeObserver)
            throw new IllegalArgumentException();
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = observer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame() {
        try {
            realObserver.onSetupGame();
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() {
        try {
            realObserver.onSetupPlayer();
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(int round, User roundUser) {
        try {
            realObserver.onRoundStart(round, roundUser);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) {
        try {
            realObserver.onTurnState(round, isFirstTurn, roundUser, turnUser);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(int round, User roundUser) {
        try {
            realObserver.onRoundEnd(round, roundUser);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(User roundUser) {
        try {
            realObserver.onEndGame(roundUser);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) {
        try {
            realObserver.onSkipTurnState(round, isFirstTurn, roundUser, turnUser);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(User turnUser) {
        try {
            realObserver.onPlaceDiceState(turnUser);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(User turnUser) {
        try {
            realObserver.onUseCardState(turnUser);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(User turnUser) {
        try {
            realObserver.onEndTurnState(turnUser);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints) {
        try {
            realObserver.onVictoryPointsCalculated(victoryPoints);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(User winner) {
        try {
            realObserver.onResultGame(winner);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }
}
