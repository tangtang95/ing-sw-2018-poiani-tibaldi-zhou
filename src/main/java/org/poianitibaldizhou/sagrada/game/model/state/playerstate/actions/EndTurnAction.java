package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class EndTurnAction implements IActionCommand{

    @Override
    public void executeAction(TurnState turnState) {
        turnState.nextTurn();
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
