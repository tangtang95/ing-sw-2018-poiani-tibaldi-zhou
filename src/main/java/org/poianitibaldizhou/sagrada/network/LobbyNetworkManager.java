package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyObserverManager;

import java.util.HashMap;
import java.util.Map;

public class LobbyNetworkManager {

    private final transient Map<String, IView> viewMap = new HashMap<>();
    private final LobbyManager lobbyManager;

    public LobbyNetworkManager(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    // GETTER
    public Map<String, IView> getViewMap() {
        return viewMap;
    }

    public IView getViewByToken(String token) {
        return viewMap.get(token);
    }

    // MODIFIER
    public void putView(String token, IView view) {
        viewMap.put(token, view);
    }

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
