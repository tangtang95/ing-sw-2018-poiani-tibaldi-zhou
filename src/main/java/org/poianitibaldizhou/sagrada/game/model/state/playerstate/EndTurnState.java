package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public class EndTurnState implements IPlayerState {
    private TurnState turnState;

    EndTurnState(TurnState turnState) {
        this.turnState = turnState;
    }

    @Override
    public void chooseAction(String action) {
        if (action.equals("endTurn"))
            turnState.nextTurn();
        throw new IllegalStateException();
    }

    @Override
    public void useCard(Player player, ToolCard toolCard) {
        if (turnState.getPlayerState() instanceof EndTurnState)
            throw new IllegalStateException();
    }

    @Override
    public void placeDice(Player player, Dice dice, int row, int column, TileConstraintType tileConstraint,
                          DiceConstraintType diceConstraint) {
        if (turnState.getPlayerState() instanceof EndTurnState)
            throw new IllegalStateException();
    }

}
