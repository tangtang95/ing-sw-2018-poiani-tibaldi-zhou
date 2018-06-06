package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.MediatorManager;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.util.*;

/**
 * OVERVIEW: This not contains duplicate
 */
public class GameManager {
    private final Map<String, IGame> gameMap;
    private final Map<String, GameObserverManager> gameObserverManagerMap;

    private final Map<String, List<String>> playersByGame;
    private final List<String> players;
    private final MediatorManager managerMediator;

    public GameManager(MediatorManager managerMediator) {
        this.managerMediator = managerMediator;
        gameMap = new HashMap<>();
        playersByGame = new HashMap<>();
        players = new ArrayList<>();
        gameObserverManagerMap = new HashMap<>();
    }

    /**
     * Creates a new single player game.
     *
     * @param userName single player's difficulty
     * @param difficulty difficulty of the game
     * @return name of the single player name
     * @throws IOException network communication error
     */
    public synchronized String createSinglePlayerGame(String userName, int difficulty) throws IOException {
        String gameName;

        // Try login with this username
        if(managerMediator.isAlreadyWaitingInALobby(userName)) {
            throw new IOException();
        }

        // Creates the game
        do {
            gameName = UUID.randomUUID().toString();
        } while(gameMap.containsKey(gameMap));

        String token = String.valueOf(userName.hashCode());

        SinglePlayerGame singlePlayer = new SinglePlayerGame(gameName, new User(userName, token), difficulty, new TerminationGameManager(gameName, this));

        gameMap.putIfAbsent(gameName, singlePlayer);
        playersByGame.putIfAbsent(gameName, Collections.singletonList(token));
        players.add(token);
        gameObserverManagerMap.putIfAbsent(gameName, new GameObserverManager(playersByGame.get(gameName)));

        return gameName;
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
        System.out.println("GAME ADDED: " + gameName);
        if(gameMap.putIfAbsent(gameName, game) == null){
            playersByGame.put(gameName, new ArrayList<>());
            game.getUsers().forEach(user -> {
                playersByGame.get(gameName).add(user.getToken());
                players.add(user.getToken());
            });
            gameObserverManagerMap.putIfAbsent(gameName, new GameObserverManager(playersByGame.get(gameName)));
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
        System.out.println("TERMINATE GAME " + gameName);
        if(gameMap.remove(gameName) != null) {
            List<String> playersPlaying = playersByGame.get(gameName);
            players.removeAll(playersPlaying);
            playersByGame.remove(gameName);
            gameObserverManagerMap.remove(gameName);
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
        return gameObserverManagerMap.get(gameName);
    }

    @Contract(pure = true)
    public synchronized List<IGame> getGameList() {
        return new ArrayList<>(gameMap.values());
    }
}
