package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class WaitTurnEnd implements ICommand {

    /**
     * Waits until the turn of the player ends.
     *
     * @param player           player who invoked the tool card containing this command
     * @param toolCardExecutor ToolCard invoked
     * @param turnState        turn in which the player acts
     * @return CommandFlow.Main
     * @throws InterruptedException due to the wait() in waiting the end of the turn
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        turnState.releaseToolCardExecution();
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
