package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

import java.util.ArrayList;
import java.util.List;

public class ToolCardExecutor {
    private final Object diceMonitor;
    private final Object colorMonitor;
    private final Object valueMonitor;
    private final Object turnEndMonitor;
    private final Object positionMonitor;

    private Dice neededDice;
    private Color neededColor;
    private Integer neededValue;
    private Position neededPosition;
    private boolean turnEnd;
    private List<IToolCardExecutorObserver> observers;

    private List<ICommand> commands;
    private boolean isDone;

    /**
     * Constructor.
     * Creates an executor helper for the invocation of the various commands.
     *
     * @param commands list of commands of the toolCard invoked
     */
    public ToolCardExecutor(List<ICommand> commands) {
        diceMonitor = new Object();
        colorMonitor = new Object();
        valueMonitor = new Object();
        turnEndMonitor = new Object();
        positionMonitor = new Object();

        neededDice = null;
        neededValue = null;
        neededColor = null;
        neededPosition = null;
        turnEnd = false;
        isDone = false;
        this.observers = new ArrayList<>();
        this.commands = commands;

    }

    public void addObserver(IToolCardExecutorObserver observer){
        this.observers.add(observer);
    }

    public List<IToolCardExecutorObserver> getObservers() {
        return observers;
    }

    /**
     * copy-constructor
     *
     * @param toolCardExecutor the toolCard Executor to copy
     */
    private ToolCardExecutor(ToolCardExecutor toolCardExecutor){
        diceMonitor = new Object();
        colorMonitor = new Object();
        valueMonitor = new Object();
        turnEndMonitor = new Object();
        positionMonitor = new Object();

    }



    public void invokeCommands(Player player, Game game){
        CommandFlow commandFlow = CommandFlow.MAIN;
        // TODO Riccardo Tree
    }

    public void setNeededValue(Integer neededValue) {
        synchronized (valueMonitor) {
            this.neededValue = neededValue;
            valueMonitor.notifyAll();
        }
    }

    public int getNeededValue() throws InterruptedException {
        synchronized (valueMonitor) {
            while (neededValue == null)
                valueMonitor.wait();
            return neededValue;
        }
    }

    public Dice getNeededDice() throws InterruptedException {
        synchronized (diceMonitor) {
            while (neededDice == null)
                diceMonitor.wait();
            return neededDice;
        }
    }

    public void setNeededDice(Dice neededDice) {
        synchronized (diceMonitor) {
            this.neededDice = neededDice;
            diceMonitor.notifyAll();
        }
    }

    public Color getNeededColor() throws InterruptedException {
        synchronized (colorMonitor) {
            while (neededColor == null)
                colorMonitor.wait();
            return neededColor;
        }
    }

    public void setNeededColor(Color neededColor) {
        synchronized (colorMonitor) {
            this.neededColor = neededColor;
            colorMonitor.notifyAll();
        }
    }

    public Position getPosition() throws InterruptedException {
        synchronized (positionMonitor) {
            while (neededPosition == null)
                positionMonitor.wait();
            return neededPosition;
        }
    }

    public void setNeededPosition(Position position) {
        synchronized (positionMonitor) {
            this.neededPosition = position;
            positionMonitor.notifyAll();
        }
    }

    public void waitForTurnEnd() throws InterruptedException {
        synchronized (turnEndMonitor) {
            while(!turnEnd)
                turnEndMonitor.wait();
        }
    }

    public synchronized void setTurnEnded(boolean isTurnEnded) {
        synchronized (turnEndMonitor) {
            this.turnEnd = isTurnEnded;
            if (isTurnEnded)
                turnEndMonitor.notifyAll();
        }
    }

    public void waitForToolCardExecutionEnd() throws InterruptedException {
        synchronized (this) {
            while (!isDone)
                this.wait();
        }
    }

    public static ToolCardExecutor newInstance(ToolCardExecutor tceh) {
        if (tceh == null)
            return null;
        return new ToolCardExecutor(tceh);
    }
}
