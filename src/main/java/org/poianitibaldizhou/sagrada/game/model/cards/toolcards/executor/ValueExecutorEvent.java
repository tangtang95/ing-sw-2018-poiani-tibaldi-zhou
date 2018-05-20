package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

public class ValueExecutorEvent implements ExecutorEvent {

    private final int value;

    public ValueExecutorEvent(int value){
        this.value = value;
    }

    @Override
    public void setNeededValue(ToolCardExecutor executor) {
        executor.setNeededValue(value);
    }
}
