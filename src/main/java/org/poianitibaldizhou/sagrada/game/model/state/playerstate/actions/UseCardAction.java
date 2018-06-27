package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.UseCardState;

import java.util.Objects;

/**
 * OVERVIEW: Represents the action of the user of choosing and using a tool card
 */
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
     * Creates a new use card action
     *
     * @return an instance of the action
     */
    public static UseCardAction toObject() {
        return new UseCardAction();
    }
}
