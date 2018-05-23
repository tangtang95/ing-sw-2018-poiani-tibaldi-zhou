package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

import java.rmi.RemoteException;


public abstract class IPlayerState {

    protected TurnState turnState;

    protected IPlayerState(TurnState turnState) {
        this.turnState = turnState;
    }

    public void chooseAction(IActionCommand action) throws InvalidActionException, RemoteException {
        throw new InvalidActionException();
    }

    public boolean useCard(Player player, ToolCard toolCard) throws NoCoinsExpendableException, InvalidActionException, RemoteException {
        throw new InvalidActionException();
    }

    public void placeDice(Player player, Dice dice, Position position) throws InvalidActionException, RuleViolationException, RemoteException {
        throw new InvalidActionException();
    }

    public void endTurn() throws InvalidActionException, RemoteException {
        throw new InvalidActionException();
    }

    public void releaseToolCardExecution() {
        // Do Nothing if it's not useCardState
    }
}
