package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

public class ClearAll implements ICommand {
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) {
        toolCardExecutor.setNeededDice(null);
        toolCardExecutor.setNeededPosition(null);
        toolCardExecutor.setTurnEnded(false);
        toolCardExecutor.setNeededColor(null);
        toolCardExecutor.setNeededValue(null);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ClearAll;
    }
}
