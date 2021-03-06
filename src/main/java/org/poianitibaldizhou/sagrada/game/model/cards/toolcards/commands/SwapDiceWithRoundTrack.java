package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

/**
 * OVERVIEW: Command for swapping a dice from the draft pool with one present in the round
 * track
 */
public class SwapDiceWithRoundTrack implements ICommand {

    /**
     * Requires the player to choose a dice from the round track.
     * The dice will be swapped with a specified one from the draft pool.
     * It requires a dice in the tool card (dice to put in the round track) and it asks the client
     * for a dice and a round.
     *
     * @param player           player who invoked the command
     * @param toolCardExecutor invoked tool card
     * @param turnState        state in which the player acts
     * @return CommandFlow.REPEAT if the specified dice is not present in the draf pool, CommandFlow.EMPTY_DRAFT_POOL
     * if the draftpool is empty, CommandFlow.EMPTY_ROUND_TRACK if the round track is empty, CommandFlow.MAIN otherwise.
     * @throws InterruptedException due to the wait() in toolcard.getNeededDice() and toolcard.getNeededValue()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        Dice roundTrackDice;
        int round;

        RoundTrack roundTrack = toolCardExecutor.getTemporaryRoundTrack();

        if(roundTrack.isEmpty())
            return CommandFlow.EMPTY_ROUND_TRACK;

        toolCardExecutor.getObservers().forEach(observer -> observer.notifyNeedDiceFromRoundTrack(roundTrack));


        toolCardExecutor.setNeededDice(null);
        round = toolCardExecutor.getNeededValue();
        roundTrackDice = toolCardExecutor.getNeededDice();

        try {
            toolCardExecutor.getTemporaryDraftPool().useDice(dice);
            toolCardExecutor.getTemporaryDraftPool().addDice(roundTrackDice);
            roundTrack.swapDice(roundTrackDice, dice, round);
        } catch (DiceNotFoundException e) {
            return CommandFlow.REPEAT;
        } catch (EmptyCollectionException e) {
            return CommandFlow.EMPTY_DRAFT_POOL;
        }
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof SwapDiceWithRoundTrack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(SwapDiceWithRoundTrack.class);
    }
}
