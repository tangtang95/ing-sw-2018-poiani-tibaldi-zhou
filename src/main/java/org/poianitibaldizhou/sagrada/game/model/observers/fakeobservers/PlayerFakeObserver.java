package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IPlayerFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;

import java.io.IOException;

public class PlayerFakeObserver implements IPlayerFakeObserver {

    private String token;
    private ObserverManager observerManager;
    private IPlayerObserver realObserver;

    /**
     * Creates a fake observer of the player used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real player observer
     * @param observerManager observer manager of the specified game
     */
    public PlayerFakeObserver(String token, ObserverManager observerManager, IPlayerObserver realObserver) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(int value)  {
        Runnable runnable = () -> {
            try {
                realObserver.onFavorTokenChange(String.valueOf(value));
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
    public void onSetOutcome(Outcome outcome)  {
        Runnable runnable = () -> {
            try {
                realObserver.onSetOutcome(outcome.toString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
