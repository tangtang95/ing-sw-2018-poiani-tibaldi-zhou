package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;
import java.util.List;

public class ChooseColorFromRoundTrack implements ICommand {

    /**
     * Notify that player needs to choose a color from the dices present in the round track
     *
     * @param player player who invoked toolCard
     * @param toolCard toolCard invoked
     * @param game Game on which the player acts
     * @throws RemoteException
     */
    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException {
        List<IToolCardObserver> observerList = toolCard.getObservers();
        for(IToolCardObserver obs : observerList)
            obs.notifyNeedColor(player);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ChooseColorFromRoundTrack;
    }
}
