package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Random;

public class RerollDice implements ICommand {

    /**
     * Re-roll ToolCard's dice and set it to the ToolCard again for further uses.
     * It requires a dice in the toolcard set to the toolcard the re-rolled dice.
     *
     * @param player           player that invoked the ToolCard
     * @param toolCardExecutor ToolCard that contains this command
     * @param turnState        state in which the player acts
     * @return CommandFlow.MAIN
     * @throws InterruptedException due to wait() in getting parameters from the executor
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        Random rand = new Random();
        dice = new Dice(rand.nextInt(Dice.MAX_VALUE - 1) + 1, dice.getColor());
        toolCardExecutor.setNeededDice(dice);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof RerollDice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RerollDice.class);
    }
}
