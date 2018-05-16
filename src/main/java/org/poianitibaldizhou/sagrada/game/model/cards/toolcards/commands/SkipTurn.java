package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

public class SkipTurn implements ICommand {

    private final int value;

    public SkipTurn(int i) {
        if(i != 1 && i != 2)
            throw new IllegalArgumentException();
        this.value = i;
    }

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
}
