package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

/**
 * OVERVIEW: In this state the player places a dice
 */
public class PlaceDiceState extends IPlayerState {

    /**
     * Constructor.
     * Creates the place dice state for the player. This needs the general turn state of the state machine
     * of the game.
     *
     * @param turnState game turn state in which the placement occurs
     */
    public PlaceDiceState(TurnState turnState) {
        super(turnState);
        turnState.notifyOnPlaceDiceState();
    }

    /**
     * Place the dice on the schemaCard of the player and then return the state to the SelectAction
     *
     * @param player   the player that wants to place a dice on his schemaCard
     * @param dice     the dice to place
     * @param position the position that the dice needs to be placed
     * @throws RuleViolationException if the placement of the dice on the schemaCard of the player is unsuccessful
     */
    @Override
    public void placeDice(Player player, Dice dice, Position position) throws RuleViolationException {
        turnState.setPlayerState(new SelectActionState(turnState));
        player.placeDice(dice, position);
    }

}
