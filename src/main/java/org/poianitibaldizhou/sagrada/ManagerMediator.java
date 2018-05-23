package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.MultiPlayerGame;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public class ManagerMediator {

    private GameManager gameManager;
    private LobbyManager lobbyManager;

    public ManagerMediator(){
        gameManager = new GameManager(this);
        lobbyManager = new LobbyManager(this);
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void setLobbyManager(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    public GameManager getGameManager(){
        return gameManager;
    }

    public LobbyManager getLobbyManager(){
        return lobbyManager;
    }

    public void createMultiPlayerGame(List<User> users) throws RemoteException {
        String gameName = UUID.randomUUID().toString();
        Game game= new MultiPlayerGame(gameName, users);
        gameManager.addGame(game, gameName);
    }

}
