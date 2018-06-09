package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.ServerSettings;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IStateFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeOutFakeObserver implements IStateFakeObserver {

    private static final long TIME = ServerSettings.getPlayerTimeout();

    private GameObserverManager observerManager;
    private final IGame game;

    private Thread timeOutThreadSetupPlayer;
    private Thread timeOutThreadTurnState;

    private ServerCreateMessage serverCreateMessage;

    /**
     * Creates a fake observer for time out of players move: they can happen both in setup player or in turn state.
     *
     * @param observerManager observer manager for the specific game
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
                if(timeOutThreadSetupPlayer != null) {
                    List<User> timedOutUsers = game.getTimedOutUsers();
                    timedOutUsers.forEach(this::createAndPushNotify);
                    game.forceStateChange();
                }
            }
        } catch (InvalidActionException e) {
            Logger.getAnonymousLogger().log(Level.INFO, "Impossible because have to happen in this way");
        }
    }

    private void createAndPushNotify(User timedOutPlayer) {
        observerManager.getObserverTimeoutHashMap().forEach((token, obs) -> {
            Runnable notify = () -> {
                try {
                    obs.onTimeOut(serverCreateMessage.createUserMessage(timedOutPlayer).buildMessage());
                } catch (IOException e) {
                    observerManager.notifyDisconnection(token);
                }
            };
            System.out.println("Notify timeout of " + timedOutPlayer.getName() + " to " + token);
            observerManager.pushTimeoutThread(token, notify, timedOutPlayer.getToken());
        });
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
                    createAndPushNotify(turnUser);
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
        System.out.println("Ending timeout turn player");
        timeOutThreadSetupPlayer.interrupt();
        timeOutThreadSetupPlayer = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() {
        System.out.println("Starting timeout player setup");
        Runnable timeout = () -> {
            try {
                Thread.sleep(TIME);
                handleTimeoutSetUpPlayer();
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "TimeoutThread interrupted");
                Thread.currentThread().interrupt();
            }
        };

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
        /*
        System.out.println("Starting time out turn state");
        Runnable timeOut = () -> {
            try {
                Thread.sleep(TIME);
                handleTimeoutTurnState(turnUser);
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "TimeoutThread interrupted");
                Thread.currentThread().interrupt();
            }
        };

        timeOutThreadTurnState = new Thread(timeOut);
        timeOutThreadTurnState.start();
        */
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
        /*
        System.out.println("Ending timeout turn state");
        timeOutThreadTurnState.interrupt();
        timeOutThreadTurnState = null;
        */
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
        // DO NOTHING BECAUSE TIMEOUT_KEY DOESN'T TRIGGER ON OBSERVING THIS METHOD
    }
}
