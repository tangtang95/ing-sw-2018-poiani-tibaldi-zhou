package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;

public class CheckBeforeDiceChose implements ICommand {
    @Override
    public void executeCommand(Player player) {

    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CheckBeforeDiceChose?true:false;
    }
}
