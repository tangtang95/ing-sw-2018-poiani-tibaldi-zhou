package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import java.rmi.RemoteException;
import java.util.Objects;

public class DrawDiceFromDicebag implements ICommand {

    /**
     * Draws a dice from the DiceBag.
     * Doesn't require anything in toolcard.
     * It pushes a dice to toolcard.
     *
     * @param player                 player who invoked the ToolCard
     * @param toolCardExecutor ToolCard invoked that contains this command
     * @param game                   Game in which the player acts
     * @throws RemoteException
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException {
        Dice dice = game.getDiceFromDiceBag();
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
