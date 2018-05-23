package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.EndTurnState;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EndTurnAction implements IActionCommand {

    /**
     * Change the state of PlayerState inside the turnState to EndTurnState
     *
     * @param turnState the actual turnState of the game
     */
    @Override
    public void executeAction(TurnState turnState) {
        EndTurnState endTurnState = new EndTurnState(turnState);
        turnState.setPlayerState(endTurnState);
        endTurnState.endTurn();
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
