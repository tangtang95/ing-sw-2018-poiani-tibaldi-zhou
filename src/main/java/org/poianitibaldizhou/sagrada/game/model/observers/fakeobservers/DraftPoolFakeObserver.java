package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;

import java.io.IOException;
import java.util.List;

public class DraftPoolFakeObserver implements IDraftPoolObserver {
    private IDraftPoolObserver realObserver;
    private String token;
    private ObserverManager observerManager;

    /**
     * Creates a fake observer of the draft pool used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real draft pool observer
     * @param observerManager observer manager of the specified game
     */
    public DraftPoolFakeObserver(String token, IDraftPoolObserver realObserver, ObserverManager observerManager) {
        if (realObserver instanceof DraftPoolFakeObserver)
            throw new IllegalArgumentException();
        this.token = token;
        this.realObserver = realObserver;
        this.observerManager = observerManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(Dice dice) {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceAdd(dice);
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
    public void onDiceRemove(Dice dice) {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceRemove(dice);
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
    public void onDicesAdd(List<Dice> dices) {
        Runnable runnable = () -> {
            try {
                realObserver.onDicesAdd(dices);
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
    public void onDraftPoolReroll(List<Dice> dices) {
        Runnable runnable = () -> {
            try {
                realObserver.onDraftPoolReroll(dices);
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
    public void onDraftPoolClear() {
        Runnable runnable = () -> {
            try {
                realObserver.onDraftPoolClear();
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
