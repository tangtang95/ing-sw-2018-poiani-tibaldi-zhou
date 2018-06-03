package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.IGame;
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

    public GameManager getGameManager(){
        return gameManager;
    }

    public LobbyManager getLobbyManager(){
        return lobbyManager;
    }

    public String createMultiPlayerGame(List<User> users)  {
        String gameName = UUID.randomUUID().toString();
        IGame game = new MultiPlayerGame(gameName, users);
        gameManager.addGame(game, gameName);
        return gameName;
    }

}
