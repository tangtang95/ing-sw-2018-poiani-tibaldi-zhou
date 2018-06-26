package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;

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
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        List<IToolCardExecutorFakeObserver> observerList = toolCardExecutor.getObservers();
        List<Dice> diceList = toolCardExecutor.getTemporaryDraftPool().getDices();
        observerList.forEach(obs -> obs.notifyNeedDice(diceList));
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
