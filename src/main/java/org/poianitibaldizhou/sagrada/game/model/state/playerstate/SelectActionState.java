package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

/**
 * OVERVIEW: Represents the state of the player in which he has to choose the action that
 * he wants to perform.
 */
public class SelectActionState extends IPlayerState {

    /**
     * Constructor.
     * Creates the choose action state for the player. This needs the general turn state of the state machine
     * of the game
     *
     * @param turnState game turn state in which the choice occurs
     */
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
