package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutorHelper;

import java.rmi.RemoteException;
import java.util.List;

public class SwapDiceWithRoundTrack implements ICommand {

    /**
     * Requires the player to choose a dice from the roundtrack.
     * The dice will be swapped with a specified one from the draftpool.
     * It requires a dice in the toolcard (dice to put in the roundtrack) and it asks the client
     * for a dice and a round.
     *
     * @param player player who invoked the command
     * @param toolCardExecutorHelper invoked toolcard
     * @param game game on which the player acts
     * @throws RemoteException network communication error
     * @throws InterruptedException due to the wait() in toolcard.getNeededDice() and toolcard.getNeededValue()
     */
    @Override
    public boolean executeCommand(Player player, ToolCardExecutorHelper toolCardExecutorHelper, Game game) throws RemoteException, InterruptedException {
        Dice dice = toolCardExecutorHelper.getNeededDice(), roundTrackDice;
        int round;
        List<IToolCardObserver> observerList = toolCardExecutorHelper.getObservers();
        RoundTrack roundTrack = game.getRoundTrack();

        for(IToolCardObserver observer : observerList) {
            observer.notifyNeedDiceFromRoundTrack(player, roundTrack);
        }

        roundTrackDice = toolCardExecutorHelper.getNeededDice();
        round = toolCardExecutorHelper.getNeededValue();

        game.getDraftPool().addDice(roundTrackDice);
        try {
            game.getDraftPool().useDice(dice);
        } catch (DiceNotFoundException | EmptyCollectionException e) {
            e.printStackTrace();
            return false;
        }
        game.removeDiceFromRoundTrack(round, roundTrackDice);
        game.addDiceToRoundTrack(dice, round);
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof SwapDiceWithRoundTrack;
    }
}
