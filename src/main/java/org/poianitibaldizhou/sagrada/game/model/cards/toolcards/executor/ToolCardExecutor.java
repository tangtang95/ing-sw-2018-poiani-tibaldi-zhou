package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToolCardExecutor extends Thread {
    // Monitors
    private final Object diceMonitor;
    private final Object colorMonitor;
    private final Object valueMonitor;
    private final Object turnEndMonitor;
    private final Object positionMonitor;
    private final Object executorMonitor;

    // Values monitorized
    private Dice neededDice;
    private Color neededColor;
    private Integer neededValue;
    private Position neededPosition;
    private boolean turnEnd;
    private List<IToolCardExecutorObserver> observers;

    // Executor's attribute
    private Node<ICommand> commandRoot;
    private boolean isExecutingCommands;
    private Player player;
    private DraftPool temporaryDraftpool;
    private DrawableCollection<Dice> temporaryDicebag;
    private RoundTrack temporaryRoundtrack;
    private SchemaCard temporarySchemaCard;
    private Map<Player, Integer> skipTurnPlayers;
    private Game game;
    private TurnState turnState;

    public ToolCardExecutor(Game game, Player player, TurnState turnState) {
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
        isExecutingCommands = false;

        this.observers = new ArrayList<>();
        this.player = player;
        this.turnState = turnState;
        this.game = game;
    }

    public void setCommands(Node<ICommand> commands) {
        this.commandRoot = commands;
    }

    public void addObserver(IToolCardExecutorObserver observer) {
        this.observers.add(observer);
    }

    public List<IToolCardExecutorObserver> getObservers() {
        return observers;
    }

    /**
     * Set all the temporary objects
     */
    private void setTemporaryObjects() {
        this.temporaryDraftpool = game.getDraftPool();
        this.temporaryDicebag = game.getDiceBag();
        this.temporaryRoundtrack = game.getRoundTrack();
        this.temporarySchemaCard = player.getSchemaCard();
        this.skipTurnPlayers = turnState.getSkipTurnPlayers();
    }

    /**
     * Set the modified objects saved before
     */
    private void updateObjects() {
        game.setDraftPool(temporaryDraftpool);
        game.setDiceBag(temporaryDicebag);
        game.setRoundTrack(temporaryRoundtrack);
        player.setSchemaCard(temporarySchemaCard);
        turnState.setSkipTurnPlayers(skipTurnPlayers);
    }


    @Override
    public synchronized void start() {
        isExecutingCommands = true;
        super.start();
    }

    @Override
    public void run() {
        try {
            setTemporaryObjects();
            invokeCommands();
            updateObjects();
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Network error");
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().log(Level.INFO, "Invocation of commands interrupted");
            Thread.currentThread().interrupt();
        } finally {
            setIsExecutingCommands(false);
            game.getState().releaseToolCardExecution();
        }

    }

    /**
     * Invoke the list of commands
     *
     * @throws RemoteException network error
     * @throws InterruptedException if the toolCard execution is interrupted by a client command or
     *                              because the command can't proceed
     */
    private void invokeCommands() throws RemoteException, InterruptedException {
        if (commandRoot == null) {
            throw new IllegalStateException();
        }
        CommandFlow commandFlow;
        Node<ICommand> root = commandRoot;
        do {
            commandFlow = root.getData().executeCommand(player, this, turnState);

            if (commandFlow == CommandFlow.MAIN) {
                root = root.getLeftChild();
            } else if (commandFlow == CommandFlow.SUB) {
                root = root.getRightChild();
            } else if (commandFlow == CommandFlow.STOP) {
                observers.forEach(IToolCardExecutorObserver::notifyCommandInterrupted);
                throw new InterruptedException();
            } else if (commandFlow == CommandFlow.REPEAT) {
                observers.forEach(IToolCardExecutorObserver::notifyError);
            }

        } while (root != null);
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

    public void waitToolCardExecutionEnd() throws InterruptedException {
        synchronized (executorMonitor) {
            while (isExecutingCommands)
                executorMonitor.wait();
        }
    }

    public void setIsExecutingCommands(boolean isExecutingCommands) {
        synchronized (executorMonitor) {
            this.isExecutingCommands = isExecutingCommands;
            if (!this.isExecutingCommands)
                executorMonitor.notifyAll();
        }
    }

    public boolean isExecutingCommands() {
        synchronized (executorMonitor) {
            return isExecutingCommands;
        }
    }

    public DraftPool getTemporaryDraftpool() {
        return temporaryDraftpool;
    }

    public DrawableCollection<Dice> getTemporaryDicebag() {
        return temporaryDicebag;
    }

    public RoundTrack getTemporaryRoundtrack() {
        return temporaryRoundtrack;
    }

    public SchemaCard getTemporarySchemaCard() {
        return temporarySchemaCard;
    }

    public void interruptCommandsInvocation() {
        this.interrupt();
    }

}
