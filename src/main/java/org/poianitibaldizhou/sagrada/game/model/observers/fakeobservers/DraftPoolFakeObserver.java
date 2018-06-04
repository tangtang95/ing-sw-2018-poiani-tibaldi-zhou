package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IDraftPoolFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;
import java.util.List;

public class DraftPoolFakeObserver implements IDraftPoolFakeObserver {
    private IDraftPoolObserver realObserver;
    private String token;
    private GameObserverManager observerManager;

    private ServerCreateMessage jsonServerCreateMessage;

    /**
     * Creates a fake observer of the draft pool used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real draft pool observer
     * @param observerManager observer manager of the specified game
     */
    public DraftPoolFakeObserver(String token, IDraftPoolObserver realObserver, GameObserverManager observerManager) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;

        jsonServerCreateMessage = new ServerCreateMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(Dice dice) {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceAdd();
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
                realObserver.onDiceRemove(serverNetworkProtocol.appendMessage(dice));
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
                realObserver.onDicesAdd(jsonServerCreateMessage.createDiceList(dices).toString());
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
                realObserver.onDraftPoolReroll(serverNetworkProtocol.appendMessage(dices));
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
