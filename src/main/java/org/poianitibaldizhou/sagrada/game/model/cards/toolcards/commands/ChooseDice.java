package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class ChooseDice implements ICommand {

    /**
     * * Notify to clients that player needs to choose a dice.
     * Doesn't require anything in toolcard
     * @param player player who needs to choose a dice
     * @param toolCardExecutor toolCard that generated this effect
     * @param turnState state in which the card is executed
     * @return CommandFlow.MAIN
     * @throws RemoteException due to network communication errors
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        List<Dice> diceList = toolCardExecutor.getTemporaryDraftPool().getDices();
        observerList.forEach(obs -> {
            try {
                obs.notifyNeedDice(diceList);
            } catch (RemoteException e) {
                observerList.remove(obs);
            }
        });
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ChooseDice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ChooseDice.class);
    }
}
