package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IPlayerFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.network.protocol.JSONServerProtocol;

import java.io.IOException;

public class PlayerFakeObserver implements IPlayerFakeObserver {

    private String token;
    private GameObserverManager observerManager;
    private IPlayerObserver realObserver;

    private JSONServerProtocol serverNetworkProtocol;

    /**
     * Creates a fake observer of the player used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real player observer
     * @param observerManager observer manager of the specified game
     */
    public PlayerFakeObserver(String token, GameObserverManager observerManager, IPlayerObserver realObserver) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;

        serverNetworkProtocol = new JSONServerProtocol();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(int value)  {
        Runnable runnable = () -> {
            try {
                realObserver.onFavorTokenChange(serverNetworkProtocol.appendMessage(value));
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
                realObserver.onSetOutcome(serverNetworkProtocol.appendMessage(outcome));
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
