package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: This class manages the execution of a certain tool card.
 */
public class ToolCardExecutor extends Thread {
    // Monitors
    private final Object diceMonitor;
    private final Object colorMonitor;
    private final Object valueMonitor;
    private final Object turnEndMonitor;
    private final Object positionMonitor;
    private final Object executorMonitor;
    private final Object answerMonitor;

    // Values monitored
    private Dice neededDice;
    private Color neededColor;
    private Integer neededValue;
    private Position neededPosition;
    private Boolean neededAnswer;
    private boolean turnEnd;
    private List<IToolCardExecutorFakeObserver> observers;

    // Executor's attribute
    private Node<ICommand> coreCommands;
    private Node<ICommand> preCommands;
    private boolean isExecutingCommands;
    private Player player;

    private DraftPool temporaryDraftPool;
    private DrawableCollection<Dice> temporaryDiceBag;
    private RoundTrack temporaryRoundTrack;
    private SchemaCard temporarySchemaCard;

    private Map<Player, Integer> temporarySkipTurnPlayers;
    private Game game;
    private TurnState turnState;

    /**
     * Constructor.
     *
     * @param game      game that contains the tool card that needs to be executed
     * @param player    player that has executed the tool card
     * @param turnState turn in which the tool card is executed
     */
    public ToolCardExecutor(Game game, Player player, TurnState turnState) {
        diceMonitor = new Object();
        colorMonitor = new Object();
        valueMonitor = new Object();
        turnEndMonitor = new Object();
        positionMonitor = new Object();
        executorMonitor = new Object();
        answerMonitor = new Object();

        neededDice = null;
        neededValue = null;
        neededColor = null;
        neededPosition = null;
        turnEnd = false;
        isExecutingCommands = false;

        this.coreCommands = null;
        this.preCommands = null;
        this.observers = new ArrayList<>();
        this.player = player;
        this.turnState = turnState;
        this.game = game;
    }

    /**
     * Run the commands of the tool card that need to be executed by this.
     */
    @Override
    public void run() {
        runCommands();
    }

    /**
     * Set the fact the tool card execution is started and starts the execution.
     */
    @Override
    public void start() {
        setIsExecutingCommands(true);
        super.start();
    }

    /**
     * Set the core commands of the tool card execution
     *
     * @param commands core commands of the tool card
     */
    public void setCoreCommands(Node<ICommand> commands) {
        this.coreCommands = commands;
    }

    /**
     * Set the pre commands of the tool card execution.
     *
     * @param commands pre commands of the tool card
     */
    public void setPreCommands(Node<ICommand> commands) {
        this.preCommands = commands;
    }

    /**
     * Add an tool card executor observer to the tool card
     *
     * @param observer observer that needs to listen to the execution of the tool card
     */
    public void addObserver(IToolCardExecutorFakeObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Returns the list of observers that listen to the execution of the tool card
     *
     * @return list of observers that listen to the execution of the tool card
     */
    public List<IToolCardExecutorFakeObserver> getObservers() {
        return observers;
    }

    /**
     * Set all the temporary view
     */
    public void setTemporaryObjects() {
        this.temporaryDraftPool = game.getDraftPool();
        this.temporaryDiceBag = game.getDiceBag();
        this.temporaryRoundTrack = game.getRoundTrack();
        this.temporarySchemaCard = player.getSchemaCard();
        this.temporarySkipTurnPlayers = turnState.getSkipTurnPlayers();
    }

    /**
     * Set the modified view saved before
     */
    private void updateObjects() {
        game.setDraftPool(temporaryDraftPool);
        game.setDiceBag(temporaryDiceBag);
        game.setRoundTrack(temporaryRoundTrack);
        player.setSchemaCard(temporarySchemaCard);
        turnState.setSkipTurnPlayers(temporarySkipTurnPlayers);
    }

    /**
     * Run the commands of the tool card.
     */
    private void runCommands() {
        if (coreCommands == null || preCommands == null) {
            throw new IllegalStateException(ServerMessage.TOOL_CARD_EXECUTOR_ILLEGAL_STATE);
        }
        try {
            setTemporaryObjects();
            invokeCommands(preCommands);
            updateObjects();
            setTemporaryObjects();
            invokeCommands(coreCommands);
            updateObjects();
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().log(Level.INFO, ServerMessage.TOOL_CARD_EXECUTOR_INVALID_COMMAND);
            Thread.currentThread().interrupt();
        } finally {
            setIsExecutingCommands(false);
            turnState.releaseToolCardExecution();
        }
        observers.forEach(IToolCardExecutorFakeObserver::notifyExecutionEnded);

    }

    /**
     * Invoke the list of commands
     *
     * @throws InterruptedException if the toolCard execution is interrupted by a client command or
     *                              because the command can't proceed
     */
    private void invokeCommands(Node<ICommand> commands) throws InterruptedException {
        CommandFlow commandFlow;
        Node<ICommand> root = commands;
        do {
            commandFlow = root.getData().executeCommand(player, this, turnState);

            if (commandFlow == CommandFlow.MAIN) {
                root = root.getLeftChild();
            } else if (commandFlow == CommandFlow.SUB) {
                root = root.getRightChild();
            } else if (commandFlow == CommandFlow.REPEAT) {
                observers.forEach(IToolCardExecutorFakeObserver::notifyRepeatAction);
            } else if (commandFlow.getProtocolNumber() == 400) {
                final CommandFlow finalCommandFlow = commandFlow;
                observers.forEach(obs -> obs.notifyCommandInterrupted(finalCommandFlow));
                throw new InterruptedException();
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

    public Position getNeededPosition() throws InterruptedException {
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

    public boolean getNeededAnswer() throws InterruptedException {
        synchronized (answerMonitor) {
            while (neededAnswer == null)
                answerMonitor.wait();
            return neededAnswer;
        }
    }

    public void setNeededAnswer(boolean answer) {
        synchronized (answerMonitor) {
            this.neededAnswer = answer;
            answerMonitor.notifyAll();
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

    public DraftPool getTemporaryDraftPool() {
        return temporaryDraftPool;
    }

    public DrawableCollection<Dice> getTemporaryDiceBag() {
        return temporaryDiceBag;
    }

    public RoundTrack getTemporaryRoundTrack() {
        return temporaryRoundTrack;
    }

    public SchemaCard getTemporarySchemaCard() {
        return temporarySchemaCard;
    }

    /**
     * Interrupt the commands execution of the tool card
     */
    public void interruptCommandsInvocation() {
        this.interrupt();
    }

    /**
     * Signal the fact that a player need to skip a certain turn
     *
     * @param player player that will skip a turn
     * @param turn   number of the turn that will be skipped by player
     */
    public void addSkipTurnPlayer(Player player, int turn) {
        if (turn < TurnState.FIRST_TURN || turn > TurnState.SECOND_TURN)
            throw new IllegalArgumentException(ServerMessage.TURN_ILLEGAL_ARGUMENT);
        temporarySkipTurnPlayers.put(player, turn);
    }
}
