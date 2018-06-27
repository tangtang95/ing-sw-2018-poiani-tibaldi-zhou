package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.poianitibaldizhou.sagrada.game.model.Color;

/**
 * OVERVIEW: Represents the event that contains a color that is needed in the execution of a tool card.
 */
public class ColorExecutorEvent implements ExecutorEvent {

    private final Color color;

    /**
     * Constructor.
     * Creates an color event with a specific color
     * @param color color that will be set to the executor.
     */
    public ColorExecutorEvent(Color color){
        this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeededValue(ToolCardExecutor executor) {
        executor.setNeededColor(color);
    }
}
