package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;

public class ControllerManager{

    private IGameController gameController;
    private ILobbyController lobbyController;

    public ControllerManager(IGameController gameController, ILobbyController lobbyController){
        this.gameController = gameController;
        this.lobbyController = lobbyController;
    }

    public IGameController getGameController() {
        return gameController;
    }

    public ILobbyController getLobbyController() {
        return lobbyController;
    }
}
