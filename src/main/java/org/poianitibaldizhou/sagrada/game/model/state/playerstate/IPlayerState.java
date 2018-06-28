package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

/**
 * OVERVIEW: Represents the interface for the state of the player inside of his turn
 */
public abstract class IPlayerState {

    protected TurnState turnState;

    /**
     * Constructor.
     * Creates the player state of a certain turn of the game
     *
     * @param turnState the current turnState
     */
    protected IPlayerState(TurnState turnState) {
        this.turnState = turnState;
    }

    /**
     * The player has chosen an action.
     *
     * @param action action that the player has chosen
     * @throws InvalidActionException if instance != SelectActionState
     */
    public void chooseAction(IActionCommand action) throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * The player has used a tool card
     *
     * @param player   player who wants to use the tool card
     * @param toolCard tool card chosen
     * @return true if the card is usable
     * @throws InvalidActionException if instance != UseCardState
     */
    public boolean useCard(Player player, ToolCard toolCard) throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * The player wants to place a dice
     *
     * @param player   player that wants to place a dice
     * @param dice     dice that the player wants to place
     * @param position the player wants to place dice on this position on his schema card
     * @throws InvalidActionException if instance != PlaceDiceState
     * @throws RuleViolationException if the placement on the schema card violates some rule
     */
    public void placeDice(Player player, Dice dice, Position position) throws InvalidActionException, RuleViolationException {
        throw new InvalidActionException();
    }

    /**
     * The player wants to terminate his turn
     *
     * @throws InvalidActionException if instance != EndTurnState
     */
    public void endTurn() throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * Release the tool card execution.
     * Does nothing if instance != UserCardState
     */
    public void releaseToolCardExecution() {
        // Do Nothing if it's not useCardState
    }
}
