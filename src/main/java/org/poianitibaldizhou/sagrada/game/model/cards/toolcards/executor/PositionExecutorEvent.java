package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.poianitibaldizhou.sagrada.game.model.Position;

public class PositionExecutorEvent implements ExecutorEvent {

    private final Position position;

    public PositionExecutorEvent(Position position){
        this.position = position;
    }

    @Override
    public void setNeededValue(ToolCardExecutor executor) {
        executor.setNeededPosition(position);
    }
}
