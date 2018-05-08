package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutorHelper;

import java.rmi.RemoteException;

public interface ICommand {
    boolean executeCommand(Player player, ToolCardExecutorHelper toolCardExecutorHelper, Game game) throws RemoteException, InterruptedException;
}
