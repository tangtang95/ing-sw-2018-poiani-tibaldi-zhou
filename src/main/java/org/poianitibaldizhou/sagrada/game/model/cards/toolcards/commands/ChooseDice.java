package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;

public class ChooseDice implements ICommand {

    @Override
    public void executeCommand(Player player) {
        // notify observer
        //for(IToolCardObserver obs :observers) {
        //    obs.notifyNeedDice();
        //}
        // Dice d = (Dice)stack.pop();

    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ChooseDice;
    }
}
