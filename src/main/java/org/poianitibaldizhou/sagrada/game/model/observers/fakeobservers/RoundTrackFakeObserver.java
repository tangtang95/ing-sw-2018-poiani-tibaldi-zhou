package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IRoundTrackObserver;

import java.io.IOException;
import java.util.List;

public class RoundTrackFakeObserver implements IRoundTrackObserver{

    private IRoundTrackObserver realObserver;
    private String token;
    private ObserverManager observerManager;

    public RoundTrackFakeObserver(String token, IRoundTrackObserver observer, ObserverManager observerManager) {
        if(observer instanceof RoundTrackFakeObserver)
            throw new IllegalArgumentException();
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = observer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAddToRound(List<Dice> diceList, int round) {
        Runnable runnable = () -> {
            try {
                realObserver.onDicesAddToRound(diceList, round);
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
    public void onDiceAddToRound(Dice dice, int round) {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceAddToRound(dice, round);
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
    public void onDiceRemoveFromRound(Dice dice, int round) {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceRemoveFromRound(dice, round);
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
    public void onDiceSwap(Dice oldDice, Dice newDice, int round) {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceSwap(oldDice, newDice, round);
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        Thread t = new Thread(runnable);
        t.start();

    }
}
