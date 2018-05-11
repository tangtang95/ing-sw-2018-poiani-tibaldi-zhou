package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Direction;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.IPlayerState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.SelectActionState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

import java.util.HashSet;
import java.util.Set;

public class TurnState extends IStateGame implements ICurrentRoundPlayer {

    private final Player currentRoundPlayer;
    private final Player currentTurnPlayer;
    private IPlayerState playerState;
    private int currentRound;
    private boolean isFirstTurn;
    private Set<IActionCommand> actionUsed;

    /**
     * Constructor.
     * Create the TurnState for the currentTurnPlayer
     *
     * @param game               the current game
     * @param currentRound       the number of the currentRound (from 0 to 9)
     * @param currentRoundPlayer the current currentTurnPlayer of the round
     * @param currentTurnPlayer             the current currentTurnPlayer
     * @param isFirstTurn        the boolean that tells if it is first turn or not
     */
    TurnState(Game game, int currentRound, Player currentRoundPlayer, Player currentTurnPlayer, boolean isFirstTurn) {
        super(game);
        this.currentRound = currentRound;
        this.currentRoundPlayer = currentRoundPlayer;
        this.currentTurnPlayer = currentTurnPlayer;
        this.isFirstTurn = isFirstTurn;
        actionUsed = new HashSet<>();
        this.playerState = new SelectActionState(this);
    }

    /**
     * This method is called when the currentTurnPlayer end his turn: if it's the second turn and the currentTurnPlayer ending the turn
     * is the current currentTurnPlayer who has thrown the dices then the state goes to RoundEndState, otherwise if it's first
     * turn and the currentTurnPlayer ending the turn is the last currentTurnPlayer(the one before the current currentTurnPlayer of the round) then
     * it starts the second run. Otherwise the turn goes to the next currentTurnPlayer (if is the first turn than the direction is
     * clockwise otherwise it's counter clockwise)
     */
    @Override
    public void nextTurn() {
        if (!isFirstTurn && currentTurnPlayer.equals(currentRoundPlayer))
            game.setState(new RoundEndState(game, currentRound, currentRoundPlayer));
        else {
            int indexLastPlayer = game.getNextIndexOfPlayer(currentRoundPlayer, Direction.COUNTER_CLOCKWISE);
            if (isFirstTurn && currentTurnPlayer.equals(game.getPlayers().get(indexLastPlayer)))
                game.setState(new TurnState(game, currentRound, currentRoundPlayer, currentTurnPlayer, false));
            else {
                int indexNextPlayer = game.getNextIndexOfPlayer(currentTurnPlayer, (isFirstTurn) ? Direction.CLOCKWISE :
                        Direction.COUNTER_CLOCKWISE);
                Player nextPlayer = game.getPlayers().get(indexNextPlayer);
                game.setState(new TurnState(game, currentRound, currentRoundPlayer, nextPlayer, isFirstTurn));
            }
        }
    }


    /**
     * Pass the operation of chooseAction to the playerState
     *
     * @param action the operation of the currentTurnPlayer
     * @throws InvalidActionException if the given player is different from the currentTurnPlayer
     */
    public void chooseAction(Player player, IActionCommand action) throws InvalidActionException {
        if(!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        if(actionUsed.contains(action))
            throw new InvalidActionException();
        actionUsed.add(action);
        playerState.chooseAction(action);
    }

    /**
     * Pass the operation of useCard to the playerState
     *
     * @param player   the currentTurnPlayer who choose to use the card
     * @param toolCard the toolCard to be used
     * @throws NoCoinsExpendableException
     * @throws InvalidActionException if the given player is different from the currentTurnPlayer
     */
    @Override
    public void useCard(Player player, ToolCard toolCard) throws NoCoinsExpendableException, InvalidActionException {
        if(!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        playerState.useCard(player, toolCard, game);
    }

    /**
     * Pass the operation of placeCard to the playerState
     *
     * @param player the currentTurnPlayer who choose to place a dice
     * @param dice   the dice to be placed
     * @throws InvalidActionException if the given player is different from the currentTurnPlayer
     * @throws RuleViolationException
     */
    @Override
    public void placeDice(Player player, Dice dice, int row, int column) throws RuleViolationException, InvalidActionException {
        if(!player.equals(currentTurnPlayer))
            throw new InvalidActionException();
        playerState.placeDice(player, dice, row, column);
    }

    public void addActionUsed(IActionCommand action) {
        actionUsed.add(action);
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

    @Override
    public Player getCurrentRoundPlayer() {
        return currentRoundPlayer;
    }



}
