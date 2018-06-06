package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.Settings;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IStateFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ITimeOutObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeOutFakeObserver implements IStateFakeObserver {

    public static final String TIME_OUT = "TIMEOUT";

    private static final long TIME = Settings.getPlayerTimeout();

    private GameObserverManager observerManager;
    private final IGame game;

    private Thread timeOutThreadSetupPlayer;
    private Thread timeOutThreadTurnState;

    private ServerCreateMessage serverCreateMessage;

    /**
     * Creates a fake observer for time out of players move: they can happen both in setup player or in turn state.
     *
     * @param observerManager observer manager for the specifid game
     * @param game game in which the timeout happens
     */
    public TimeOutFakeObserver(GameObserverManager observerManager, IGame game) {
        this.observerManager = observerManager;
        this.game = game;

        serverCreateMessage = new ServerCreateMessage();
    }

    /**
     * Handle the timeout that can happens during the turn state 
     */
    private void handleTimeoutSetUpPlayer() {
        try {
            synchronized (game) {
                if(timeOutThreadSetupPlayer != null)
                    game.forceStateChange();
            }
        } catch (InvalidActionException e) {
            Logger.getAnonymousLogger().log(Level.INFO, "Impossible because have to happen in this way");
        }
    }

    /**
     * Notifies to all the user that a certain player has timed out while being in turn state.
     * It also acts on the model, by forcing the state change, due to timeout experienced
     *
     * @param turnUser user who timed out
     */
    private void handleTimeoutTurnState(User turnUser) {
        try {
            synchronized (game) {
                if(timeOutThreadTurnState != null) {
                    Runnable notify = () -> observerManager.getObserverTimeoutHashMap().forEach((token, obs) -> {
                        try {
                            obs.onTimeOut(serverCreateMessage.createTurnUserMessage(turnUser).buildMessage());
                        } catch (IOException e) {
                            observerManager.notifyDisconnection(token);
                        }
                    });

                    observerManager.pushThreadInQueue(TIME_OUT, notify);

                    game.forceStateChange();
                }
            }
        } catch (InvalidActionException e) {
            Logger.getAnonymousLogger().log(Level.INFO, "Impossible because have to happen in this way");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame() {
        timeOutThreadSetupPlayer.interrupt();
        timeOutThreadSetupPlayer = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() {
        Runnable timeout = () -> {
            try {
                Thread.sleep(TIME);
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "TimeoutThread interrupted");
                Thread.currentThread().interrupt();
            }
            handleTimeoutSetUpPlayer();
        };

        timeOutThreadSetupPlayer = new Thread(timeout);
        timeOutThreadSetupPlayer.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(int round, User roundUser) {
        // DO NOTHING BECAUSE TIMEOUT DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(int round, int turn, User roundUser, User turnUser) {
        Runnable timeOut = () -> {
            try {
                Thread.sleep(TIME);
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "TimeoutThread interrupted");
                Thread.currentThread().interrupt();
            }
            handleTimeoutTurnState(turnUser);
        };

        timeOutThreadTurnState = new Thread(timeOut);
        timeOutThreadTurnState.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(int round, User roundUser) {
        // DO NOTHING BECAUSE TIMEOUT DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(User roundUser) {
        // DO NOTHING BECAUSE TIMEOUT DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) {
        // DO NOTHING BECAUSE TIMEOUT DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(User turnUser) {
        // DO NOTHING BECAUSE TIMEOUT DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(User turnUser) {
        // DO NOTHING BECAUSE TIMEOUT DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(User turnUser) {
        timeOutThreadTurnState.interrupt();
        timeOutThreadTurnState = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints) {
        // DO NOTHING BECAUSE TIMEOUT DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(User winner) {
        // DO NOTHING BECAUSE TIMEOUT DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }
}
