package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.network.observers.LobbyObserverManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the network side of the lobby phase of the game
 */
public class LobbyNetworkManager {

    private final transient Map<String, IView> viewMap = new HashMap<>();
    private final LobbyManager lobbyManager;

    /**
     * Creates a new lobby network manager for the lobby phase of the game.
     * It needs the server lobby manager
     * @param lobbyManager server lobby manager
     */
    public LobbyNetworkManager(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    // GETTER

    /**
     * Returns the map of client's token and their view map
     * @return
     */
    public Map<String, IView> getViewMap() {
        return viewMap;
    }

    /**
     * Return the view of a certain client by his token
     * @param token user's token
     * @return user's view
     */
    public IView getViewByToken(String token) {
        return viewMap.get(token);
    }

    // MODIFIER

    /**
     * Adds a view of a client identified by his token
     * @param token client's token
     * @param view client's view
     */
    public void putView(String token, IView view) {
        viewMap.put(token, view);
    }

    /**
     * Remove a view of a certain client identified by his token
     * @param token client's token
     */
    public void removeView(String token) {
        viewMap.remove(token);
    }

    /**
     * Clear the observers of the lobby
     */
    public void clearObserver() {
        if (lobbyManager.isLobbyActive()) {
            ping();
            LobbyObserverManager lobbyObserverManager = lobbyManager.getLobbyObserverManager();
            lobbyObserverManager.getDisconnectedUserNotNotified().forEach(token -> {
                lobbyManager.userDisconnects(token);
                viewMap.remove(token);
            });
            lobbyObserverManager.getDisconnectedUserNotNotified().forEach(lobbyObserverManager::disconnectionNotified);
        }
    }


    /**
     * Pings the client in the lobby
     */
    public synchronized void ping() {
        if (lobbyManager.isLobbyActive()) {
            lobbyManager.getLobby().getLobbyObserverMap().forEach((k, v) -> v.onPing());
        }
    }

}
