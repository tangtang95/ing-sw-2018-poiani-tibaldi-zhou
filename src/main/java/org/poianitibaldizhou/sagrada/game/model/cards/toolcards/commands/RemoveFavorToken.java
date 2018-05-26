package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.Objects;

public class RemoveFavorToken implements ICommand{

    private int numberOfTokenToRemove;

    public RemoveFavorToken(int cost) {
        numberOfTokenToRemove = cost;
    }

    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        player.removeCoins(numberOfTokenToRemove);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof RemoveFavorToken))
            return false;
        return numberOfTokenToRemove == ((RemoveFavorToken) obj).numberOfTokenToRemove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RemoveFavorToken.class, numberOfTokenToRemove);
    }
}
