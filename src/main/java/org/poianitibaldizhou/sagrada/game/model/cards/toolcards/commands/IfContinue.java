package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.Objects;

public class IfContinue implements ICommand {
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        toolCardExecutor.getObservers().forEach(obs -> obs.notifyNeedContinueAnswer());

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
