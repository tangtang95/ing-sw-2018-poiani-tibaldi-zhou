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

    public DraftPoolFakeObserver(String token, IDraftPoolObserver realObserver, ObserverManager observerManager) {
        if(realObserver instanceof DraftPoolFakeObserver)
            throw new IllegalArgumentException();
        this.token  = token;
        this.realObserver = realObserver;
        this.observerManager = observerManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(Dice dice) {
        try {
            realObserver.onDiceAdd(dice);
        } catch(IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(Dice dice) {
        try {
            realObserver.onDiceRemove(dice);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(List<Dice> dices) {
        try {
            realObserver.onDicesAdd(dices);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolReroll(List<Dice> dices) {
        try {
            realObserver.onDraftPoolReroll(dices);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolClear() {
        try {
            realObserver.onDraftPoolClear();
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }
}
