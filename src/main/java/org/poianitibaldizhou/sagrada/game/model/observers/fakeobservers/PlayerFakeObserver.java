package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;

import java.io.IOException;

public class PlayerFakeObserver implements IPlayerObserver {

    private String token;
    private ObserverManager observerManager;
    private IPlayerObserver realObseerver;

    public PlayerFakeObserver(String token, ObserverManager observerManager, IPlayerObserver observer) {
        if(observer instanceof PlayerFakeObserver)
            throw new IllegalArgumentException();
        this.token = token;
        this.observerManager = observerManager;
        this.realObseerver = observer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(int value)  {
        try {
            realObseerver.onFavorTokenChange(value);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetOutcome(Outcome outcome)  {
        try {
            realObseerver.onSetOutcome(outcome);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }
}
