package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.game.model.Dice;

import java.net.Socket;

public class ProxyGameController extends ProxyController implements IGameController{

    /**
     * Constructor.
     * Create a proxy client controller to have transparency of the socket connection
     *
     * @param socket the socket connected with the server
     */
    public ProxyGameController(Socket socket) {
        super(socket);
    }

    @Override
    public void setDice(Dice d) {

    }
}
