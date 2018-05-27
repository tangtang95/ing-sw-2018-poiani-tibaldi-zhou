package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.poianitibaldizhou.sagrada.game.model.Dice;

public class DiceExecutorEvent implements ExecutorEvent {

    private final Dice dice;

    public DiceExecutorEvent(Dice dice){
        this.dice = dice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeededValue(ToolCardExecutor executor) {
        executor.setNeededDice(dice);
    }
}
