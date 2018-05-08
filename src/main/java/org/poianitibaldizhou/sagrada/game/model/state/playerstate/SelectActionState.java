package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

public class SelectActionState extends IPlayerState {

    public SelectActionState(TurnState turnState) {
        super(turnState);
    }

    /**
     * set the playerState
     *
     * @param action the operation of the player
     */
    @Override
    public void chooseAction(String action) {
        switch (action){
            case "useCard" :
                turnState.setPlayerState(new UseCardState(turnState));
                break;
            case "placeDice" :
                turnState.setPlayerState(new PlaceDiceState(turnState));
                break;
            case "endTurn" :
                turnState.nextTurn();
                break;
            default:
                throw new  IllegalArgumentException();
        }
    }

    @Override
    public void useCard(Player player, ToolCard toolCard, Game game) {
        if (turnState.getPlayerState() instanceof SelectActionState)
            throw new IllegalStateException();
    }

    @Override
    public void placeDice(Player player, Dice dice, int row, int column, TileConstraintType tileConstraint,
                          DiceConstraintType diceConstraint) {
        if (turnState.getPlayerState() instanceof SelectActionState)
            throw new IllegalStateException();
    }

}
