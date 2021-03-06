package org.poianitibaldizhou.sagrada.network.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;

/**
 * @see IToolCardFakeObserver
 */
public class ToolCardFakeObserver implements IToolCardFakeObserver{
    private String token;
    private IToolCardObserver realObserver;
    private GameObserverManager observerManager;

    /**
     * Creates a fake observer of the tool card used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real tool card observer
     * @param observerManager observer manager of the specified game
     */
    public ToolCardFakeObserver(String token, GameObserverManager observerManager, IToolCardObserver realObserver) {
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
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onTokenChange(serverCreateMessage.createMessageValue(tokens).buildMessage());
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
