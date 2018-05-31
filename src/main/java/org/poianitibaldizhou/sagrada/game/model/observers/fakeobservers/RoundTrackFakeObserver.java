package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IRoundTrackFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ServerNetworkProtocol;

import java.io.IOException;
import java.util.List;

public class RoundTrackFakeObserver implements IRoundTrackFakeObserver{

    private IRoundTrackObserver realObserver;
    private String token;
    private ObserverManager observerManager;

    private ServerNetworkProtocol serverNetworkProtocol;

    /**
     * Creates a fake observer of the round track used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real round track observer
     * @param observerManager observer manager of the specified game
     */
    public RoundTrackFakeObserver(String token, IRoundTrackObserver realObserver, ObserverManager observerManager) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;

        serverNetworkProtocol = new ServerNetworkProtocol();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAddToRound(List<Dice> diceList, int round) {
        Runnable runnable = () -> {
            try {
                realObserver.onDicesAddToRound(serverNetworkProtocol.createMessage(diceList, round));
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
                realObserver.onDiceAddToRound(serverNetworkProtocol.createMessage(dice, round));
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
                realObserver.onDiceRemoveFromRound(serverNetworkProtocol.createMessage(dice, round));
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
                realObserver.onDiceSwap(serverNetworkProtocol.createMessage(oldDice, newDice, round));
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
