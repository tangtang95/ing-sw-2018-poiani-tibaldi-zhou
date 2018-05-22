package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.IPlayerState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.SelectActionState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;

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
     * copy_constructor
     *
     * @param turnState turnState to copy
     */
    private TurnState(TurnState turnState) {
        super(turnState.game);
        this.currentRoundPlayer = turnState.currentRoundPlayer;
        this.currentTurnPlayer = turnState.currentTurnPlayer;
        this.playerState = turnState.playerState;
        this.currentRound = turnState.currentRound;
        this.isFirstTurn = turnState.isFirstTurn;
        this.actionsUsed = new HashSet<>();
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
        for (Player player : skipTurnPlayers.keySet()) {
            this.skipTurnPlayers.put(player, skipTurnPlayers.get(player));
        }

    }

    @Override
    public void init() {
        if (skipTurnPlayers.containsKey(getCurrentTurnPlayer()) && skipTurnPlayers.get(getCurrentTurnPlayer()) == (isFirstTurn ? 1 : 2)) {
            //TODO notify skip player
            nextTurn();
        }
    }




    /**
     * Pass the operation of chooseAction to the playerState
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
        actionsUsed.add(action);
        playerState.chooseAction(action);
    }

    /**
     * Pass the operation of useCard to the playerState
     *
     * @param player   the currentTurnPlayer who choose to use the card
     * @param toolCard the toolCard to be used
     * @throws NoCoinsExpendableException there aren't expendable coins
     * @throws InvalidActionException     if the given player is different from the currentTurnPlayer
     */
    @Override
    public void useCard(Player player, ToolCard toolCard, IToolCardExecutorObserver observer) throws NoCoinsExpendableException, InvalidActionException, RemoteException, InterruptedException {
        if (!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        if(!playerState.useCard(player, toolCard, game))
            throw new InvalidActionException();
        Node<ICommand> rootCommand = game.getCompleteCommands(toolCard);
        toolCardExecutor.setCommands(rootCommand);
        toolCardExecutor.addObserver(observer);
        toolCardExecutor.start();
    }

    /**
     * Pass the operation of placeCard to the playerState
     *
     * @param player the currentTurnPlayer who choose to place a dice
     * @param dice   the dice to be placed
     * @param position
     * @throws InvalidActionException if the given player is different from the currentTurnPlayer
     * @throws RuleViolationException if the rules of placement are violated
     */
    @Override
    public void placeDice(Player player, Dice dice, Position position) throws RuleViolationException, InvalidActionException {
        if (!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        playerState.placeDice(player, dice, position);
    }

    @Override
    public void releaseToolCardExecution() {
        playerState.releaseToolCardExecution();
    }

    @Override
    public void fireExecutorEvent(ExecutorEvent event) throws InvalidActionException {
        if(!toolCardExecutor.isExecutingCommands())
            throw new InvalidActionException();
        event.setNeededValue(toolCardExecutor);
    }

    @Override
    public void interruptToolCardExecution() {
        if(toolCardExecutor.isExecutingCommands())
            toolCardExecutor.interruptCommandsInvocation();
    }

    /**
     * This method is called when the currentTurnPlayer end his turn: if it's the second turn and the currentTurnPlayer ending the turn
     * is the current currentTurnPlayer who has thrown the dices then the state goes to RoundEndState, otherwise if it's first
     * turn and the currentTurnPlayer ending the turn is the last currentTurnPlayer(the one before the current currentTurnPlayer of the round) then
     * it starts the second run. Otherwise the turn goes to the next currentTurnPlayer (if is the first turn than the direction is
     * clockwise otherwise it's counter clockwise)
     */
    public void nextTurn() {
        if (!isFirstTurn && currentTurnPlayer.equals(currentRoundPlayer))
            game.setState(new RoundEndState(game, currentRound, currentRoundPlayer));
        else {
            Player lastPlayer = game.getNextPlayer(currentRoundPlayer, Direction.COUNTER_CLOCKWISE);
            if (isFirstTurn &&  currentTurnPlayer.equals(lastPlayer))
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

    public boolean hasActionUsed(PlaceDiceAction placeDiceAction) {
        return actionsUsed.contains(placeDiceAction);
    }

    public Map<Player, Integer> getSkipTurnPlayers(){
        return new HashMap<>(skipTurnPlayers);
    }

    public void setSkipTurnPlayers(Map<Player, Integer> skipTurnPlayers){
        this.skipTurnPlayers.clear();
        this.skipTurnPlayers.putAll(skipTurnPlayers);
    }

    public void setPlayerState(IPlayerState playerState) {
        this.playerState = playerState;
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

    public void addSkipTurnPlayer(Player player, int turn) {
        this.skipTurnPlayers.put(player, turn);
    }

    public ToolCardExecutor getToolCardExecutor() {
        return toolCardExecutor;
    }

    @Override
    public Player getCurrentRoundPlayer() {
        return currentRoundPlayer;
    }

    public static IStateGame newInstance(IStateGame ts) {
        if (ts == null)
            return null;
        return new TurnState((TurnState) ts);
    }


}
