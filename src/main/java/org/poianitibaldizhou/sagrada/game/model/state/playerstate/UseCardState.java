package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public class UseCardState implements IPlayerState {
    private TurnState turnState;

    UseCardState(TurnState turnState) {
        this.turnState = turnState;
    }

    @Override
    public void chooseAction(String action) {
        if (turnState.getPlayerState() instanceof UseCardState)
            throw new IllegalStateException();
    }

    @Override
    public void useCard(Player player, ToolCard toolCard) {
        try {
            player.useCard(toolCard);
        } catch (NoCoinsExpendableException e) {
            e.printStackTrace();
        }

        turnState.nextTurn();
    }

    @Override
    public void placeDice(Player player, Dice dice, int row, int column, TileConstraintType tileConstraint,
                          DiceConstraintType diceConstraint) {
        if (turnState.getPlayerState() instanceof UseCardState)
            throw new IllegalStateException();
    }

}
