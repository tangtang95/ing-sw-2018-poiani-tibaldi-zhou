package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;

public interface IGame {

    public void fireExecutorEvent(ExecutorEvent event) throws InvalidActionException;


}
