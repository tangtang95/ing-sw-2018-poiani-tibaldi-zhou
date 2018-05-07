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
    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException, InterruptedException {
        Dice dice = toolCard.getNeededDice(), roundTrackDice;
        List<IToolCardObserver> observerList = toolCard.getObservers();
        List<Dice> diceList = new ArrayList<>();
        RoundTrack roundTrack = game.getRoundTrack();
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            diceList.addAll(roundTrack.getDices(i)); // TODO check if round starts from 0 and go to 9 or 1 to 10
        }

        for(IToolCardObserver observer : observerList) {
            observer.notifyNeedDice(player, diceList);
        }

        roundTrackDice = toolCard.getNeededDice();
        // needs better implementation i guess, because number of round is needed to do this also
        game.getDraftPool().addDice(roundTrackDice);
        try {
            game.getDraftPool().useDice(dice);
        } catch (DiceNotFoundException e) {
            e.printStackTrace();
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }
        // now change the round in an appropriate way
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof SwapDiceWithRoundTrack;
    }
}
