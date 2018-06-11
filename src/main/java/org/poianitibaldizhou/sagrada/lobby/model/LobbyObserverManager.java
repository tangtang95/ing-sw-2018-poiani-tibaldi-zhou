package org.poianitibaldizhou.sagrada.lobby.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * OVERVIEW: manages the observer of the current active lobby
 * Not contains duplicates.
 * This class manages a queue of a single thread running for each client
 */
public class LobbyObserverManager {

    private Map<String, ScheduledExecutorService> scheduledExecutorServiceMap;
    private Set<String> disconnectedUserNotNotified;

    /**
     * Constructor.
     * Creates a new observer manager for the current lobby
     */
    public LobbyObserverManager() {
        scheduledExecutorServiceMap = new HashMap<>();
        disconnectedUserNotNotified = new HashSet<>();
    }

    /**
     * Returns the list of disconnected users of the lobby that haven't been notified to the other
     * users inside the lobby yet
     *
     * @return list of disconnected users of the lobby that haven't been notified to the other
     * users inside the lobby yet
     */
    public synchronized Set<String> getDisconnectedUserNotNotified() {
        return disconnectedUserNotNotified;
    }

    /**
     * Notify that the users inside the lobby have been notified of the disconnection of the user
     * identified by token
     *
     * @param token user's token that have been signaled to other users as disconnected
     */
    public synchronized void disconnectionNotified(String token) {
        disconnectedUserNotNotified.remove(token);
        scheduledExecutorServiceMap.remove(token);
    }

    /**
     * Add a thread queue for the user identified by token
     * @param token user's token
     */
    public synchronized void addToken(String token) {
        scheduledExecutorServiceMap.putIfAbsent(token, Executors.newScheduledThreadPool(1));
    }

    /**
     * Removes and close the thread queue related with token
     * @param token user's token
     */
    public synchronized void removeToken(String token) {
        if(scheduledExecutorServiceMap.get(token) != null) {
            scheduledExecutorServiceMap.get(token).shutdown();
            scheduledExecutorServiceMap.remove(token);
        }
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

    /**
     * Push a runnable related to token in queue
     *
     * @param token user's token
     * @param notify runnable that will be executed by the thread queue related to token
     */
    public synchronized void pushThreadInQueue(String token, Runnable notify) {
        if (!getDisconnectedUserNotNotified().contains(token) && scheduledExecutorServiceMap.get(token) != null)
            scheduledExecutorServiceMap.get(token).submit(notify);
    }
}
