package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.view.IGameView;

import java.rmi.RemoteException;

public class ProxyGameController extends ProxyController implements IGameController{


    /**
     * Constructor.
     * Create a proxy client controller to have transparency of the socket connection
     *
     * @param serverHandler the serverHandler connected with the server
     */
    public ProxyGameController(ServerHandler serverHandler) {
        super(serverHandler);
    }

    @Override
    public void joinGame(Game game, IGameView view, String token) throws RemoteException {
        // TODO
    }

    @Override
    public void setDice(Dice dice, Game game, ToolCard toolCard) throws RemoteException {
        //TODO
    }
}
