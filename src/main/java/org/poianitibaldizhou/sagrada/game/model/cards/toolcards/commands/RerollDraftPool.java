package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.Objects;

public class RerollDraftPool implements ICommand {

    /**
     * Re-roll every dice presents in the DraftPool.
     * Doesn't require anything and doesn't change anything from and in toolcard.
     *
     * @param player           player that invoked the ToolCard containing this command
     * @param toolCardExecutor ToolCard that used this command
     * @param turnState        state in which the player acts
     * @return CommandFlow.MAIN
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws RemoteException {
        toolCardExecutor.getTemporaryDraftPool().reRollDices();
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
