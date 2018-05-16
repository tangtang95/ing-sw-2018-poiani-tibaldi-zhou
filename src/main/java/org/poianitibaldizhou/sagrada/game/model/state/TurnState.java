package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnState extends IStateGame implements ICurrentRoundPlayer {

    private final Player currentRoundPlayer;
    private final Player currentTurnPlayer;
    private IPlayerState playerState;
    private int currentRound;
    private boolean isFirstTurn;
    private Set<IActionCommand> actionsUsed;
    private Map<Player, Integer> skipTurnPlayers;
    private ToolCardExecutor toolCardExecutor;

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
        this.toolCardExecutor = null;
        this.playerState = new SelectActionState(this);
        this.skipTurnPlayers = new HashMap<>();
    }

    /**
     * copy_constructor
     *
     * @param turnState turnState to copy
     */
    private TurnState(TurnState turnState) {
        super(turnState.game);
        this.currentRoundPlayer = Player.newInstance(turnState.getCurrentRoundPlayer());
        this.currentTurnPlayer = Player.newInstance(turnState.getCurrentTurnPlayer());
        this.playerState = turnState.playerState;
        this.currentRound = turnState.currentRound;
        this.isFirstTurn = turnState.isFirstTurn;
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
        this.toolCardExecutor = null;
        this.playerState = new SelectActionState(this);
        this.skipTurnPlayers = new HashMap<>();
        for (Player player : skipTurnPlayers.keySet()) {
            this.skipTurnPlayers.put(player, skipTurnPlayers.get(player));
        }
    }

    @Override
    public void init() {
        if (skipTurnPlayers.get(getCurrentTurnPlayer()) == (isFirstTurn ? 1 : 2)) {
            //TODO notify skip player
            nextTurn();
        }
    }

    /**
     * This method is called when the currentTurnPlayer end his turn: if it's the second turn and the currentTurnPlayer ending the turn
     * is the current currentTurnPlayer who has thrown the dices then the state goes to RoundEndState, otherwise if it's first
     * turn and the currentTurnPlayer ending the turn is the last currentTurnPlayer(the one before the current currentTurnPlayer of the round) then
     * it starts the second run. Otherwise the turn goes to the next currentTurnPlayer (if is the first turn than the direction is
     * clockwise otherwise it's counter clockwise)
     */
    private void nextTurn() {
        if (!isFirstTurn && currentTurnPlayer.equals(currentRoundPlayer))
            game.setState(new RoundEndState(game, currentRound, currentRoundPlayer));
        else {
            int indexLastPlayer = game.getNextIndexOfPlayer(currentRoundPlayer, Direction.COUNTER_CLOCKWISE);
            if (isFirstTurn && currentTurnPlayer.equals(game.getPlayers().get(indexLastPlayer)))
                game.setState(new TurnState(game, currentRound, currentRoundPlayer, currentTurnPlayer,
                        false, skipTurnPlayers));
            else {
                int indexNextPlayer = game.getNextIndexOfPlayer(currentTurnPlayer, (isFirstTurn) ? Direction.CLOCKWISE :
                        Direction.COUNTER_CLOCKWISE);
                Player nextPlayer = game.getPlayers().get(indexNextPlayer);
                game.setState(new TurnState(game, currentRound, currentRoundPlayer, nextPlayer, isFirstTurn,
                        skipTurnPlayers));
            }
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
        // TODO This control should be checked from the controller or gameManager
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
        Node<ICommand> rootCommand = playerState.useCard(player, toolCard);
        toolCardExecutor = new ToolCardExecutor(rootCommand, player, game);
        toolCardExecutor.addObserver(observer);
        toolCardExecutor.start();
    }

    /**
     * Pass the operation of placeCard to the playerState
     *
     * @param player the currentTurnPlayer who choose to place a dice
     * @param dice   the dice to be placed
     * @throws InvalidActionException if the given player is different from the currentTurnPlayer
     * @throws RuleViolationException if the rules of placement are violated
     */
    @Override
    public void placeDice(Player player, Dice dice, int row, int column) throws RuleViolationException, InvalidActionException {
        if (!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        playerState.placeDice(player, dice, row, column);
    }

    public synchronized void waitUntilToolCardExecutionEnded() {
        if (toolCardExecutor != null) {
            toolCardExecutor.setTurnEnded(true);
            try {
                toolCardExecutor.waitForToolCardExecutionEnd();
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "interrupt signal called");
            }
        }
        nextTurn();
    }

    public boolean hasActionUsed(PlaceDiceAction placeDiceAction) {
        return actionsUsed.contains(placeDiceAction);
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
        this.skipTurnPlayers.put(Player.newInstance(player), turn);
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
