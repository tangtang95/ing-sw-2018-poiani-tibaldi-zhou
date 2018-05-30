package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IStateFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.util.Map;

public class StateFakeObserver implements IStateFakeObserver {

    private String token;
    private ObserverManager observerManager;
    private IStateObserver realObserver;

    /**
     * Creates a fake observer of the state used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real state observer
     * @param observerManager observer manager of the specified game
     */
    public StateFakeObserver(String token, ObserverManager observerManager, IStateObserver realObserver) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame() {
        Runnable runnable = () -> {
            try {
                realObserver.onSetupGame();
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() {
        Runnable runnable = () -> {
            try {
                realObserver.onSetupPlayer();
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(int round, User roundUser) {
        Runnable runnable = () -> {
            try {
                realObserver.onRoundStart(String.valueOf(round), roundUser.toJSON().toJSONString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) {
        Runnable runnable = () -> {
            try {
                realObserver.onTurnState(String.valueOf(round), String.valueOf(isFirstTurn),
                        roundUser.toJSON().toJSONString(), turnUser.toString().toString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(int round, User roundUser) {
        Runnable runnable = () -> {
            try {
                realObserver.onRoundEnd(String.valueOf(round), roundUser.toJSON().toJSONString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(User roundUser) {
        Runnable runnable = () -> {
            try {
                realObserver.onEndGame(roundUser.toJSON().toJSONString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) {
        Runnable runnable = () -> {
            try {
                realObserver.onSkipTurnState(String.valueOf(round), String.valueOf(isFirstTurn),
                        roundUser.toJSON().toJSONString(), turnUser.toJSON().toJSONString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(User turnUser) {
        Runnable runnable = () -> {
            try {
                realObserver.onPlaceDiceState(turnUser.toJSON().toJSONString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(User turnUser) {
        Runnable runnable = () -> {
            try {
                realObserver.onUseCardState(turnUser.toJSON().toJSONString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(User turnUser) {
        Runnable runnable = () -> {
            try {
                realObserver.onEndTurnState(turnUser.toJSON().toJSONString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints) {
        Runnable runnable = () -> {
            try {
                realObserver.onVictoryPointsCalculated(JSONObject.toJSONString(victoryPoints));
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(User winner) {
        Runnable runnable = () -> {
            try {
                realObserver.onResultGame(winner.toJSON().toJSONString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
