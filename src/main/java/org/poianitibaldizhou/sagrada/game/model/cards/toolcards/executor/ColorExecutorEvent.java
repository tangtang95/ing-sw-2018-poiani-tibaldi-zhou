package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.poianitibaldizhou.sagrada.game.model.Color;

public class ColorExecutorEvent implements ExecutorEvent {

    private final Color color;

    public ColorExecutorEvent(Color color){
        this.color = color;
    }

    @Override
    public void setNeededValue(ToolCardExecutor executor) {
        executor.setNeededColor(color);
    }
}
