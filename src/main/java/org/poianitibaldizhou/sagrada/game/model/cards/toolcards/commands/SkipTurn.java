package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;

import java.util.List;

public class SkipTurn implements ICommand {

    private final int value;

    public SkipTurn(int i) {
        this.value = i;
    }

    @Override
    public void executeCommand(Player player, List<IToolCardObserver> observers) {

    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof SkipTurn))
            return false;

        SkipTurn obj = (SkipTurn) object;
        return obj.getValue() == this.value;
    }
}
