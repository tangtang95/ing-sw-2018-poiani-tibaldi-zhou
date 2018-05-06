package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;
import java.util.List;

public class PourOverDice implements ICommand {
    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException, InterruptedException {
        while(toolCard.getDice() == null)
            wait();
        Dice chosenDice = toolCard.getDice();

        DraftPool draftPool = game.getDraftPool();
        List<Dice> draftPoolDices = draftPool.getDices();

        for(Dice d : draftPoolDices)
            if(d.equals(chosenDice)) {
                draftPool.addDice(chosenDice.pourOverDice());
                try {
                    draftPool.useDice(d);
                } catch (DiceNotFoundException e) {
                    e.printStackTrace();
                } catch (EmptyCollectionException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof PourOverDice;
    }
}