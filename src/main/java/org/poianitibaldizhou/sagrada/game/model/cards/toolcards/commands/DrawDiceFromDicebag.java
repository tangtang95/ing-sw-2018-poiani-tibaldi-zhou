package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;

public class DrawDiceFromDicebag implements ICommand {

    /**
     * Draws a dice from the DiceBag.
     *
     * @param player player who invoked the ToolCard
     * @param toolCard ToolCard invoked that contains this command
     * @param game Game in which the player acts
     * @throws RemoteException
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException {
        try {
            Dice dice = game.getDiceBag().draw();
            toolCard.setNeededDice(dice);
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof DrawDiceFromDicebag;
    }
}
