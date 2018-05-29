package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;

import java.io.IOException;
import java.util.List;

public class DrawableCollectionFakeObserver<T> implements IDrawableCollectionObserver<T> {
    private String token;
    private ObserverManager observerManager;
    private IDrawableCollectionObserver<T> realObserver;

    public DrawableCollectionFakeObserver(String token, IDrawableCollectionObserver<T> observer, ObserverManager observerManager) {
        if(observer instanceof DrawableCollectionFakeObserver)
            throw new IllegalArgumentException();
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = observer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(T elem)  {
        try {
            realObserver.onElementAdd(elem);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementsAdd(List<T> elemList)  {
        try {
            realObserver.onElementsAdd(elemList);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementDraw(T elem)  {
        try {
            realObserver.onElementDraw(elem);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }
}