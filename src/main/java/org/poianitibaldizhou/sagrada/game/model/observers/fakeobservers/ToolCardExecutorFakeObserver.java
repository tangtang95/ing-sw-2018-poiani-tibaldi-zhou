package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ToolCardExecutorFakeObserver implements IToolCardExecutorObserver {

    private String token;
    private ObserverManager observerManager;
    private IToolCardExecutorObserver realObserver;

    public ToolCardExecutorFakeObserver(String token, ObserverManager observerManager, IToolCardExecutorObserver observer) {
        if(observer instanceof ToolCardExecutorFakeObserver)
            throw new IllegalArgumentException();
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = observer;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void notifyNeedDice(List<Dice> diceList)  {
        try {
            realObserver.notifyNeedDice(diceList);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewValue()  {
        try {
            realObserver.notifyNeedNewValue();
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedColor(Set<Color> colors)  {
        try {
            realObserver.notifyNeedColor(colors);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewDeltaForDice(int diceValue, int value)  {
        try {
            realObserver.notifyNeedNewDeltaForDice(diceValue, value);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack)  {
        try {
            realObserver.notifyNeedDiceFromRoundTrack(roundTrack);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedPosition()  {
        try {
            realObserver.notifyNeedPosition();
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDicePositionOfCertainColor(Color color)  {
        try {
            realObserver.notifyNeedDicePositionOfCertainColor(color);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyRepeatAction()  {
        try {
            realObserver.notifyRepeatAction();
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyCommandInterrupted(CommandFlow error)  {
        try {
            realObserver.notifyCommandInterrupted(error);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedContinueAnswer()  {
        try {
            realObserver.notifyNeedContinueAnswer();
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }
}
