package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.game.model.Dice;

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
    public void setDice(Dice d) {

    }
}
