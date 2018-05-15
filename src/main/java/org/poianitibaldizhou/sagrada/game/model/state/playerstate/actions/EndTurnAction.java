package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.EndTurnState;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EndTurnAction implements IActionCommand{

    @Override
    public void executeAction(TurnState turnState) {
        EndTurnState endTurnState = new EndTurnState(turnState);
        turnState.setPlayerState(endTurnState);
        try {
            endTurnState.endTurn();
        } catch (InvalidActionException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Shouldn't happen", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EndTurnAction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(EndTurnAction.class);
    }
}
