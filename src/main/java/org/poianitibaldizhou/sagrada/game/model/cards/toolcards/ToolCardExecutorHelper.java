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

    private ToolCardExecutorHelper(Dice neededDice, Color neededColor, Integer neededValue, Position neededPosition,
                                   boolean turnEnd, List<IToolCardObserver> observers){
        this.neededDice = Dice.newInstance(neededDice);
        this.neededColor = neededColor;
        this.neededValue = neededValue;
        this.neededPosition = neededPosition;
        this.turnEnd = turnEnd;
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

    public static ToolCardExecutorHelper newInstance(ToolCardExecutorHelper tceh) {
        if (tceh == null)
            return null;
        try {
            return new ToolCardExecutorHelper(tceh.getNeededDice(),tceh.getNeededColor(),tceh.getNeededValue(),
                    tceh.getPosition(),tceh.getTurnEnded(),tceh.getObservers());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
