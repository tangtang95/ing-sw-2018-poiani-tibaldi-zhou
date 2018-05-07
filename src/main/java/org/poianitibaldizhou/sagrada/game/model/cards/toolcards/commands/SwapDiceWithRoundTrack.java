package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SwapDiceWithRoundTrack implements ICommand {

    /**
     * Requires the player to choose a dice from the roundtrack.
     * The dice will be swapped with a specified one from the draftpool.
     *
     *
     * @param player player who invoked the command
     * @param toolCard invoked toolcard
     * @param game game on which the player acts
     * @throws RemoteException network communication error
     * @throws InterruptedException due to the wait() in toolcard.getNeededDice() and toolcard.getNeededValue()
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException, InterruptedException {
        Dice dice = toolCard.getNeededDice(), roundTrackDice;
        int round;
        List<IToolCardObserver> observerList = toolCard.getObservers();
        List<Dice> diceList = new ArrayList<>();
        RoundTrack roundTrack = game.getRoundTrack();

        for(IToolCardObserver observer : observerList) {
            observer.notifyNeedDiceFromRoundTrack(player, roundTrack);
        }

        roundTrackDice = toolCard.getNeededDice();
        round = toolCard.getNeededValue();

        game.getDraftPool().addDice(roundTrackDice);
        try {
            game.getDraftPool().useDice(dice);
        } catch (DiceNotFoundException | EmptyCollectionException e) {
            e.printStackTrace();
            return false;
        }
        roundTrack.removeDiceFromRoundTrack(round,roundTrackDice);
        roundTrack.addDiceToRound(dice, round);
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof SwapDiceWithRoundTrack;
    }
}
