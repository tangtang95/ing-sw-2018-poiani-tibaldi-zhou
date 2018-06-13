package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.TestOnly;
import org.poianitibaldizhou.sagrada.MediatorManager;
import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.ForceSkipTurnFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.TimeOutFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.GameNetworkManager;
import org.poianitibaldizhou.sagrada.network.ServerGameHeartBeat;

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

    private final GameNetworkManager gameNetworkManager;

    /**
     * Constructor.
     * Creates a game manager for handling the various games active on the server.
     *
     * @param managerMediator manager mediator between this and lobby manager
     */
    public GameManager(MediatorManager managerMediator) {
        this.managerMediator = managerMediator;
        gameMap = new HashMap<>();
        playersByGame = new HashMap<>();
        players = new ArrayList<>();
        gameObserverManagerMap = new HashMap<>();
        gameNetworkManager = new GameNetworkManager(this);
    }

    // GETTER

    public GameNetworkManager getGameNetworkManager() {
        return gameNetworkManager;
    }

    @Contract(pure = true)
    public synchronized List<IGame> getGameList() {
        return new ArrayList<>(gameMap.values());
    }


    @Contract(pure = true)
    public synchronized boolean notContainsGame(final String gameName) {
        return !gameMap.containsKey(gameName);
    }

    /**
     * Returs the list of the player of a certain game.
     * If none game with that name is present, return null
     *
     * @param gameName name of the game
     * @return list of the player in gameName
     */
    @Contract(pure = true)
    public synchronized List<String> getPlayersByGame(String gameName) {
        return gameMap.containsKey(gameName) ? new ArrayList<>(playersByGame.get(gameName)) : null;
    }

    public synchronized GameObserverManager getObserverManagerByGame(String gameName) {
        return gameObserverManagerMap.get(gameName);
    }

    /**
     * Return the game associated with name.
     *
     * @param name name of the wanted game
     * @return game associated with name
     */
    public synchronized IGame getGameByName(String name) {
        // TODO: refactor in this way
        //if(gameMap.get(name) == null)
        //    throw new IOException();
        return gameMap.get(name);
    }

    // MODIFIER

    /**
     * Creates a new single player game.
     *
     * @param userName   single player's difficulty
     * @param difficulty difficulty of the game
     * @return name of the single player name
     * @throws IOException network communication error
     */
    public synchronized String createSinglePlayerGame(String userName, int difficulty) throws IOException {
        String gameName;

        // Try login with this username
        if (managerMediator.isAlreadyWaitingInALobby(userName) || players.contains(String.valueOf(userName.hashCode()))) {
            throw new IOException();
        }

        // Creates the game
        do {
            gameName = UUID.randomUUID().toString();
        } while (gameMap.containsKey(gameName));

        String token = String.valueOf(userName.hashCode());

        SinglePlayerGame singlePlayer = new SinglePlayerGame(gameName, new User(userName, token), difficulty,
                new TerminationGameManager(gameName, this));

        gameMap.putIfAbsent(gameName, singlePlayer);
        playersByGame.putIfAbsent(gameName, Collections.singletonList(token));
        players.add(token);
        gameObserverManagerMap.putIfAbsent(gameName, new GameObserverManager(playersByGame.get(gameName), singlePlayer));

        gameNetworkManager.startHearthBeat(gameName);

        singlePlayer.initGame();

        return gameName;
    }

    /**
     * Adds a multi player game to the list of current gameMap, if it's not present.
     * It also creates the GameObserverManager associated with the game
     * If the game is already present, does nothing.
     *
     * @param game     game to add
     * @param gameName game's name
     */
    public synchronized void createMultiPlayerGame(IGame game, String gameName) {
        if (game.isSinglePlayer())
            throw new IllegalArgumentException();

        if (gameMap.putIfAbsent(gameName, game) == null) {
            playersByGame.put(gameName, new ArrayList<>());
            game.getUsers().forEach(user -> {
                playersByGame.get(gameName).add(user.getToken());
                players.add(user.getToken());
            });
            gameObserverManagerMap.putIfAbsent(gameName, new GameObserverManager(playersByGame.get(gameName), game));

            TimeOutFakeObserver timeOutFakeObserver = new TimeOutFakeObserver(getObserverManagerByGame(gameName));
            game.attachStateObserver(GameObserverManager.TIME_OUT, timeOutFakeObserver);
            gameObserverManagerMap.get(gameName).setTimeOutFakeObserver(timeOutFakeObserver);
            game.attachStateObserver(UUID.randomUUID().toString(), new ForceSkipTurnFakeObserver(gameObserverManagerMap.get(gameName)));

            gameNetworkManager.startHearthBeat(gameName);

            game.initGame();
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
        if (gameMap.remove(gameName) != null) {
            List<String> playersPlaying = playersByGame.get(gameName);
            players.removeAll(playersPlaying);
            playersByGame.remove(gameName);
            gameObserverManagerMap.remove(gameName);

            gameNetworkManager.stopHearthBeat(gameName);
        }
    }

    /**
     * Force the termination of the game.
     * When the game is single player, if the only player present disconnects the game terminates.
     * When the game is multi player, and there is only one player connected, it handles its victory.
     *
     * @param game            handle the termination of this game
     * @param observerManager game observer manager of game
     * @return true if the game gets terminated, false otherwise
     */
    public boolean handleEndGame(IGame game, GameObserverManager observerManager) {
        if (!game.isSinglePlayer()) {
            if (observerManager.getDisconnectedPlayer().size() == game.getUsers().size() - 1) {
                // search for the player that it's not disconnected
                for (Player player : game.getPlayers())
                    if (!observerManager.getDisconnectedPlayer().contains(player.getToken())) {
                        game.forceGameTermination(player);
                    }
                terminateGame(game.getName());
                return true;
            } else if (observerManager.getDisconnectedPlayer().size() == game.getUsers().size()) {
                terminateGame(game.getName());
                return true;
            }
        } else if (!observerManager.getDisconnectedPlayer().isEmpty()) {
            terminateGame(game.getName());
            return true;
        }
        return false;
    }
}
