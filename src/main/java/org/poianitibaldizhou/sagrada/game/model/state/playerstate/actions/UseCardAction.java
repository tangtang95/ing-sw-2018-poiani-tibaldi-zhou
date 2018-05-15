package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.UseCardState;

import java.util.Objects;

public class UseCardAction implements IActionCommand {
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
}
