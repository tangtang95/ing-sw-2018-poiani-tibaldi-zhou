package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class SwapDiceWithRoundTrack implements ICommand {

    /**
     * Requires the player to choose a dice from the roundtrack.
     * The dice will be swapped with a specified one from the draftpool.
     * It requires a dice in the toolcard (dice to put in the roundtrack) and it asks the client
     * for a dice and a round.
     *
     * @param player                 player who invoked the command
     * @param toolCardExecutor invoked toolcard
     * @param game                   game on which the player acts
     * @throws RemoteException      network communication error
     * @throws InterruptedException due to the wait() in toolcard.getNeededDice() and toolcard.getNeededValue()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException, InterruptedException, ExecutionCommandException {
        Dice dice = toolCardExecutor.getNeededDice();
        Dice roundTrackDice;
        int round;
        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        RoundTrack roundTrack = game.getRoundTrack();

        for (IToolCardExecutorObserver observer : observerList) {
            observer.notifyNeedDiceFromRoundTrack(roundTrack);
        }

        roundTrackDice = toolCardExecutor.getNeededDice();
        round = toolCardExecutor.getNeededValue();

        try {
            game.swapDraftPoolDice(dice, roundTrackDice);
            game.swapRoundTrackDice(roundTrackDice, dice, round);
        } catch (DiceNotFoundException e) {
            throw new ExecutionCommandException();
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
