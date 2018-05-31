package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IToolCardFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;

import java.io.IOException;

public class ToolCardFakeObserver implements IToolCardFakeObserver{
    private String token;
    private IToolCardObserver realObserver;
    private ObserverManager observerManager;

    /**
     * Creates a fake observer of the tool card used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real tool card observer
     * @param observerManager observer manager of the specified game
     */
    public ToolCardFakeObserver(String token, ObserverManager observerManager, IToolCardObserver realObserver) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTokenChange(int tokens)  {
        Runnable runnable = () -> {
            try {
                realObserver.onTokenChange(String.valueOf(tokens));
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCardDestroy()  {
        Runnable runnable = () -> {
            try {
                realObserver.onCardDestroy();
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }
}
