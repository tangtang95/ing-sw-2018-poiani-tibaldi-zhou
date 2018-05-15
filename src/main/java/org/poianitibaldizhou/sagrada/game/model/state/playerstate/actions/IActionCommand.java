package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public interface IActionCommand {
    void executeAction(TurnState turnState);
}
