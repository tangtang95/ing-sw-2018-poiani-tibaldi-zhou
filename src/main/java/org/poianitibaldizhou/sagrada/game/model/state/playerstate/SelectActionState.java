package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

public class SelectActionState extends IPlayerState {

    public SelectActionState(TurnState turnState) {
        super(turnState);
    }

    /**
     * set the playerState
     *
     * @param action the operation of the player
     */
    @Override
    public void chooseAction(IActionCommand action) {
        action.executeAction(turnState);
    }

}
