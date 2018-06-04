package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.protocol.JSONServerProtocol;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ToolCardExecutorFakeObserver implements IToolCardExecutorFakeObserver {

    private String token;
    private GameObserverManager observerManager;
    private IToolCardExecutorObserver realObserver;

    private JSONServerProtocol serverNetworkProtocol;

    /**
     * Creates a fake observer of the draft pool used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real draft pool observer
     * @param observerManager observer manager of the specified game
     */
    public ToolCardExecutorFakeObserver(String token, GameObserverManager observerManager, IToolCardExecutorObserver realObserver) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;

        serverNetworkProtocol = new JSONServerProtocol();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDice(List<Dice> diceList) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedDice(serverNetworkProtocol.appendMessage(
                        SharedConstants.DICE_LIST_KEY,
                        diceList
                ));
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
    public void notifyNeedNewValue() {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedNewValue();
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
    public void notifyNeedColor(Set<Color> colors) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedColor(serverNetworkProtocol.appendMessage(colors));
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
    public void notifyNeedNewDeltaForDice(int diceValue, int value) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedNewDeltaForDice(serverNetworkProtocol.appendMessage(diceValue, value));
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
    public void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedDiceFromRoundTrack(serverNetworkProtocol.appendMessage(roundTrack));
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
    public void notifyNeedPosition() {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedPosition();
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
    public void notifyNeedDicePositionOfCertainColor(Color color) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedDicePositionOfCertainColor(serverNetworkProtocol.appendMessage(color));
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
    public void notifyRepeatAction() {
        Runnable runnable = () -> {
            try {
                realObserver.notifyRepeatAction();
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
    public void notifyCommandInterrupted(CommandFlow error) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyCommandInterrupted(serverNetworkProtocol.appendMessage(error));
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
    public void notifyNeedContinueAnswer() {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedContinueAnswer();
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
