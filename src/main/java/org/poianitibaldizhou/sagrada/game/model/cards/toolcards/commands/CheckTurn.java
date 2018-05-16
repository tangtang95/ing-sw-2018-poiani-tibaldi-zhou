package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

public class CheckTurn implements ICommand {
    private final int turn;

    public CheckTurn(int turn) {
        if(turn < 1 || turn > 2)
            throw new IllegalArgumentException("illegal value of turn");
        this.turn = turn;
    }

    /**
     * Check if the turn is first turn or second turn
     *
     * @param player player that invoked the ToolCard
     * @param toolCardExecutor executorHelper that contains this command
     * @param game game in which the player acts
     * @return true if the number of turn matches with this.turn, otherwise false
     * @throws RemoteException RMI connection error
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException, ExecutionCommandException {
        TurnState turnState = (TurnState) game.getState();
        if(turnState.isFirstTurn() != (getTurn() == 1))
            throw new ExecutionCommandException();
        return CommandFlow.MAIN;
    }

    public int getTurn() {
        return turn;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof CheckTurn))
            return false;
        CheckTurn obj = (CheckTurn) object;
        return this.turn == obj.getTurn();
    }
}