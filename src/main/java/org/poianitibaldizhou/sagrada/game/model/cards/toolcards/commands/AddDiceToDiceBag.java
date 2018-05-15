package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

public class AddDiceToDiceBag implements ICommand {

    /**
     * Adds the toolCard's dice to game's DiceBag.
     * This methods expects a dice in ToolCard.
     *
     * @param player Player who invoked the command
     * @param toolCardExecutor toolCard that has been invoked and that contains this command
     * @param game Game in which the player acts
     * @return true
     * @throws InterruptedException due to the wait for toolCard.getNeededDice()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        game.addDiceToDiceBag(dice);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof AddDiceToDiceBag;
    }
}
