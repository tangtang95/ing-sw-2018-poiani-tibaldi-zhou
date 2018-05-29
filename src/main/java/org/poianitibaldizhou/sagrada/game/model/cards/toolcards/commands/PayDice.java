package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.DisconnectedException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class PayDice implements ICommand {

    private final Color color;

    public PayDice(Color color){
        this.color = color;
    }

    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        List<Dice> diceList = toolCardExecutor.getTemporaryDraftPool().getDices(color);
        toolCardExecutor.getObservers().forEach(obs -> {obs.notifyNeedDice(diceList);});

        Dice dice = toolCardExecutor.getNeededDice();
        if(!toolCardExecutor.getTemporaryDraftPool().getDices().contains(dice))
            return CommandFlow.NOT_DICE_IN_DRAFTPOOL;
        if(dice.getColor() != color)
            return CommandFlow.REPEAT;
        try {
            toolCardExecutor.getTemporaryDraftPool().useDice(dice);
        } catch (EmptyCollectionException | DiceNotFoundException e) {
            // Exception impossible to happen (already checked before)
            return CommandFlow.NOT_DICE_IN_DRAFTPOOL;
        } catch (DisconnectedException e) {
            e.printStackTrace();
        }
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PayDice))
            return false;
        PayDice other = (PayDice) obj;
        return other.color == this.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PayDice.class, this.color);
    }
}
