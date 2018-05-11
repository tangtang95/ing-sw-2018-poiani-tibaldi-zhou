package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

public class UseCardState extends IPlayerState {

    public UseCardState(TurnState turnState) {
        super(turnState);
    }

    @Override
    public void useCard(Player player, ToolCard toolCard, Game game) throws NoCoinsExpendableException {
        try {
            player.useCard(toolCard, game);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        turnState.setPlayerState(new SelectActionState(turnState));
    }


}
