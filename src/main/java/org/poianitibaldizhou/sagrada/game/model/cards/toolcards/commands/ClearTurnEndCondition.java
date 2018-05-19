package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

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
