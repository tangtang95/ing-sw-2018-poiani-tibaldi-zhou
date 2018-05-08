package org.poianitibaldizhou.sagrada.game.model;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
     *
     * @param game game to add
     */
    public synchronized void addGame(Game game, String name) {
        if(games.putIfAbsent(name, game) == null){
            playersByGame.put(name, new ArrayList<>());
        }
    }

    /**
     * Remove a game from the list of current games, and thus all the players
     * present in that game.
     *
     * @param game game to be removed
     */
    public synchronized void removeGame(Game game) {
        if(games.remove(game.getName()) != null) {
            playersByGame.remove(game.getName());
        }
    }

    /**
     * Player joining a certain game
     *
     * @param game game that player in joining
     * @param token player's token
     * @throws RemoteException if player is already playing in another game
     */
    public synchronized void joinGame(Game game, String token) throws RemoteException {
        if(players.contains(token))
            throw new RemoteException("Already playing in a game");
        players.add(token);
        playersByGame.get(game.getName()).add(token);
    }

    /**
     * Terminates a certain game removing all the information connected to it.
     *
     * @param game game to terminate
     */
    public synchronized void terminateGame(Game game) {
        games.remove(game.getName());
        List<String> playersPlaying = playersByGame.get(game.getName());
        players.removeAll(playersPlaying);
        playersByGame.remove(game.getName());
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
}
