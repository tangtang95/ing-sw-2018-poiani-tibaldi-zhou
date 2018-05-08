package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutorHelper;

public class WaitTurnEnd implements ICommand {

    /**
     * Waits until the turn of the player ends.
     *
     * @param player player who invoked the tool card containing this command
     * @param toolCardExecutorHelper ToolCard invoked
     * @param game Game in which the player acts
     * @return true
     * @throws InterruptedException due to the wait
     */
    @Override
    public boolean executeCommand(Player player, ToolCardExecutorHelper toolCardExecutorHelper, Game game) throws InterruptedException {
        toolCardExecutorHelper.getTurnEnded();
        return true;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WaitTurnEnd? true : false;
    }
}
