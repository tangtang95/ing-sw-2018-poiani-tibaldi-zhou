package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;

public class RerollDice implements ICommand {
    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException {

    }

    @Override
    public boolean equals(Object object) {
        return object instanceof RerollDice;
    }
}
