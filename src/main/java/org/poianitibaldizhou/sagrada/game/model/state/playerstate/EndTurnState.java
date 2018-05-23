package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EndTurnState extends IPlayerState {

    public EndTurnState(TurnState turnState) throws RemoteException {
        super(turnState);
        turnState.notifyOnEndTurnState();
    }

    /**
     * Notify to the toolCardExecutor that the turnState is going to endTurn and wait for the end of the
     * execution of the toolCard if necessary. At the end go to the nexTurn
     */
    @Override
    public void endTurn() throws RemoteException {
        turnState.getToolCardExecutor().setTurnEnded(true);
        try {
            turnState.getToolCardExecutor().waitToolCardExecutionEnd();
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().log(Level.INFO, "toolCardExecution ended");
            Thread.currentThread().interrupt();
        }
        turnState.nextTurn();
    }
}
