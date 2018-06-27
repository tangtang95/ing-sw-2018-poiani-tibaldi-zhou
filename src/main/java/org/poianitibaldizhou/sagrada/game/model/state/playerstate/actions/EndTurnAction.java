package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;


import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.EndTurnState;


import java.util.Objects;

/**
 * OVERVIEW: Represents the action of ending a turn
 */
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

    /**
     * Return null because this action is never sent to the client over the network
     *
     * @return null
     */
    @Override
    public JSONObject toJSON() {
        return null;
    }

    /**
     * Creates a new end turn action
     *
     * @return an instance of the action
     */
    public static EndTurnAction toObject() {
        return new EndTurnAction();
    }
}
