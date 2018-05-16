package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.omg.CORBA.Object;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.rmi.RemoteException;

public class ClearTurnEndCondition implements ICommand {
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) {
        toolCardExecutor.setTurnEnded(false);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        return o instanceof ClearTurnEndCondition;
    }
}
