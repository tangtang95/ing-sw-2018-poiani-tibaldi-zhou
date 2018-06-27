package org.poianitibaldizhou.sagrada.network.strategycontroller;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;

/**
 * OVERVIEW: Represents the network strategy controller. It allows the clients to get the
 * controllers (and therefore using their action) independently w.r.t. the network communication
 * chosen, thus decoupling the view from the network.
 */
public interface NetworkStrategyController {

    /**
     * @return the lobby controller based on the strategy
     */
    ILobbyController getLobbyController();

    /**
     * @return the game controller based on the strategy
     */
    IGameController getGameController();

    /**
     * Close the connection
     */
    void close();
}
