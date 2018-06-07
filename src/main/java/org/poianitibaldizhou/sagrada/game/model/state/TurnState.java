package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.EndTurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.IPlayerState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.PlaceDiceState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.SelectActionState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.UseCardAction;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TurnState extends IStateGame implements ICurrentRoundPlayer {

    private final Player currentRoundPlayer;
    private final Player currentTurnPlayer;
    private final int currentRound;
    private final boolean isFirstTurn;
    private final Set<IActionCommand> actionsUsed;
    private final ToolCardExecutor toolCardExecutor;
    private final Map<Player, Integer> skipTurnPlayers;

    private IPlayerState playerState;

    private static final int FIRST_TURN = 1;
    private static final int SECOND_TURN = 2;


    /**
     * Constructor.
     * Create the TurnState for the currentTurnPlayer
     *
     * @param game               the current game
     * @param currentRound       the number of the currentRound (from 0 to 9)
     * @param currentRoundPlayer the current currentTurnPlayer of the round
     * @param currentTurnPlayer  the current currentTurnPlayer
     * @param isFirstTurn        the boolean that tells if it is first turn or not
     */
    public TurnState(Game game, int currentRound, Player currentRoundPlayer, Player currentTurnPlayer, boolean isFirstTurn) {
        super(game);
        this.currentRound = currentRound;
        this.currentRoundPlayer = currentRoundPlayer;
        this.currentTurnPlayer = currentTurnPlayer;
        this.isFirstTurn = isFirstTurn;
        this.actionsUsed = new HashSet<>();
        this.playerState = new SelectActionState(this);
        this.skipTurnPlayers = new HashMap<>();
        this.toolCardExecutor = new ToolCardExecutor(game, currentTurnPlayer, this);
    }

    /**
     * Constructor.
     * Create the TurnState for the currentTurnPlayer
     *
     * @param game               the current game
     * @param currentRound       the number of the currentRound (from 0 to 9)
     * @param currentRoundPlayer the current currentTurnPlayer of the round
     * @param currentTurnPlayer  the current currentTurnPlayer
     * @param isFirstTurn        the boolean that tells if it is first turn or not
     * @param skipTurnPlayers    the hashMap containing the players who need to skip the turn
     */
    public TurnState(Game game, int currentRound, Player currentRoundPlayer, Player currentTurnPlayer,
                     boolean isFirstTurn, Map<Player, Integer> skipTurnPlayers) {
        super(game);
        this.currentRound = currentRound;
        this.currentRoundPlayer = currentRoundPlayer;
        this.currentTurnPlayer = currentTurnPlayer;
        this.isFirstTurn = isFirstTurn;
        this.actionsUsed = new HashSet<>();
        this.toolCardExecutor = new ToolCardExecutor(game, currentTurnPlayer, this);
        this.playerState = new SelectActionState(this);
        this.skipTurnPlayers = new HashMap<>();
        skipTurnPlayers.keySet().forEach(player -> this.skipTurnPlayers.put(player, skipTurnPlayers.get(player)));

    }

    @Override
    public void init() {
        if (skipTurnPlayers.containsKey(getCurrentTurnPlayer())
                && skipTurnPlayers.get(getCurrentTurnPlayer()) == (isFirstTurn ? FIRST_TURN : SECOND_TURN)) {
            game.getStateObservers().forEach((key, value) ->
                    value.onSkipTurnState(currentRound, isFirstTurn, currentRoundPlayer.getUser(), currentTurnPlayer.getUser()));
            nextTurn();
            return;
        }
        game.getStateObservers().forEach((key, value) -> value.onTurnState(currentRound, (isFirstTurn)?1:2, currentRoundPlayer.getUser(), currentTurnPlayer.getUser()));

    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @param action the operation of the currentTurnPlayer
     * @throws InvalidActionException if the given player is different from the currentTurnPlayer
     */
    @Override
    public void chooseAction(Player player, IActionCommand action) throws InvalidActionException {
        if (!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        if (actionsUsed.contains(action))
            throw new InvalidActionException();
        playerState.chooseAction(action);
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @param player   the currentTurnPlayer who choose to use the card
     * @param toolCard the toolCard to be used
     * @throws InvalidActionException if the given player is different from the currentTurnPlayer ||
     *                                there aren't expendable coins
     */
    @Override
    public void useCard(Player player, ToolCard toolCard, IToolCardExecutorFakeObserver observer)
            throws InvalidActionException {

        if (!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        try {
            if (!playerState.useCard(player, toolCard))
                throw new InvalidActionException();
        } catch (NoCoinsExpendableException e) {
            throw new InvalidActionException(e);
        }
        Node<ICommand> preCommands = game.getPreCommands(toolCard);
        toolCardExecutor.setPreCommands(preCommands);
        toolCardExecutor.setCoreCommands(toolCard.getCommands());
        toolCardExecutor.addObserver(observer);
        toolCardExecutor.start();
        actionsUsed.add(new UseCardAction());
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidActionException if the given player is different from the currentTurnPlayer ||
     *                                the rules of placement are violated
     */
    @Override
    public void placeDice(Player player, Dice dice, Position position) throws InvalidActionException {
        if (!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        if(!(playerState instanceof PlaceDiceState))
            throw new InvalidActionException();
        try {
            playerState.placeDice(player, dice, position);
        } catch (RuleViolationException e) {
            throw new InvalidActionException(e);
        }
        actionsUsed.add(new PlaceDiceAction());
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidActionException if the toolCardExecutor is not executing commands
     */
    @Override
    public void fireExecutorEvent(ExecutorEvent event) throws InvalidActionException {
        if (!toolCardExecutor.isExecutingCommands())
            throw new InvalidActionException();
        event.setNeededValue(toolCardExecutor);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidActionException if the player given is not equals the current turn player
     */
    @Override
    public void interruptToolCardExecution(Player player) throws InvalidActionException {
        if(!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        if (toolCardExecutor.isExecutingCommands())
            toolCardExecutor.interruptCommandsInvocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCurrentPlayer() {
        return currentTurnPlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCurrentRoundPlayer() {
        return currentRoundPlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceStateChange() {
        toolCardExecutor.interruptCommandsInvocation();
        if(!(playerState instanceof EndTurnState))
            setPlayerState(new EndTurnState(this));
    }

    /**
     * Release the toolCard execution from UseCardState to SelectActionState (doesn't necessarily means that the
     * toolCard execution is ended)
     */
    public void releaseToolCardExecution() {
        playerState.releaseToolCardExecution();
    }

    /**
     * This method is called when the currentTurnPlayer end his turn: if it's the second turn and the currentTurnPlayer ending the turn
     * is the current currentTurnPlayer who has thrown the dices then the state goes to RoundEndState, otherwise if it's first
     * turn and the currentTurnPlayer ending the turn is the last currentTurnPlayer(the one before the current currentTurnPlayer of the round) then
     * it starts the second startCLI. Otherwise the turn goes to the next currentTurnPlayer (if is the first turn than the direction is
     * clockwise otherwise it's counter clockwise)
     */
    public void nextTurn() {
        if (!isFirstTurn && currentTurnPlayer.equals(currentRoundPlayer))
            game.setState(new RoundEndState(game, currentRound, currentRoundPlayer));
        else {
            Player lastPlayer = game.getNextPlayer(currentRoundPlayer, Direction.COUNTER_CLOCKWISE);
            if (isFirstTurn && currentTurnPlayer.equals(lastPlayer))
                game.setState(new TurnState(game, currentRound, currentRoundPlayer, currentTurnPlayer,
                        false, skipTurnPlayers));
            else {
                Player nextPlayer = game.getNextPlayer(currentTurnPlayer, (isFirstTurn) ? Direction.CLOCKWISE :
                        Direction.COUNTER_CLOCKWISE);
                game.setState(new TurnState(game, currentRound, currentRoundPlayer, nextPlayer, isFirstTurn,
                        skipTurnPlayers));
            }
        }
    }

    // GETTER
    public boolean hasActionUsed(PlaceDiceAction placeDiceAction) {
        return actionsUsed.contains(placeDiceAction);
    }

    public Map<Player, Integer> getSkipTurnPlayers() {
        return new HashMap<>(skipTurnPlayers);
    }

    public IPlayerState getPlayerState() {
        return playerState;
    }

    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public boolean isFirstTurn() {
        return isFirstTurn;
    }

    /**
     * @return the reference of toolCardExecutor
     */
    public ToolCardExecutor getToolCardExecutor() {
        return toolCardExecutor;
    }

    // MODIFIERS
    public void setSkipTurnPlayers(Map<Player, Integer> skipTurnPlayers) {
        this.skipTurnPlayers.clear();
        this.skipTurnPlayers.putAll(skipTurnPlayers);
    }

    public void setPlayerState(IPlayerState playerState) {
        this.playerState = playerState;
    }

    public void addActionUsed(IActionCommand actionCommand) {
        actionsUsed.add(actionCommand);
    }

    public void addSkipTurnPlayer(Player player, int turn) {
        if(turn < FIRST_TURN || turn > SECOND_TURN)
            throw new IllegalArgumentException("Turn has to be 1 or 2");
        this.skipTurnPlayers.put(player, turn);
    }

    // NOTIFIERS
    public void notifyOnPlaceDiceState() {
        game.getStateObservers().forEach((key, value) -> value.onPlaceDiceState(currentTurnPlayer.getUser()));
    }

    public void notifyOnUseToolCardState() {
        game.getStateObservers().forEach((key, value) -> value.onUseCardState(currentTurnPlayer.getUser()));
    }

    public void notifyOnEndTurnState() {
        game.getStateObservers().forEach((key, value) -> value.onEndTurnState(currentTurnPlayer.getUser()));
    }


}
