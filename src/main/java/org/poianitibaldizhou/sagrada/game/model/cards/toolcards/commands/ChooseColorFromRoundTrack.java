package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.*;

public class ChooseColorFromRoundTrack implements ICommand {

    /**
     * Notify that player needs to choose a color from the dices present in the round track. It gives them the list of
     * colors present in the current roundtrack.
     * Doesn't require anything in ToolCard.
     *
     * @param player player who invoked toolCard
     * @param toolCardExecutor toolCard invoked
     * @param turnState state in which the player acts
     * @return CommandFlow.STOP if the RoundTrack doesn't contain any dice, CommandFlow.MAIN otherwise
     * @throws RemoteException communication architecture error
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws RemoteException {
        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        RoundTrack roundTrack = toolCardExecutor.getTemporaryRoundtrack();
        if(roundTrack.isEmpty())
            return CommandFlow.STOP;
        Set<Color> colors = new HashSet<>();
        List<Dice> diceList;
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            diceList = roundTrack.getDices(i);
            for(Dice d : diceList)
                if(!colors.contains(d.getColor()))
                    colors.add(d.getColor());

        }

        for(IToolCardExecutorObserver obs : observerList)
            obs.notifyNeedColor(colors);
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
