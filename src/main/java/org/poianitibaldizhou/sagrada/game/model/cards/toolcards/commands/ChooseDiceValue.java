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
     *
     * @param player Player who invoked the ToolCard
     * @param toolCard ToolCard invoked that contains this command
     * @param game Game in which the player acts
     * @throws RemoteException if there are network communication errors
     */
    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException {
        List<IToolCardObserver> observerList = toolCard.getObservers();
        for(IToolCardObserver obs : observerList){
            obs.notifyNeedNewValue(player);
        }
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ChooseDiceValue;
    }
}
