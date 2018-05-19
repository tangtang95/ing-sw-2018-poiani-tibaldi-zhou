package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import java.util.Objects;

public class WaitTurnEnd implements ICommand {

    /**
     * Waits until the turn of the player ends.
     *
     * @param player player who invoked the tool card containing this command
     * @param toolCardExecutor ToolCard invoked
     * @param game Game in which the player acts
     * @return true
     * @throws InterruptedException due to the wait
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws InterruptedException {
        game.releaseToolCardExecution();
        toolCardExecutor.waitForTurnEnd();
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WaitTurnEnd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(WaitTurnEnd.class);
    }
}
