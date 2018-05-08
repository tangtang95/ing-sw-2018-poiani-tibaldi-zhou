package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Position;
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
    public void joinGame(Game game, IGameView view, String token, String gameName) throws RemoteException {
        // TODO
    }

    @Override
    public void setDice(Dice dice, String gameName, String toolCardName) throws RemoteException {
        //TODO
    }

    @Override
    public void setNewValue(int value, String gameName, String toolCardName) throws RemoteException {
        // TODO
    }

    @Override
    public void setColor(Color color, String gameName, String toolCardName) throws RemoteException {
        // TODO
    }

    @Override
    public void setPosition(Position position, String gameName, String toolCardName) throws RemoteException {
        // TODO
    }
}
