package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.MediatorManager;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OVERVIEW: This not contains duplicate
 */
public class GameManager {
    private Map<String, IGame> gameMap;
    private Map<String, GameObserverManager> observerManagerMap;

    private Map<String, List<String>> playersByGame;
    private List<String> players;
    private final MediatorManager managerMediator;

    public GameManager(MediatorManager managerMediator) {
        this.managerMediator = managerMediator;
        gameMap = new HashMap<>();
        playersByGame = new HashMap<>();
        players = new ArrayList<>();
        observerManagerMap = new HashMap<>();
    }

    /**
     * Adds a game to the list of current gameMap, if it's not present.
     * It also creates the GameObserverManager associated with the game
     * If the game is already present, does nothing.
     *
     * @param game game to add
     * @param gameName game's name
     */
    public synchronized void addGame(IGame game, String gameName) {
        if(gameMap.putIfAbsent(gameName, game) == null){
            playersByGame.put(gameName, new ArrayList<>());
            game.getUsers().forEach(user -> {
                playersByGame.get(gameName).add(user.getToken());
                players.add(user.getToken());
            });
            observerManagerMap.putIfAbsent(gameName, new GameObserverManager(playersByGame.get(gameName)));
        }
    }

    /**
     * Terminates a certain game removing all the information connected to it.
     * It also removes the observer manager related with the game.
     * If the game is not present, does nothing.
     *
     * @param gameName game to terminate
     */
    public synchronized void terminateGame(String gameName) {
        if(gameMap.remove(gameName) != null) {
            List<String> playersPlaying = playersByGame.get(gameName);
            players.removeAll(playersPlaying);
            playersByGame.remove(gameName);
            observerManagerMap.remove(gameName);
        }
    }

    /**
     * Return the game associated with name.
     *
     * @param name name of the wanted game
     * @return game associated with name
     */
    public synchronized IGame getGameByName(String name) {
        return gameMap.get(name);
    }

    /**
     * Returs the list of the player of a certain game.
     * If none game with that name is present, return null
     *
     * @param gameName name of the game
     * @return list of the player in gameName
     */
    public synchronized List<String> getPlayersByGame(String gameName) {
        return gameMap.containsKey(gameName)? new ArrayList<>(playersByGame.get(gameName)) : null;
    }

    public synchronized boolean containsGame(final String gameName) {
        return gameMap.values().stream().map(IGame::getName).anyMatch(s -> s.equals(gameName));
    }

    public synchronized GameObserverManager getObserverManagerByGame(String gameName) {
        return observerManagerMap.get(gameName);
    }

    @Contract(pure = true)
    public synchronized List<IGame> getGameList() {
        return new ArrayList<>(gameMap.values());
    }
}
