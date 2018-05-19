package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OVERVIEW: This not contains duplicate
 */
public class GameManager {
    private HashMap<String, Game> games;
    private HashMap<String, List<String>> playersByGame;
    private List<String> players;

    public GameManager() {
        games = new HashMap<>();
        playersByGame = new HashMap<>();
        players = new ArrayList<>();
    }

    /**
     * Adds a game to the list of current games, if it's not present.
     * If the game is already present, does nothing.
     *
     * @param game game to add
     */
    public synchronized void addGame(Game game, String name) {
        if(games.putIfAbsent(name, game) == null){
            playersByGame.put(name, new ArrayList<>());
        }
    }

    /**
     * Player joining a certain game.
     * If the game is not present, does nothing.
     *
     * @param token player's token
     * @throws RemoteException if player is already playing in another game
     */
    public synchronized void joinGame(String gameName, String token) throws RemoteException {
        if(games.containsKey(gameName)) {
            if(players.contains(token))
                throw new RemoteException("Already playing in a game");

            players.add(token);
            playersByGame.get(gameName).add(token);
        }
    }

    /**
     * Terminates a certain game removing all the information connected to it.
     * If the game is not present, does nothing.
     *
     * @param gameName game to terminate
     */
    public synchronized void terminateGame(String gameName) {
        if(games.remove(gameName) != null) {
            List<String> playersPlaying = playersByGame.get(gameName);
            players.removeAll(playersPlaying);
            playersByGame.remove(gameName);
        }
    }

    /**
     * Return the game associated with name.
     *
     * @param name name of the wanted game
     * @return game associated with name
     */
    public synchronized Game getGameByName(String name) {
        return games.get(name);
    }

    /**
     * Returs the list of the player of a certain game.
     * If none game with that name is present, return null
     *
     * @param gameName name of the game
     * @return list of the player in gameName
     */
    public synchronized List<String> getPlayersByGame(String gameName) {
        return games.containsKey(gameName)? new ArrayList(playersByGame.get(gameName)) : null;

    }

    @Contract(pure = true)
    public synchronized List<Game> getGames() {
        return new ArrayList<>(games.values());
    }
}
