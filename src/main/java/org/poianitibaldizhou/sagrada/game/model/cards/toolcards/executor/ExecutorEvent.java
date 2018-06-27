package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

/**
 * OVERVIEW: Represents a event that can be fired from the players during the execution of the tool card.
 * This means that the concrete implementation of this will contains a certain value of a specific type (e.g Integer,
 * Color...) that will be set to the executor when the event is fired.
 */
public interface ExecutorEvent {
    /**
     * Set the value inside the ExecutorEvent to the executor
     *
     * @param executor the toolCardExecutor of the current turnState in game
     */
    void setNeededValue(ToolCardExecutor executor);
}
