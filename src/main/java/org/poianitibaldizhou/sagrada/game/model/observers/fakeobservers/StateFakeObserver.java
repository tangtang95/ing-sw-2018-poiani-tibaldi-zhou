package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IStateFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.protocol.ServerNetworkProtocol;

import java.io.IOException;
import java.util.Map;

public class StateFakeObserver implements IStateFakeObserver {

    private String token;
    private GameObserverManager observerManager;
    private IStateObserver realObserver;

    private ServerNetworkProtocol serverNetworkProtocol;

    /**
     * Creates a fake observer of the state used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real state observer
     * @param observerManager observer manager of the specified game
     */
    public StateFakeObserver(String token, GameObserverManager observerManager, IStateObserver realObserver) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;

        serverNetworkProtocol = new ServerNetworkProtocol();
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
                realObserver.onRoundStart(serverNetworkProtocol.createMessage(round, roundUser));
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
                realObserver.onTurnState(serverNetworkProtocol.createMessage(round, isFirstTurn, round, turnUser));
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
                realObserver.onRoundEnd(serverNetworkProtocol.createMessage(round, roundUser));
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
                realObserver.onEndGame(serverNetworkProtocol.createMessage(roundUser));
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
                realObserver.onSkipTurnState(serverNetworkProtocol.createMessage(round, isFirstTurn, roundUser, turnUser));
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
                realObserver.onPlaceDiceState(serverNetworkProtocol.createMessage(turnUser));
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
                realObserver.onUseCardState(serverNetworkProtocol.createMessage(turnUser));
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
                realObserver.onEndTurnState(serverNetworkProtocol.createMessage(turnUser));
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
                realObserver.onVictoryPointsCalculated(serverNetworkProtocol.createMessage(victoryPoints));
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
                realObserver.onResultGame(serverNetworkProtocol.createMessage(winner));
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
