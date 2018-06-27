package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;

import java.util.Objects;

/**
 * OVERVIEW: Command necessary for asking for an answer from the client.
 */
public class IfContinue implements ICommand {

    /**
     * This command notify the clients to send a boolean value representing the fact of
     * continuing the execution of the tool card or not.
     * Doesn't require any parameter set in the executor.
     *
     * @param player           players that executed the command
     * @param toolCardExecutor tool card executor that executed this command
     * @param turnState        turn in which the execution happens
     * @return CommandFlow.MAIN if the answer is true, CommandFlow.SUB if false
     * @throws InterruptedException due to wait call in the tool card executor for retrieving
     *                              the response
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        toolCardExecutor.getObservers().forEach(IToolCardExecutorFakeObserver::notifyNeedContinueAnswer);

        boolean answer = toolCardExecutor.getNeededAnswer();
        return answer ? CommandFlow.MAIN : CommandFlow.SUB;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IfContinue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(IfContinue.class);
    }
}
