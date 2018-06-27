package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class RemoveDiceFromDraftPool implements ICommand {

    /**
     * It requires a dice in the toolcard and removes it from the toolcard.
     *
     * @param player           player who invoked the toolcard
     * @param toolCardExecutor toolcard invoked
     * @param turnState        state in which the player acts
     * @return CommandFlow.NOT_DICE_IN_DRAFT_POOL if the dice isn't present in DraftPool, CommandFlow_EMPTY_DRAFTPOOL if
     * the draftpool is empty, CommandFlow.MAIN otherwise
     * @throws InterruptedException due to wait()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        try {
            toolCardExecutor.getTemporaryDraftPool().useDice(dice);
        } catch (DiceNotFoundException e) {
            return CommandFlow.NOT_DICE_IN_DRAFT_POOL;
        } catch (EmptyCollectionException e) {
            return CommandFlow.EMPTY_DRAFT_POOL;
        }
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RemoveDiceFromDraftPool;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RemoveDiceFromDraftPool.class);
    }
}
