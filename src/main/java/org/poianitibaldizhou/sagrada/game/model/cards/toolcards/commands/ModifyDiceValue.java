package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;
import java.util.List;

public class ModifyDiceValue implements ICommand {

    private final int value;

    public ModifyDiceValue(int value) {
        this.value = value;
    }

    /**
     * Notify that a player needs to modify the value of a certain dice by a certain quantity.
     * This method won't pop the dice from toolcard.
     *
     * @param player Player who invoked toolcard
     * @param toolCard ToolCard invoked that contains this command
     * @param game Game in which the player acts
     * @throws RemoteException network communication error
     * @throws InterruptedException due to the wait() in  toolCard.getDice()
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException, InterruptedException {
        Dice dice = toolCard.getNeededDice();
        toolCard.setNeededDice(dice);

        List<IToolCardObserver> list = toolCard.getObservers();
        for(IToolCardObserver obs : list)
            obs.notifyNeedNewDeltaForDice(dice.getNumber(), value);
        return true;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof ModifyDiceValue))
            return false;

        ModifyDiceValue obj = (ModifyDiceValue) object;
        return this.value == obj.getValue();
    }
}
