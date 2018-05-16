package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.util.Objects;

public class PourOverDice implements ICommand {

    /**
     * Pour overs a certain dice in the draftpool.
     * Needs a dice to pour over in toolcard, it pours it over and sets it to toolcard.
     *
     * @param player player's that used the toolcard
     * @param toolCardExecutor toolcard used
     * @param game game in which the player acts
     * @return true
     * @throws InterruptedException error with wait()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws InterruptedException {
        Dice chosenDice = toolCardExecutor.getNeededDice();
        toolCardExecutor.setNeededDice(chosenDice.pourOverDice());
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