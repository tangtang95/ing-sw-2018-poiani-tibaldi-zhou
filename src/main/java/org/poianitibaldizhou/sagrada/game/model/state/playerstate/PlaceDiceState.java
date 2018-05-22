package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public class PlaceDiceState extends IPlayerState {


    public PlaceDiceState(TurnState turnState) {
        super(turnState);
        turnState.notifyOnPlaceDiceState();
    }

    /**
     * Place the dice on the schemaCard of the player and then return the state to the SelectAction
     *
     * @param player the player that wants to place a dice on his schemaCard
     * @param dice the dice to place
     * @param position
     * @throws RuleViolationException
     */
    @Override
    public void placeDice(Player player, Dice dice, Position position) throws RuleViolationException {
        player.placeDice(dice, position);
        turnState.setPlayerState(new SelectActionState(turnState));
    }

}
