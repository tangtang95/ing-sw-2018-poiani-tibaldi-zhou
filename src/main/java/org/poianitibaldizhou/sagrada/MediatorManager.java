package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.MultiPlayerGame;
import org.poianitibaldizhou.sagrada.game.model.TerminationGameManager;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * OVERVIEW: Class the manages the communication between the game manager and the lobby manager. This has been implemented
 * for decoupling purposes.
 */
public class MediatorManager {

    private GameManager gameManager;
    private LobbyManager lobbyManager;

    /**
     * Constructor.
     * Creates a mediator manager that allows the communications between the game manager and the lobby manager
     */
    public MediatorManager() {
        gameManager = new GameManager(this);
        lobbyManager = new LobbyManager(this);
    }

    /**
     * @return server game manager
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * @return server lobby manager
     */
    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    /**
     * Creates a multi player game with the list of users who joined the lobby
     *
     * @param users list of users of the multi player game that will be created
     * @return name of the multi player game that has been created
     */
    public String createMultiPlayerGame(List<User> users) {
        String gameName = UUID.randomUUID().toString();
        IGame game = new MultiPlayerGame(gameName, users, new TerminationGameManager(gameName, gameManager));
        gameManager.createMultiPlayerGame(game, gameName);
        return gameName;
    }

    /**
     * Returns true if a player with this user name is already playing
     *
     * @param username username considered
     * @return true if a player with username is already playing in some game, false otherwise
     */
    public boolean isAlreadyPlayingAGame(String username) {
        for (IGame game : gameManager.getGameList()) {
            for (User user : game.getUsers()) {
                if (user.getName().equals(username))
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
        Optional<User> result = lobbyManager.getLoggedUser().stream().filter(user -> user.getName().equals(username)).findAny();
        return result.isPresent();
    }
}
