package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutorHelper;

import java.rmi.RemoteException;

public class RemoveDiceFromDraftPool implements ICommand {

    /**
     * It requires a dice in the toolcard and removes it from the toolcard.
     *
     * @param player player who invoked the toolcard
     * @param toolCardExecutorHelper toolcard invoked
     * @param game game in which the player acts
     * @return false if dice is not present in the DraftPool, true otherwise
     * @throws RemoteException network communication error
     * @throws InterruptedException due to wait()
     */
    @Override
    public boolean executeCommand(Player player, ToolCardExecutorHelper toolCardExecutorHelper, Game game) throws RemoteException, InterruptedException {
        Dice dice = toolCardExecutorHelper.getNeededDice();
        try {
            game.useDraftPoolDice(dice);
        } catch (DiceNotFoundException | EmptyCollectionException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RemoveDiceFromDraftPool? true:false;
    }
}
