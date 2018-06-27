package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;


public interface ExecutorEvent {
    /**
     * Set the value inside the ExecutorEvent to the executor
     *
     * @param executor the toolCardExecutor of the current turnState in game
     */
    void setNeededValue(ToolCardExecutor executor);
}
