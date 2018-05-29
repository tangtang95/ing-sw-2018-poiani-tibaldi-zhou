package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;

import java.io.IOException;

public class SchemaCardFakeObserver implements ISchemaCardObserver {

    private String token;
    private ObserverManager observerManager;
    private ISchemaCardObserver realObserver;

    public SchemaCardFakeObserver(String token, ObserverManager observerManager, ISchemaCardObserver observer) {
        if(observer instanceof SchemaCardFakeObserver)
            throw new IllegalArgumentException();
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = observer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDice(Dice dice, Position position)  {
        Runnable runnable = () -> {
            try {
                realObserver.onPlaceDice(dice, position);
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
    public void onDiceRemove(Dice dice, Position position)  {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceRemove(dice, position);
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
