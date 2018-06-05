package org.poianitibaldizhou.sagrada.lobby.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class LobbyObserverManager {

    private Map<String, ScheduledExecutorService> scheduledExecutorServiceMap;
    private Set<String> disconnectedUserNotNotified;

    public LobbyObserverManager() {
        scheduledExecutorServiceMap = new HashMap<>();
        disconnectedUserNotNotified = new HashSet<>();
    }

    public synchronized Set<String> getDisconnectedUserNotNotified() {
        return disconnectedUserNotNotified;
    }

    public synchronized void disconnectionNotified(String token) {
        disconnectedUserNotNotified.remove(token);
        scheduledExecutorServiceMap.remove(token);
    }

    public synchronized void addToken(String token) {
        scheduledExecutorServiceMap.putIfAbsent(token, Executors.newScheduledThreadPool(1));
    }

    public synchronized void removeToken(String token) {
        scheduledExecutorServiceMap.get(token).shutdown();
        scheduledExecutorServiceMap.remove(token);
    }

    /**
     * Invoked when a player is detected as disconnected.
     * It stops all of the threads regarding the observer notification ù
     *
     * @param token token of the disconnected user
     */
    public synchronized void signalDisconnection(String token) {
        disconnectedUserNotNotified.add(token);
        scheduledExecutorServiceMap.get(token).shutdownNow();
    }

    public synchronized void pushThreadInQueue(String token, Runnable notify) {
        if(!getDisconnectedUserNotNotified().contains(token))
            scheduledExecutorServiceMap.get(token).submit(notify);
    }
}
