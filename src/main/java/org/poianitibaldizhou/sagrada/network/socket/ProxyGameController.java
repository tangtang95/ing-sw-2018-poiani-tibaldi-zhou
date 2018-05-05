package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.game.model.Dice;

public class ProxyGameController extends ProxyController implements IGameController{
    /**
     * Constructor.
     * Create a proxy client controller to have transparency of the socket connection
     *
     * @param ipAddress the IP address of the server
     * @param port      the port of the server on which is listening
     */
    public ProxyGameController(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public void setDice(Dice d) {

    }
}
