package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;

import java.rmi.RemoteException;
import java.util.Objects;

public class DrawDiceFromDicebag implements ICommand {

    /**
     * Draws a dice from the DiceBag.
     * Doesn't require anything in toolcard.
     * It pushes a dice to toolcard.
     *
     * @param player           player who invoked the ToolCard
     * @param toolCardExecutor ToolCard invoked that contains this command
     * @param stateGame        state in which the player acts
     * @return CommandFlow.STOP in the DiceBag is empty, CommandFlow.MAIN otherwise
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, IStateGame stateGame) {
        Dice dice = null;
        try {
            dice = toolCardExecutor.getTemporaryDicebag().draw();
        } catch (EmptyCollectionException e) {
            return CommandFlow.STOP;
        }
        toolCardExecutor.setNeededDice(dice);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof DrawDiceFromDicebag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(DrawDiceFromDicebag.class);
    }
}
