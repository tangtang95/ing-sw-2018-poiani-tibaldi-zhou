package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

public class ModifyDiceValue implements ICommand {

    private int value;

    public ModifyDiceValue(int value) {
        this.value = value;
    }

    @Override
    public void executeCommand(Player player) {

    }
}
