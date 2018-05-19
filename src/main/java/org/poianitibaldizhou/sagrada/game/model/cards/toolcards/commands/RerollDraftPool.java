package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import java.util.Objects;

public class RerollDraftPool implements ICommand {

    /**
     * Re-roll every dice presents in the DraftPool.
     * Doesn't require anything and doesn't change anything from and in toolcard.
     * @param player player that invoked the ToolCard containing this command
     * @param toolCardExecutor ToolCard that used this command
     * @param game game in which the player acts
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) {
        game.reRollDraftPool();
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof RerollDraftPool;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RerollDraftPool.class);
    }
}
