package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IDrawableCollectionFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.network.protocol.JSONServerProtocol;

import java.io.IOException;
import java.util.List;

public class DrawableCollectionFakeObserver<T extends JSONable> implements IDrawableCollectionFakeObserver<T> {

    private IDrawableCollectionObserver realObserver;
    private String token;
    private GameObserverManager observerManager;

    private JSONServerProtocol serverNetworkProtocol;

    /**
     * Creates a fake observer of a drawable collection used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real drawable collection observer
     * @param observerManager observer manager of the specified game
     */
    public DrawableCollectionFakeObserver(String token, IDrawableCollectionObserver realObserver, GameObserverManager observerManager) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;

        serverNetworkProtocol = new JSONServerProtocol();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(T elem)  {
        Runnable runnable = () -> {
            try {
                realObserver.onElementAdd(serverNetworkProtocol.appendMessage(elem));
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
    public void onElementsAdd(List<T> elemList)  {
        Runnable runnable = () -> {
            try {
                realObserver.onElementsAdd(serverNetworkProtocol.appendMessage(elemList));
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
    public void onElementDraw(T elem)  {
        Runnable runnable = () -> {
            try {
                realObserver.onElementDraw(serverNetworkProtocol.appendMessage(elem));
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
