package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;

public class CheckDicePlaceble implements ICommand {
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException, InterruptedException {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CheckDicePlaceble? true:false;
    }
}
