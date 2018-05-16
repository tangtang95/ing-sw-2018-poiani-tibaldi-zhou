package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.rmi.RemoteException;
import java.util.List;

public class ChooseDice implements ICommand {

    /**
     * Notify to clients that player needs to choose a dice.
     * Doesn't require anything in toolcard
     * @param player player who needs to choose a dice
     * @param toolCardExecutor toolCard that generated this effect
     * @param game
     **/
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException {
        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        List<Dice> diceList = game.getDraftPool().getDices();
        for(IToolCardExecutorObserver obs : observerList)
            obs.notifyNeedDice(diceList);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ChooseDice;
    }
}
