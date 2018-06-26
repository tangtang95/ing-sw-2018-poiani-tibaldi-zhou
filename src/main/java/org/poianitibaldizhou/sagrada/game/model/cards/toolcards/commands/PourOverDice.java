package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class PourOverDice implements ICommand {

    /**
     * Pour overs a certain dice.
     * Needs a dice to pour over in toolcard, it pours it over and sets it to toolcard.
     *
     * @param player           player's that used the toolcard
     * @param toolCardExecutor toolcard used
     * @param turnState        state in which the player acts
     * @return CommandFlow.MAIN
     * @throws InterruptedException error due to wait() in getting parameters from the executor
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        Dice chosenDice = toolCardExecutor.getNeededDice();
        toolCardExecutor.setNeededDice(chosenDice.pourOverDice());
        toolCardExecutor.getObservers().forEach(obs -> obs.notifyDicePouredOver(chosenDice.pourOverDice()));
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof PourOverDice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PourOverDice.class);
    }
}