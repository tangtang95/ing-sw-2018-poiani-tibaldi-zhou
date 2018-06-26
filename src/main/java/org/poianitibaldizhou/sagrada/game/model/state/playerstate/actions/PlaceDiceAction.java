package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.PlaceDiceState;

import java.util.Objects;

public class PlaceDiceAction implements IActionCommand{

    /**
     * Change the state of PlayerState inside turnState to PlaceDiceState
     *
     * @param turnState the actual turnState of the game
     */
    @Override
    public void executeAction(TurnState turnState) {
        turnState.setPlayerState(new PlaceDiceState(turnState));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlaceDiceAction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PlaceDiceAction.class);
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    public static PlaceDiceAction toObject() {
        return new PlaceDiceAction();
    }
}
