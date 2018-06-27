package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

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
        if(turnState.getToolCardExecutor().isExecutingCommands()) {
            Thread thread = new Thread(() -> {
                try {
                    turnState.getToolCardExecutor().waitToolCardExecutionEnd();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                turnState.notifyOnEndTurnState();
                turnState.nextTurn();
            });
            thread.start();
        }
        else {
            turnState.notifyOnEndTurnState();
            turnState.nextTurn();
        }
    }
}
