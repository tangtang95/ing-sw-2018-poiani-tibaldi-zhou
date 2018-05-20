package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Node;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public class UseCardState extends IPlayerState {

    public UseCardState(TurnState turnState) {
        super(turnState);
    }

    @Override
    public boolean useCard(Player player, ToolCard toolCard, Game game) throws InvalidActionException {
        return player.isCardUsable(toolCard);
    }

    @Override
    public void releaseToolCardExecution() {
        turnState.setPlayerState(new SelectActionState(turnState));
    }

}
