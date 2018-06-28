package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;

/**
 * OVERVIEW: Manages the controller server side. It has the references of the game controller
 * and the lobby controller.
 */
public class ControllerManager{

    private IGameController gameController;
    private ILobbyController lobbyController;

    /**
     * Creates a controller manager with a game controller and a lobby controller
     *
     * @param gameController game controller for Sagrada game
     * @param lobbyController  lobby controller for Sagrada game
     */
    public ControllerManager(IGameController gameController, ILobbyController lobbyController){
        this.gameController = gameController;
        this.lobbyController = lobbyController;
    }

    /**
     * @return Sagrada's game controller
     */
    public IGameController getGameController() {
        return gameController;
    }

    /**
     * @return Sagrada's lobby controller
     */
    public ILobbyController getLobbyController() {
        return lobbyController;
    }
}
