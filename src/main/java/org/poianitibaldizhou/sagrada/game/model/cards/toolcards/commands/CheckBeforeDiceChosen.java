package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;

import java.rmi.RemoteException;
import java.util.Objects;

public class CheckBeforeDiceChosen implements ICommand {

    /**
     * Check if the player has already placed a dice or not
     *
     * @param player player that invoked the ToolCard
     * @param toolCardExecutor executorHelper that contains this command
     * @param game game in which the player acts
     * @return always the MAIN flow of the treeFlow
     * @throws RemoteException RMI connection error
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException, ExecutionCommandException {
        TurnState turnState = (TurnState) game.getState();
        if(turnState.hasActionUsed(new PlaceDiceAction()))
            throw new ExecutionCommandException();
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CheckBeforeDiceChosen;
    }

    @Override
    public int hashCode() {
        return Objects.hash(CheckBeforeDiceChosen.class);
    }
}
