package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

/**
 * OVERVIEW: Represents the event that contains an integer that is needed in the execution of a tool card.
 */
public class ValueExecutorEvent implements ExecutorEvent {

    private final int value;

    /**
     * Constructor.
     * Creates an integer event with a specific color
     * @param value integer that will be set to the executor.
     */
    public ValueExecutorEvent(int value){
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeededValue(ToolCardExecutor executor) {
        executor.setNeededValue(value);
    }
}
