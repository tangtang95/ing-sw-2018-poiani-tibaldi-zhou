package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

/**
 * OVERVIEW: In this player state the player can use a tool card
 */
public class UseCardState extends IPlayerState {

    /**
     * Constructor.
     * Creates the use card state for the player. This needs the general turn state of the state machine
     * of the game
     * @param turnState game turn state in which the use of the tool card occurs
     */
    public UseCardState(TurnState turnState) {
        super(turnState);
        turnState.notifyOnUseToolCardState();
    }

    /**
     * Return true if the toolCard is usable
     *
     * @param player   the player who wants to use the toolCard
     * @param toolCard the toolCard chosen to use
     * @return true if the toolCard is usable
     */
    @Override
    public boolean useCard(Player player, ToolCard toolCard) {
        return player.isCardUsable(toolCard);
    }

    /**
     * Release the execution of the tool card
     */
    @Override
    public void releaseToolCardExecution() {
        turnState.setPlayerState(new SelectActionState(turnState));
    }

}
