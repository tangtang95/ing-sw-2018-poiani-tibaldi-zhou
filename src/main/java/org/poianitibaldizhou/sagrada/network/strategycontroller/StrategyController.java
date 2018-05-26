package org.poianitibaldizhou.sagrada.network.strategycontroller;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;

public interface StrategyController {

    /**
     * @return the lobby controller based on the strategy
     */
    ILobbyController getLobbyController();

    /**
     * @return the game controller based on the strategy
     */
    IGameController getGameController();

    void close();
}
