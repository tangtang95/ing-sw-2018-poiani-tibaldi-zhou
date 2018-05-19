package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;

import java.rmi.RemoteException;
import java.util.Objects;

public class CheckTurnEnd implements ICommand {
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, IStateGame stateGame) throws RemoteException, InterruptedException {
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CheckTurnEnd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(CheckTurnEnd.class);
    }
}
