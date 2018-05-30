package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ToolCardExecutorFakeObserver implements IToolCardExecutorFakeObserver {

    private String token;
    private ObserverManager observerManager;
    private IToolCardExecutorObserver realObserver;

    public ToolCardExecutorFakeObserver(String token, ObserverManager observerManager, IToolCardExecutorObserver observer) {
        if (observer instanceof ToolCardExecutorFakeObserver)
            throw new IllegalArgumentException();
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = observer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDice(List<Dice> diceList) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedDice(JSONArray.toJSONString(diceList));
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
                realObserver.notifyNeedColor(JSONArray.toJSONString(Collections.singletonList(colors)));
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
                realObserver.notifyNeedNewDeltaForDice(String.valueOf(diceValue), String.valueOf(value));
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
                realObserver.notifyNeedDiceFromRoundTrack(roundTrack.toJSON().toJSONString());
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
                realObserver.notifyNeedDicePositionOfCertainColor(color.toString());
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
                realObserver.notifyCommandInterrupted(error.toString());
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
