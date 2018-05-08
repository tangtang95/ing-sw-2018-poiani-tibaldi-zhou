package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;
import java.util.List;

public class ChooseDiceValue implements ICommand {

    /**
     * Notify that a new value for a certain dice is needed.
     * Doesn't require anything in toolcard.
     *
     * @param player Player who invoked the ToolCard
     * @param toolCard ToolCard invoked that contains this command
     * @param game Game in which the player acts
     * @return true
     * @throws RemoteException if there are network communication errors
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException {
        List<IToolCardObserver> observerList = toolCard.getObservers();
        for(IToolCardObserver obs : observerList){
            obs.notifyNeedNewValue(player);
        }
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ChooseDiceValue;
    }
}
