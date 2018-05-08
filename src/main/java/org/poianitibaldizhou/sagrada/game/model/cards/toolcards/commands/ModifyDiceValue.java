package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;
import java.util.List;

public class ModifyDiceValue implements ICommand {

    /**
     * Notify that a new value for a certain dice is needed.
     * Doesn't require anything in toolcard. It requires a dice in toolcard
     * It will modify it's value and puts the new Dice in toolcard.
     *
     * @param player Player who invoked the ToolCard
     * @param toolCard ToolCard invoked that contains this command
     * @param game Game in which the player acts
     * @return true
     * @throws RemoteException if there are network communication errors
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException, InterruptedException {
        List<IToolCardObserver> observerList = toolCard.getObservers();
        for(IToolCardObserver obs : observerList){
            obs.notifyNeedNewValue(player);
        }
        Dice dice = toolCard.getNeededDice();
        Integer integer = toolCard.getNeededValue();
        toolCard.setNeededDice(new Dice(integer, dice.getColor()));
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ModifyDiceValue;
    }
}
