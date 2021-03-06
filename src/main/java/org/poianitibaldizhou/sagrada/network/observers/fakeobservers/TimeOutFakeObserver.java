package org.poianitibaldizhou.sagrada.network.observers.fakeobservers;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.utilities.ServerSettings;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IStateFakeObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW : observe the state of the game and sets and handles timeout to various actions
 * @see IStateFakeObserver
 */
public class TimeOutFakeObserver implements IStateFakeObserver {

    private static final long TIME = ServerSettings.getPlayerTimeout();
    private static final long TIME_JOIN = ServerSettings.getWaitJoinTimeout();

    private GameObserverManager observerManager;

    private Thread timeOutJoin;
    private Thread timeOutThreadSetupPlayer;
    private Thread timeOutThreadTurnState;
    private Long timeoutStart;

    /**
     * Creates a fake observer for time out of players move: they can happen both in setup player or in turn state.
     *
     * @param observerManager observer manager for the specific game
     */
    public TimeOutFakeObserver(GameObserverManager observerManager) {
        this.observerManager = observerManager;

        timeoutStart = null;
    }

    // GETTER

    /**
     * Returns the time to timeout
     *
     * @return time to timeout in millis
     */
    @Contract(pure = true)
    public long getTimeToTimeout() {
        if ((timeOutThreadSetupPlayer == null && timeOutThreadTurnState == null) || timeoutStart == null)
            throw new IllegalStateException();
        long currTime = System.currentTimeMillis();
        return TIME - (currTime - timeoutStart);
    }


    // INTERFACE METHODS

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame() {
        timeoutStart = null;
        timeOutThreadSetupPlayer.interrupt();
        timeOutThreadSetupPlayer = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() {
        // Stop the timeout for the waiting related to the player join
        timeOutJoin.interrupt();
        timeOutJoin = null;

        // Start the timeout related to the choose of the schema card
        Runnable timeout = () -> {
            try {
                Thread.sleep(TIME);
                handleTimeoutSetUpPlayer();
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, ServerMessage.TIMEOUT_INTERRUPT);
                Thread.currentThread().interrupt();
            }
        };

        timeoutStart = System.currentTimeMillis();
        timeOutThreadSetupPlayer = new Thread(timeout);
        timeOutThreadSetupPlayer.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(int round, User roundUser) {
        // DO NOTHING BECAUSE TIMEOUT_KEY DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(int round, int turn, User roundUser, User turnUser) {
        Runnable timeOut = () -> {
            try {
                Thread.sleep(TIME);
                handleTimeoutTurnState(turnUser);
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, ServerMessage.TIMEOUT_INTERRUPT);
                Thread.currentThread().interrupt();
            }
        };

        timeoutStart = System.currentTimeMillis();
        timeOutThreadTurnState = new Thread(timeOut);
        timeOutThreadTurnState.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(int round, User roundUser) {
        // DO NOTHING BECAUSE TIMEOUT_KEY DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(User roundUser) {
        // DO NOTHING BECAUSE TIMEOUT_KEY DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) {
        // DO NOTHING BECAUSE TIMEOUT_KEY DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(User turnUser) {
        // DO NOTHING BECAUSE TIMEOUT_KEY DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(User turnUser) {
        // DO NOTHING BECAUSE TIMEOUT_KEY DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(User turnUser) {
        timeoutStart = null;
        timeOutThreadTurnState.interrupt();
        timeOutThreadTurnState = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints) {
        // DO NOTHING BECAUSE TIMEOUT_KEY DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(User winner) {
        stopTimeout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onWaitingForPlayer() {
        Runnable runnable = () -> {
            try {
                Thread.sleep(TIME_JOIN);
                handleTimeoutJoin();
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, ServerMessage.TIMEOUT_INTERRUPT);
                Thread.currentThread().interrupt();
            }
        };
        timeOutJoin = new Thread(runnable);
        timeOutJoin.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onGameTerminationBeforeStarting() {
        stopTimeout();
    }

    @Override
    public void onSelectActionState(User user) {
        // DO NOTHING
    }

    // PRIVATE METHODS FOR HANDLING TIMEOUT

    /**
     * Stop all the timeouts
     */
    private void stopTimeout() {
        timeoutStart = null;
        if (timeOutThreadSetupPlayer != null)
            timeOutThreadSetupPlayer.interrupt();
        timeOutThreadSetupPlayer = null;

        if (timeOutThreadTurnState != null)
            timeOutThreadTurnState.interrupt();
        timeOutThreadTurnState = null;

        if(timeOutJoin != null)
            timeOutJoin.interrupt();
        timeOutJoin = null;
    }

    /**
     * Notifies to all the user that a certain player has timed out while being in turn state.
     * It also acts on the model, by forcing the state change, due to timeout experienced
     *
     * @param turnUser user who timed out
     */
    private void handleTimeoutTurnState(User turnUser) {
        try {
            synchronized (observerManager.getGame()) {
                if (timeOutThreadTurnState != null) {
                    createAndPushNotify(turnUser);
                    observerManager.getGame().forceStateChange();
                }
            }
        } catch (InvalidActionException e) {
            Logger.getAnonymousLogger().log(Level.INFO, ServerMessage.INVALID_ACTION);
        }
    }

    /**
     * Handle the timeout that can happens during the turn state
     */
    private void handleTimeoutSetUpPlayer() {
        try {
            synchronized (observerManager.getGame()) {
                if (timeOutThreadSetupPlayer != null) {
                    List<User> timedOutUsers = observerManager.getGame().getTimedOutUsers();
                    timedOutUsers.forEach(this::createAndPushNotify);
                    observerManager.getGame().forceStateChange();
                }
            }
        } catch (InvalidActionException e) {
            Logger.getAnonymousLogger().log(Level.INFO, ServerMessage.INVALID_ACTION);
        }
    }

    /**
     * Handles the timeout of the players joining the game
     */
    private void handleTimeoutJoin() {
        synchronized (observerManager.getGame()) {
            if(timeOutJoin != null) {
                try {
                    observerManager.getGame().forceGameTerminationBeforeStarting();
                } catch (InvalidActionException e) {
                    throw new IllegalStateException();
                }
            }
        }
    }

    /**
     * Creates and push a notify in order to signal the timeout of the player
     * @param timedOutPlayer player who timed out
     */
    private void createAndPushNotify(User timedOutPlayer) {
        observerManager.getObserverTimeoutHashMap().forEach((token, obs) -> {
            Runnable notify = () -> {
                try {
                    ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                    obs.onTimeOut(serverCreateMessage.createUserMessage(timedOutPlayer).buildMessage());
                } catch (IOException e) {
                    observerManager.notifyDisconnection(token);
                }
            };
            observerManager.pushTimeoutThread(token, notify, timedOutPlayer.getToken());
        });
    }

}
