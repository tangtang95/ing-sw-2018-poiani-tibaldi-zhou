package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * OVERVIEW: Represents the command of choosing a color from a dice present in the round track
 */
public class ChooseColorFromRoundTrack implements ICommand {

    /**
     * Notify that player needs to choose a color from the dices present in the round track. It gives them the list of
     * colors present in the current round track.
     * Doesn't require anything in ToolCard.
     *
     * @param player           player who invoked toolCard
     * @param toolCardExecutor toolCard invoked
     * @param turnState        state in which the player acts
     * @return CommandFlow.EMPTY_ROUND_TRACK if the RoundTrack doesn't contain any dice, CommandFlow.MAIN otherwise
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        List<IToolCardExecutorFakeObserver> observerList = toolCardExecutor.getObservers();
        RoundTrack roundTrack = toolCardExecutor.getTemporaryRoundTrack();
        if (roundTrack.isEmpty())
            return CommandFlow.EMPTY_ROUND_TRACK;
        Set<Color> colors = new HashSet<>();
        List<Dice> diceList;
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            diceList = roundTrack.getDices(i);
            for (Dice d : diceList)
                if (!colors.contains(d.getColor()))
                    colors.add(d.getColor());

        }

        observerList.forEach(obs -> obs.notifyNeedColor(colors));
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
