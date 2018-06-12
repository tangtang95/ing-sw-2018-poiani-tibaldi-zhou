package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public interface IActionCommand extends JSONable{
    void executeAction(TurnState turnState);
}
