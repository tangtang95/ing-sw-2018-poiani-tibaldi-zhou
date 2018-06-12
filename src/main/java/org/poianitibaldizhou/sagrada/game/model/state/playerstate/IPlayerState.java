package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;


public abstract class IPlayerState {

    protected TurnState turnState;

    protected IPlayerState(TurnState turnState) {
        this.turnState = turnState;
    }

    public void chooseAction(IActionCommand action) throws InvalidActionException {
        throw new InvalidActionException();
    }

    public boolean useCard(Player player, ToolCard toolCard) throws InvalidActionException {
        throw new InvalidActionException();
    }

    public void placeDice(Player player, Dice dice, Position position) throws InvalidActionException, RuleViolationException {
        throw new InvalidActionException();
    }

    public void endTurn() throws InvalidActionException{
        throw new InvalidActionException();
    }

    public void releaseToolCardExecution() {
        // Do Nothing if it's not useCardState
    }
}
