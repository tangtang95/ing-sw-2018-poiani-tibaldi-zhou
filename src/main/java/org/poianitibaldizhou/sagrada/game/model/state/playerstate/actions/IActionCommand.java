package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

public interface IActionCommand {
    void executeAction(TurnState turnState);
}
