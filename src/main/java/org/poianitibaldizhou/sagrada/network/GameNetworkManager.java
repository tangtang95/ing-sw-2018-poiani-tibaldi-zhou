package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.util.*;

/**
 * OVERVIEW: handles the network part of the application regarding the playing stage of the game
 */
public class GameNetworkManager {

    private final HashMap<String, IGameView> viewMap = new HashMap<>();
    private final GameManager gameManager;

    private final Map<String, ServerGameHeartBeat> serverGameHeartBeatMap;

    /**
     * Creates a new game network manager to manage the network of the game state phase
     *
     * @param gameManager game manager of the server
     */
    public GameNetworkManager(GameManager gameManager) {
        this.gameManager = gameManager;
        serverGameHeartBeatMap = new HashMap<>();
    }

    // GETTER

    /**
     * Returns the view of a certain client by his token
     *
     * @param token client's token
     * @return client's view
     */
    public IGameView getViewByToken(String token) {
        return viewMap.get(token);
    }


    /**
     * Returns true if the network manager manages a view identified by token
     * @param token client's token
     * @return true if client is managed, false otherwise
     */
    public boolean containsToken(String token) {
        return viewMap.containsKey(token);
    }

    /**
     * Remove the client identified by token
     * @param token client's token
     */
    public void removeToken(String token){
        viewMap.remove(token);
    }

    // MODIFIER

    /**
     * Start the heart beat for a certain game
     *
     * @param gameName heart beat starts for this game
     */
    public void startHearthBeat(String gameName) {
        ServerGameHeartBeat serverGameHeartBeat = new ServerGameHeartBeat(this, gameManager, gameName);
        serverGameHeartBeatMap.putIfAbsent(gameName, serverGameHeartBeat);
        serverGameHeartBeat.start();
    }

    /**
     * Stop the heart beat of a certain game
     *
     * @param gameName the heart beat of this game stops
     */
    public void stopHearthBeat(String gameName) {
        serverGameHeartBeatMap.remove(gameName).interrupt();
    }

    /**
     * Remove a view of a certain client
     *
     * @param token client's token
     */
    public void removeView(String token) {
        viewMap.remove(token);
    }

    /**
     * Insert a view of a certain client
     *
     * @param token    client's token
     * @param gameView client's view
     */
    public void putView(String token, IGameView gameView) {
        viewMap.put(token, gameView);
    }


    /**
     * It cleans the observer of a certain game, with the notify disconnections.
     * It also signals the disconnections of the various player.
     * It also handles the game termination when there aren't enough user to continue the game
     *
     * @param gameName game's name
     * @return true if the game terminates, false otherwise
     */
    public synchronized boolean clearObservers(String gameName) {
        try {
            synchronized (gameManager.getGameByName(gameName)) {
                GameObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
                Set<String> toNotifyDisconnect = observerManager.getDisconnectedPlayerNotNotified();
                Set<String> disconnected = observerManager.getDisconnectedPlayer();
                List<User> playerList = gameManager.getGameByName(gameName).getUsers();

                ping(gameName);

                for(String disconnectedToken : toNotifyDisconnect) {
                    playerList.forEach(player -> {
                        if (!disconnected.contains(player.getToken())) {
                            try {
                                Optional<User> user = playerList.stream().filter(u -> u.getToken().
                                        equals(disconnectedToken)).findFirst();
                                if (user.isPresent())
                                    viewMap.get(player.getToken()).err(user.get().getName() + " disconnected");
                            } catch (IOException e) {
                                observerManager.signalDisconnection(player.getToken());
                            }
                        }
                    });

                    gameManager.getGameByName(gameName).detachObservers(disconnectedToken);
                    observerManager.notifyDisconnection(disconnectedToken);
                    viewMap.remove(disconnectedToken);
                }

                return gameManager.handleEndGame(gameManager.getGameByName(gameName), observerManager);
            }
        } catch(IOException e) {
            return true;
        }
    }


    /**
     * Pings the connected client of a certain game in order to identify disconnections.
     *
     * @param gameName name of the game
     */
    private void ping(String gameName) {
        try {
            gameManager.getGameByName(gameName).getPlayers().stream().map(Player::getToken).forEach(
                    token -> {
                        try {
                            if (viewMap.get(token) != null)
                                viewMap.get(token).ping();
                        } catch (IOException e) {
                            gameManager.getObserverManagerByGame(gameName).signalDisconnection(token);
                        }
                    }
            );
        } catch(IOException e) {
            // DO NOTHING BECAUSE THE GAME IS ALREADY TERMINATED
        }
    }

}
