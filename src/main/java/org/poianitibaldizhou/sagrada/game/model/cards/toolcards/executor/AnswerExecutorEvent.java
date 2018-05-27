package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

public class AnswerExecutorEvent implements ExecutorEvent{

    private final boolean answer;

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
