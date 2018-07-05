package org.poianitibaldizhou.sagrada.network.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IStateFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @see IStateFakeObserver
 */
public class StateFakeObserver implements IStateFakeObserver {

    private String token;
    private GameObserverManager observerManager;
    private IStateObserver realObserver;

    /**
     * Creates a fake observer of the state used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token           player's token of the real observer
     * @param realObserver    real state observer
     * @param observerManager observer manager of the specified game
     */
    public StateFakeObserver(String token, GameObserverManager observerManager, IStateObserver realObserver) {
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
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onRoundStart(serverCreateMessage.createRoundUserMessage(roundUser).createMessageValue(round).buildMessage());
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
    public void onTurnState(int round, int turn, User roundUser, User turnUser) {
        if (!observerManager.getDisconnectedPlayer().contains(turnUser.getToken())) {
            Runnable runnable = () -> {
                try {
                    ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                    realObserver.onTurnState(serverCreateMessage.createMessageValue(round).createTurnValueMessage(turn).
                            createRoundUserMessage(roundUser).createTurnUserMessage(turnUser).buildMessage());
                } catch (IOException e) {
                    observerManager.signalDisconnection(token);
                }
            };

            observerManager.pushThreadInQueue(token, runnable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(int round, User roundUser) {
        Runnable runnable = () -> {
            try {
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onRoundEnd(serverCreateMessage.createRoundUserMessage(roundUser).createMessageValue(round).buildMessage());
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
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onEndGame(serverCreateMessage.createRoundUserMessage(roundUser).buildMessage());
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
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onSkipTurnState(serverCreateMessage.createMessageValue(round).createBooleanMessage(isFirstTurn).
                        createRoundUserMessage(roundUser).createTurnUserMessage(turnUser).buildMessage());
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
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onPlaceDiceState(serverCreateMessage.createTurnUserMessage(turnUser).buildMessage());
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
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onUseCardState(serverCreateMessage.createTurnUserMessage(turnUser).buildMessage());
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
        if (!observerManager.getDisconnectedPlayer().contains(turnUser.getToken())) {
            Runnable runnable = () -> {
                try {
                    ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                    realObserver.onEndTurnState(serverCreateMessage.createTurnUserMessage(turnUser).buildMessage());
                } catch (IOException e) {
                    observerManager.signalDisconnection(token);
                }
            };

            observerManager.pushThreadInQueue(token, runnable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints) {
        Runnable runnable = () -> {
            try {
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                HashMap<User, Integer> map = new HashMap<>();
                victoryPoints.forEach((k, v) -> map.putIfAbsent(k.getUser(), v));
                realObserver.onVictoryPointsCalculated(serverCreateMessage.createVictoryPointMapMessage(map).buildMessage());
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
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onResultGame(serverCreateMessage.createUserMessage(winner).buildMessage());
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
    public void onWaitingForPlayer() {
        // DO NOTHING
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onGameTerminationBeforeStarting() {
        Runnable runnable = () -> {
            try {
                realObserver.onGameTerminationBeforeStarting();
            } catch(IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    @Override
    public void onSelectActionState(User user) {
        Runnable runnable = () -> {
            try {
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onSelectActionState(serverCreateMessage.createTurnUserMessage(user).buildMessage());
            } catch(IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
