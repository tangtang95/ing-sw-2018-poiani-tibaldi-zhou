package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.poianitibaldizhou.sagrada.game.model.cards.Position;

/**
 * OVERVIEW: Represents the event that contains a position that is needed in the execution of a tool card.
 */
public class PositionExecutorEvent implements ExecutorEvent {

    private final Position position;

    /**
     * Constructor.
     * Creates a position event with a specific color
     * @param position position that will be set to the executor.
     */
    public PositionExecutorEvent(Position position){
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeededValue(ToolCardExecutor executor) {
        executor.setNeededPosition(position);
    }
}
