package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

/**
 * OVERVIEW: Represents the event that contains an answer that is needed in the execution of a tool card.
 */
public class AnswerExecutorEvent implements ExecutorEvent{

    private final boolean answer;

    /**
     * Constructor.
     * Creates an answer event with a specific answer
     * @param answer answer that will be set to the executor.
     */
    public AnswerExecutorEvent(boolean answer){
        this.answer = answer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeededValue(ToolCardExecutor executor) {
        executor.setNeededAnswer(answer);
    }
}
