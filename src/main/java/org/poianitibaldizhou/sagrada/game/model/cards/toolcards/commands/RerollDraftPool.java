package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutorHelper;

import java.util.Random;

public class RerollDraftPool implements ICommand {

    /**
     * Re-roll every dice presents in the DraftPool.
     * Doesn't require anything and doesn't change anything from and in toolcard.
     *  @param player player that invoked the ToolCard containing this command
     * @param toolCardExecutorHelper ToolCard that used this command
     * @param game game in which the player acts
     */
    @Override
    public boolean executeCommand(Player player, ToolCardExecutorHelper toolCardExecutorHelper, Game game) {
        game.reRollDraftPool();
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof RerollDraftPool;
    }
}
