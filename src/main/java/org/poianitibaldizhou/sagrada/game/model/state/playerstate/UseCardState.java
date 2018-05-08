package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

public class UseCardState extends IPlayerState {

    public UseCardState(TurnState turnState) {
        super(turnState);
    }


    @Override
    public void useCard(Player player, ToolCard toolCard, Game game) {
        try {
            try {
                player.useCard(toolCard, game);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (NoCoinsExpendableException e) {
            e.printStackTrace();
        }

        turnState.nextTurn();
    }


}
