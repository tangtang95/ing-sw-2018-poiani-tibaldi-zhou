package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;


public abstract class IPlayerState {

    protected TurnState turnState;

    protected IPlayerState(TurnState turnState) {
        this.turnState = turnState;
    }

    public void chooseAction(String action){
        throw new IllegalStateException();
    }

    public void useCard(Player player, ToolCard toolCard, Game game){
        throw new IllegalStateException();
    }

    public void placeDice(Player player, Dice dice, int row, int column, TileConstraintType tileConstraint,
                   DiceConstraintType diceConstraint){
        throw new IllegalStateException();
    }
}
