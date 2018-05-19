package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

public class PlaceDiceState extends IPlayerState {


    public PlaceDiceState(TurnState turnState) {
        super(turnState);
    }

    @Override
    public void placeDice(Player player, Dice dice, int row, int column) throws RuleViolationException {
        player.getSchemaCard().setDice(dice,row,column);
        turnState.setPlayerState(new SelectActionState(turnState));
    }

}
