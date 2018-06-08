package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EndTurnState extends IPlayerState {

    public EndTurnState(TurnState turnState) {
        super(turnState);
    }

    /**
     * Notify to the toolCardExecutor that the turnState is going to endTurn and wait for the end of the
     * execution of the toolCard if necessary. At the end go to the nextTurn
     */
    @Override
    public void endTurn() {
        turnState.getToolCardExecutor().setTurnEnded(true);
        Thread thread = new Thread(()->{
            try {
                turnState.getToolCardExecutor().waitToolCardExecutionEnd();
            } catch (InterruptedException e) {
                Logger.getAnonymousLogger().log(Level.INFO, "toolCardExecution ended");
                Thread.currentThread().interrupt();
            }

            turnState.notifyOnEndTurnState();
            turnState.nextTurn();
        });
        thread.start();
    }
}
