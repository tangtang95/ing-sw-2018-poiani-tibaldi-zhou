package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToolCardExecutor extends Thread{
    private final Object diceMonitor;
    private final Object colorMonitor;
    private final Object valueMonitor;
    private final Object turnEndMonitor;
    private final Object positionMonitor;
    private final Object executorMonitor;

    private Dice neededDice;
    private Color neededColor;
    private Integer neededValue;
    private Position neededPosition;
    private boolean turnEnd;
    private List<IToolCardExecutorObserver> observers;

    private Node<ICommand> commandRoot;
    private boolean isDone;
    private Player player;
    private Game game;

    /**
     * Constructor.
     * Creates an executor helper for the invocation of the various commandRoot.
     * @param commands tree of commandRoot of the toolCard invoked
     * @param player
     * @param game
     *
     */
    public ToolCardExecutor(Node<ICommand> commands, Player player, Game game) {
        diceMonitor = new Object();
        colorMonitor = new Object();
        valueMonitor = new Object();
        turnEndMonitor = new Object();
        positionMonitor = new Object();
        executorMonitor = new Object();

        neededDice = null;
        neededValue = null;
        neededColor = null;
        neededPosition = null;
        turnEnd = false;
        isDone = false;
        this.observers = new ArrayList<>();
        this.commandRoot = commands;
        this.player = player;
        this.game = game;
    }

    public void addObserver(IToolCardExecutorObserver observer) {
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
    private ToolCardExecutor(ToolCardExecutor toolCardExecutor) {
        diceMonitor = new Object();
        colorMonitor = new Object();
        valueMonitor = new Object();
        turnEndMonitor = new Object();
        positionMonitor = new Object();
        executorMonitor = new Object();
    }

    @Override
    public void run() {
        try {
            invokeCommands();
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Network error");
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().log(Level.INFO, "Invocation of commands interrupted");
        }
    }

    private void invokeCommands() throws RemoteException, InterruptedException {
        CommandFlow commandFlow;
        Node<ICommand> root = commandRoot;
        do {
            try {
                commandFlow = root.getData().executeCommand(player, this, game);

                if (commandFlow == CommandFlow.MAIN) {
                    root = root.getLeftChild();
                } else {
                    root = root.getRightChild();
                }
            } catch (ExecutionCommandException e) {
                // Basically nothing is needed, the commands just needs to be re-executed
                // TODO notify
            }
        } while(root != null);
        isDone = true;
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
            while (!turnEnd)
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
        synchronized (executorMonitor) {
            while (!isDone)
                executorMonitor.wait();
        }
    }

    public void setIsDone(boolean isDone) {
        synchronized (executorMonitor) {
            this.isDone = isDone;
            if(isDone)
                executorMonitor.notifyAll();
        }
    }

    public void interruptCommandsInvocation(){
        this.interrupt();
    }

    public static ToolCardExecutor newInstance(ToolCardExecutor tceh) {
        if (tceh == null)
            return null;
        return new ToolCardExecutor(tceh);
    }


}
