package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public class UseCardState extends IPlayerState {

    public UseCardState(TurnState turnState) {
        super(turnState);
        turnState.notifyOnUseToolCardState();
    }

    /**
     * Return true if the toolCard is usable
     *
     * @param player the player who wants to use the toolCard
     * @param toolCard the toolCard chosen to use
     * @return true if the toolCard is usable
     */
    @Override
    public boolean useCard(Player player, ToolCard toolCard) {
        return player.isCardUsable(toolCard);
    }

    @Override
    public void releaseToolCardExecution() {
        turnState.setPlayerState(new SelectActionState(turnState));
    }

}
