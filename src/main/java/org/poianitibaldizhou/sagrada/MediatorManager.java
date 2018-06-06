package org.poianitibaldizhou.sagrada;

import org.jetbrains.annotations.TestOnly;
import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.MultiPlayerGame;
import org.poianitibaldizhou.sagrada.game.model.TerminationGameManager;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MediatorManager {

    private GameManager gameManager;
    private LobbyManager lobbyManager;

    public MediatorManager(){
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
        IGame game = new MultiPlayerGame(gameName, users, new TerminationGameManager(gameName, gameManager));
        gameManager.addGame(game, gameName);
        return gameName;
    }

    @TestOnly
    public String createMultiPlayerGameTest(List<User> users) {
        String gameName = "corderoGame";
        IGame game = new MultiPlayerGame(gameName, users, new TerminationGameManager(gameName, gameManager));
        gameManager.addGame(game, gameName);
        return gameName;
    }

    /**
     * Returns true if a player with this user name is already playing
     *
     * @param username username considered
     * @return true if a player with username is already playing in some game, false otherwise
     */
    public boolean isAlreadyPlayingAGame(String username) {
        for(IGame game : gameManager.getGameList()) {
            for(Player player : game.getPlayers()) {
                if(player.getUser().getName().equals(username))
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns true if a player with that username is already waiting in lobby
     *
     * @param username player's username
     * @return true if the player is present, false otherwise
     */
    public boolean isAlreadyWaitingInALobby(String username) {
        Optional<User> result = lobbyManager.getUsers().stream().filter(user -> user.getName().equals(username)).findAny();
        return result.isPresent();
    }
}
