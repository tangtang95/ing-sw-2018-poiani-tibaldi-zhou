package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.Objects;

public class AddDiceToDiceBag implements ICommand {

    /**
     * Adds the toolCard's dice to game's DiceBag.
     * This methods expects a dice in ToolCard, and leaves it there.
     *
     * @param player Player who invoked the command
     * @param toolCardExecutor toolCard that has been invoked and that contains this command
     * @param turnState state of the game in which the toolcard is executed
     * @return Always CommandFlow.MAIN
     * @throws InterruptedException due to the wait for toolCard.getNeededDice()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        toolCardExecutor.getTemporaryDicebag().addElement(dice);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof AddDiceToDiceBag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(AddDiceToDiceBag.class);
    }
}
