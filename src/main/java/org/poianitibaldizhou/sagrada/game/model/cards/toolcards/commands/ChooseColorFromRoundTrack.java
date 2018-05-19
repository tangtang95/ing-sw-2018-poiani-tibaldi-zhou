package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class ChooseColorFromRoundTrack implements ICommand {

    /**
     * Notify that player needs to choose a color from the dices present in the round track
     * Doesn't require anything in ToolCard.
     *
     * @param player player who invoked toolCard
     * @param toolCardExecutor toolCard invoked
     * @param game Game on which the player acts
     * @throws RemoteException
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException {
        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        for(IToolCardExecutorObserver obs : observerList)
            obs.notifyNeedColor();
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ChooseColorFromRoundTrack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ChooseColorFromRoundTrack.class);
    }
}
