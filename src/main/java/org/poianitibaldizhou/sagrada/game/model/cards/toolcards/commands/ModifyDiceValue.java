package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

public class ModifyDiceValue implements ICommand {

    private final int value;

    public ModifyDiceValue(int value) {
        this.value = value;
    }

    @Override
    public void executeCommand(Player player) {

    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof ModifyDiceValue))
            return false;

        ModifyDiceValue obj = (ModifyDiceValue) object;
        return this.value == obj.getValue()?true:false;
    }
}
