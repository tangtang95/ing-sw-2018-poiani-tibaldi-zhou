package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

/**
 * OVERVIEW: Represents an action that can be executed from the client. Concrete action are actions that a player
 * can use during his turn.
 */
public interface IActionCommand extends JSONable{

    /**
     * Player executes a certain action in his turn
     * @param turnState player's turn
     */
    void executeAction(TurnState turnState);
}
