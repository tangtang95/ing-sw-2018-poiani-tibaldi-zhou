package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;

public interface StrategyController {
    ILobbyController getLobbyController();
    IGameController getGameController();
}
