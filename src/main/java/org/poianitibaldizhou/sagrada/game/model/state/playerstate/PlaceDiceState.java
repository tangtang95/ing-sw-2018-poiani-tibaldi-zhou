package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public class PlaceDiceState implements IPlayerState {
    private TurnState turnState;

    PlaceDiceState(TurnState turnState) {
        this.turnState = turnState;
    }

    @Override
    public void chooseAction(String action) {
        if (turnState.getPlayerState() instanceof PlaceDiceState)
            throw new IllegalStateException();
    }

    @Override
    public void useCard(Player player, ToolCard toolCard) {
        if (turnState.getPlayerState() instanceof PlaceDiceState)
            throw new IllegalStateException();
    }

    @Override
    public void placeDice(Player player, Dice dice, int row, int column, TileConstraintType tileConstraint,
                          DiceConstraintType diceConstraint) {
        try {
            player.placeDice(dice,row,column,tileConstraint,diceConstraint);
        } catch (RuleViolationException e) {
            e.printStackTrace();
        }
        turnState.nextTurn();
    }

}
