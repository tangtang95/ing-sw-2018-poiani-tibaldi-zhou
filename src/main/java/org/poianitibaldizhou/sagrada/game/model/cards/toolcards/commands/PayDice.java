package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.List;

public class PayDice implements ICommand {

    private final Color color;

    public PayDice(Color color){
        this.color = color;
    }

    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws RemoteException, InterruptedException {
        List<Dice> diceList = toolCardExecutor.getTemporaryDraftPool().getDices(color);
        for (IToolCardExecutorObserver obs: toolCardExecutor.getObservers()) {
            obs.notifyNeedDice(diceList);
        }

        Dice dice = toolCardExecutor.getNeededDice();
        if(dice.getColor() != color)
            return CommandFlow.REPEAT;
        try {
            toolCardExecutor.getTemporaryDraftPool().useDice(dice);
        } catch (DiceNotFoundException e) {
            return CommandFlow.REPEAT;
        } catch (EmptyCollectionException e) {
            return CommandFlow.EMPTY_DRAFTPOOL;
        }
        return CommandFlow.MAIN;
    }
}
