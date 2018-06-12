package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

public interface IActionCommand extends JSONable{
    void executeAction(TurnState turnState);
}
