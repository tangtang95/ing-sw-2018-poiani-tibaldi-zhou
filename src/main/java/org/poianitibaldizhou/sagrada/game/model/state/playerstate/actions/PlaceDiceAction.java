package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.PlaceDiceState;

import java.rmi.RemoteException;
import java.util.Objects;

public class PlaceDiceAction implements IActionCommand{

    /**
     * Change the state of PlayerState inside turnState to PlaceDiceState
     *
     * @param turnState the actual turnState of the game
     */
    @Override
    public void executeAction(TurnState turnState) throws RemoteException {
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
}
