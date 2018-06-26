package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.Objects;

public class SwapDiceWithRoundTrack implements ICommand {

    /**
     * Requires the player to choose a dice from the roundtrack.
     * The dice will be swapped with a specified one from the draftpool.
     * It requires a dice in the toolcard (dice to put in the roundtrack) and it asks the client
     * for a dice and a round.
     *
     * @param player           player who invoked the command
     * @param toolCardExecutor invoked toolcard
     * @param turnState        state in which the player acts
     * @return CommandFlow.REPEAT if the specified dice is not present in the drafpool, CommandFlow.EMPTY_DRAFTPOOL
     * if the draftpool is empty, CommandFlow.EMPTY_ROUNDTRACK if the round track is empty, CommandFlow.MAIN otherwise.
     * @throws RemoteException      network communication error
     * @throws InterruptedException due to the wait() in toolcard.getNeededDice() and toolcard.getNeededValue()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        Dice roundTrackDice;
        int round;

        RoundTrack roundTrack = toolCardExecutor.getTemporaryRoundTrack();

        if(roundTrack.isEmpty())
            return CommandFlow.EMPTY_ROUNDTRACK;

        toolCardExecutor.getObservers().forEach(observer -> observer.notifyNeedDiceFromRoundTrack(roundTrack));


        toolCardExecutor.setNeededDice(null);
        round = toolCardExecutor.getNeededValue();
        roundTrackDice = toolCardExecutor.getNeededDice();

        try {
            toolCardExecutor.getTemporaryDraftPool().useDice(dice);
            toolCardExecutor.getTemporaryDraftPool().addDice(roundTrackDice);
            roundTrack.swapDice(roundTrackDice, dice, round);
        } catch (DiceNotFoundException e) {
            return CommandFlow.REPEAT;
        } catch (EmptyCollectionException e) {
            return CommandFlow.EMPTY_DRAFTPOOL;
        }
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof SwapDiceWithRoundTrack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(SwapDiceWithRoundTrack.class);
    }
}
