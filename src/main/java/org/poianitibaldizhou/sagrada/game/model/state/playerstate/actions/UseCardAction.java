package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.UseCardState;

import java.rmi.RemoteException;
import java.util.Objects;

public class UseCardAction implements IActionCommand {

    /**
     * Change the state of PlayerState inside turnState to UseCardState
     *
     * @param turnState the actual turnState of the game
     */
    @Override
    public void executeAction(TurnState turnState) {
        turnState.setPlayerState(new UseCardState(turnState));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UseCardAction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(UseCardAction.class);
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return new UseCardAction();
    }
}
