package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Position;

import java.util.List;

public class ToolCardExecutorHelper {
    private Dice neededDice;
    private Color neededColor;
    private Integer neededValue;
    private Position neededPosition;
    private boolean turnEnd;
    List<IToolCardObserver> observers;

    public ToolCardExecutorHelper(List<IToolCardObserver> observers) {
        neededDice = null;
        neededValue = null;
        neededColor = null;
        neededPosition = null;
        turnEnd = false;
        this.observers = observers;
    }

    public List<IToolCardObserver> getObservers() {
        return observers;
    }

    public synchronized void setNeededValue(Integer neededValue) {
        this.neededValue = neededValue;
        notifyAll();
    }

    public synchronized int getNeededValue() throws InterruptedException {
        while(neededValue == null)
            wait();
        return neededValue;
    }

    public synchronized Dice getNeededDice() throws InterruptedException {
        while(neededDice == null)
            wait();
        return neededDice;
    }

    public synchronized void setNeededDice(Dice neededDice) {
        this.neededDice = neededDice;
        notifyAll();
    }

    public synchronized Color getNeededColor() throws InterruptedException {
        while(neededColor == null)
            wait();
        return neededColor;
    }

    public synchronized void setNeededColor(Color neededColor) {
        this.neededColor = neededColor;
        notifyAll();
    }

    public synchronized Position getPosition() throws InterruptedException {
        while(neededPosition == null)
            wait();
        return neededPosition;
    }

    public synchronized void setNeededPosition(Position position) {
        this.neededPosition = position;
        notifyAll();
    }

    public synchronized boolean getTurnEnded() throws InterruptedException {
        if(turnEnd == false)
            wait();
        return true;
    }

    public synchronized void setTurnEnded(boolean isTurnEnded) {
        this.turnEnd = isTurnEnded;
        if(isTurnEnded)
            notifyAll();
    }
}
