package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IDrawableCollectionFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;

import java.io.IOException;
import java.util.List;

public class DrawableCollectionFakeObserver<T extends JSONable> implements IDrawableCollectionFakeObserver<T> {
    private String token;
    private ObserverManager observerManager;
    private IDrawableCollectionObserver realObserver;

    /**
     * Creates a fake observer of a drawable collection used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real drawable collection observer
     * @param observerManager observer manager of the specified game
     */
    public DrawableCollectionFakeObserver(String token, IDrawableCollectionObserver realObserver, ObserverManager observerManager) {
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
                realObserver.onElementAdd(elem.toJSON().toJSONString());
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
                StringBuilder json = new StringBuilder();
                elemList.forEach(elem -> json.append(elem.toJSON().toJSONString()));
                realObserver.onElementsAdd(json.toString());
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
                realObserver.onElementDraw(elem.toJSON().toJSONString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
