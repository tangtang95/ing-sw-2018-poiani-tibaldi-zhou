package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DisconnectedException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class AddDiceToDraftPool implements ICommand {

    /**
     * Add the ToolCard's dice to the DraftPool.
     * This method requires a dice in ToolCard, and leaves it there.
     *
     * @param player player that invoked the ToolCard
     * @param toolCardExecutor ToolCard invoked that contains this command
     * @param turnState the state of the game
     * @return CommandFlow.MAIN always
     * @throws InterruptedException given by wait of toolCard.getNeededDice()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        try {
            toolCardExecutor.getTemporaryDraftPool().addDice(dice);
        } catch (DisconnectedException e) {
            e.printStackTrace();
        }
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof AddDiceToDraftPool;
    }

    @Override
    public int hashCode() {
        return Objects.hash(AddDiceToDraftPool.class);
    }
}
