package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public class EndTurnState extends IPlayerState {

    public EndTurnState(TurnState turnState) {
        super(turnState);
    }

    @Override
    public void endTurn() throws InvalidActionException {
        turnState.getToolCardExecutor().setTurnEnded(true);
        try {
            turnState.getToolCardExecutor().waitForTurnEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        turnState.nextTurn();
    }
}
