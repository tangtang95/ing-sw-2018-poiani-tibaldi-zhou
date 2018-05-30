package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IRoundTrackFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IRoundTrackObserver;

import java.io.IOException;
import java.util.List;

public class RoundTrackFakeObserver implements IRoundTrackFakeObserver{

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
                realObserver.onDicesAddToRound(JSONArray.toJSONString(diceList), String.valueOf(round));
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
    public void onDiceAddToRound(Dice dice, int round) {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceAddToRound(dice.toJSON().toJSONString(), String.valueOf(round));
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
    public void onDiceRemoveFromRound(Dice dice, int round) {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceRemoveFromRound(dice.toJSON().toJSONString(), String.valueOf(round));
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
    public void onDiceSwap(Dice oldDice, Dice newDice, int round) {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceSwap(oldDice.toJSON().toJSONString(), newDice.toJSON().toJSONString(), String.valueOf(round));
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
