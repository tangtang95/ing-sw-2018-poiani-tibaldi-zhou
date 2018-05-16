package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.Objects;

public class SkipTurn implements ICommand {

    private final int value;

    public SkipTurn(int i) {
        this.value = i;
    }

    /**
     * Add skip turn to the player that invoked the ToolCard
     *
     * @param player player that invoked the ToolCard
     * @param toolCardExecutor executorHelper that contains this command
     * @param game game in which the player acts
     * @return always the MAIN flow of the treeFlow
     * @throws RemoteException RMI connection error
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException {
        TurnState turnState = (TurnState) game.getState();
        turnState.addSkipTurnPlayer(player, value);
        return CommandFlow.MAIN;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof SkipTurn))
            return false;

        SkipTurn obj = (SkipTurn) object;
        return obj.getValue() == this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(SkipTurn.class, getValue());
    }
}
