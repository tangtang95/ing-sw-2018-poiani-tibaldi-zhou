package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IPlayerFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.network.protocol.JSONServerProtocol;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;

public class PlayerFakeObserver implements IPlayerFakeObserver {

    private String token;
    private GameObserverManager observerManager;
    private IPlayerObserver realObserver;

    private ServerCreateMessage serverCreateMessage;

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

        serverCreateMessage = new ServerCreateMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(int value)  {
        Runnable runnable = () -> {
            try {
                realObserver.onFavorTokenChange(serverCreateMessage.createMessageValue(value).buildMessage());
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
                realObserver.onSetOutcome(serverCreateMessage.createOutcomeMessage(outcome).buildMessage());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
