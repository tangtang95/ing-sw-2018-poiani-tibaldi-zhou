package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;


import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.EndTurnState;


import java.util.Objects;


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

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return new EndTurnAction();
    }
}
