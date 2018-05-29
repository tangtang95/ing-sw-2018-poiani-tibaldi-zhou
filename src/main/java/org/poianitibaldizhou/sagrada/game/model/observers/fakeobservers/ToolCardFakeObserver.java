package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;

import java.io.IOException;

public class ToolCardFakeObserver implements IToolCardObserver {
    private String token;
    private IToolCardObserver realObserver;
    private ObserverManager observerManager;

    public ToolCardFakeObserver(String token, ObserverManager observerManager, IToolCardObserver observer) {
        if(observer instanceof ToolCardFakeObserver)
            throw new IllegalArgumentException();
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = observer;
    }

    @Override
    public void onTokenChange(int tokens)  {
        try {
            realObserver.onTokenChange(tokens);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    @Override
    public void onCardDestroy()  {
        try {
            realObserver.onCardDestroy();
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }
}
