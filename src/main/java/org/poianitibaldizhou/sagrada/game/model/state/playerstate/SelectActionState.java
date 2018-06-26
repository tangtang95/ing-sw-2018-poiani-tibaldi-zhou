package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

public class SelectActionState extends IPlayerState {

    public SelectActionState(TurnState turnState) {
        super(turnState);
    }

    /**
     * Execute the action based on the IActionCommand, basically it changes the IPlayerState on the turnState
     *
     * @param action the operation of the player
     */
    @Override
    public void chooseAction(IActionCommand action) {
        action.executeAction(turnState);
    }

}
