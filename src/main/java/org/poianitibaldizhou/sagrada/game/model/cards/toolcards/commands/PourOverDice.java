package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;

public class PourOverDice implements ICommand {
    @Override
    public void executeCommand(Player player) {

    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof PourOverDice))
            return false;
        return true;
    }
}
