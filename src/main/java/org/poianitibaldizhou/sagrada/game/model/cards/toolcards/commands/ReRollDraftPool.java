package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

/**
 * OVERVIEW: Commands for re-rolling every dice present in the draft pool
 */
public class ReRollDraftPool implements ICommand {

    /**
     * Re-roll every dice presents in the DraftPool.
     * Doesn't require anything and doesn't change anything from and in tool card.
     *
     * @param player           player that invoked the ToolCard containing this command
     * @param toolCardExecutor ToolCard that used this command
     * @param turnState        state in which the player acts
     * @return CommandFlow.MAIN
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        toolCardExecutor.getTemporaryDraftPool().reRollDices();

        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ReRollDraftPool;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ReRollDraftPool.class);
    }
}
