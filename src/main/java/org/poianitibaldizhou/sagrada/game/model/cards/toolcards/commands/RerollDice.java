package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;

import java.util.List;

public class RerollDice implements ICommand {
    @Override
    public void executeCommand(Player player, List<IToolCardObserver> observers) {

    }

    @Override
    public boolean equals(Object object) {
        return object instanceof RerollDice;
    }
}
