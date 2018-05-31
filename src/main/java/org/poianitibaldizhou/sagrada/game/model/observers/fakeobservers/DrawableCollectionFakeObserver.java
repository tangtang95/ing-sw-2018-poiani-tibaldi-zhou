package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.apache.maven.settings.Server;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IDrawableCollectionFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ServerNetworkProtocol;

import java.io.IOException;
import java.util.List;

public class DrawableCollectionFakeObserver<T extends JSONable> implements IDrawableCollectionFakeObserver<T> {

    private IDrawableCollectionObserver realObserver;
    private String token;
    private ObserverManager observerManager;

    private ServerNetworkProtocol serverNetworkProtocol;

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

        serverNetworkProtocol = new ServerNetworkProtocol();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(T elem)  {
        Runnable runnable = () -> {
            try {
                realObserver.onElementAdd(serverNetworkProtocol.createMessage(elem));
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
                realObserver.onElementsAdd(serverNetworkProtocol.createMessage(elemList));
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
                realObserver.onElementDraw(serverNetworkProtocol.createMessage(elem));
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
