package org.poianitibaldizhou.sagrada.network.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IDrawableCollectionFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;
import java.util.List;

public class DrawableCollectionFakeObserver<T extends JSONable> implements IDrawableCollectionFakeObserver<T> {

    private IDrawableCollectionObserver realObserver;
    private String token;
    private GameObserverManager observerManager;


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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(T elem)  {
        Runnable runnable = () -> {
            try {
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onElementAdd(serverCreateMessage.createElem(elem).buildMessage());
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
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onElementsAdd(serverCreateMessage.createElemList(elemList).buildMessage());
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
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onElementDraw(serverCreateMessage.createElem(elem).buildMessage());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
