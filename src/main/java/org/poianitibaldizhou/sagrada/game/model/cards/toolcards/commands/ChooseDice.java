package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChooseDice implements ICommand {

    /**
     * Notify to clients that player needs to choose a dice.
     * @param player player who needs to choose a dice
     * @param toolCard toolCard that generated this effect
     * @param game
     **/
    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException {
        List<IToolCardObserver> observerList = toolCard.getObservers();
        for(IToolCardObserver obs : observerList)
            obs.notifyNeedDice(player, new ArrayList<Dice>());
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ChooseDice;
    }
}
