package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;

/**
 * OVERVIEW: Represents the event that contains a dice that is needed in the execution of a tool card.
 */
public class DiceExecutorEvent implements ExecutorEvent {

    private final Dice dice;

    /**
     * Constructor.
     * Creates a dice event with a specific color
     * @param dice dice that will be set to the executor.
     */
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
