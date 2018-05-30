package org.poianitibaldizhou.sagrada.game.model.observers;

import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.TimeOutFakeObserver;
import sun.nio.ch.ThreadPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class ObserverManager {

    private Set<String> disconnectedPlayer;
    private Set<String> disconnectedPlayerNotNotified;
    private HashMap<String, ScheduledExecutorService> executorHashMap;

    /**
     * Creates an observer manager for a certain game, starting from the the list of the player of that game.
     * This class handles a thread pool for sending updates to the observers.
     *
     * @param tokenList list of player's token of the intended game
     */
    public ObserverManager(List<String> tokenList) {
        disconnectedPlayer = new HashSet<>();
        disconnectedPlayerNotNotified = new HashSet<>();
        executorHashMap = new HashMap<>();

        tokenList.forEach(token -> {
            ScheduledExecutorService scheduledTask = Executors.newScheduledThreadPool(1);
            executorHashMap.putIfAbsent(token, scheduledTask);
        });

        executorHashMap.putIfAbsent(TimeOutFakeObserver.TIME_OUT, Executors.newScheduledThreadPool(1));
    }

    // GETTER

    @Contract(pure = true)
    public Set<String> getDisconnectedPlayer() {
        return new HashSet<>(disconnectedPlayer);
    }

    @Contract(pure = true)
    public Set<String> getDisconnectedPlayerNotNotified() {
        return new HashSet<>(disconnectedPlayerNotNotified);
    }

    // MODIFIER

    /**
     * Adds a thread in the queue regarding a certain player, only if it is signaled as non-disconnected.
     *
     * @param token  player's token
     * @param notify runnable interface that needs to be scheduled
     */
    public void pushThreadInQueue(String token, Runnable notify) {
        if (!disconnectedPlayer.contains(token))
            executorHashMap.get(token).submit(notify);
    }

    /**
     * The players have been notified that a certain player identified by token is disconnected.
     *
     * @param token player's token
     */
    public void notifyDisconnection(String token) {
        disconnectedPlayerNotNotified.remove(token);
    }

    /**
     * Signals the disconnections of a certain player.
     * This method stops the thread queue regarding that player.
     *
     * @param token player's token
     */
    public void signalDisconnection(String token) {
        executorHashMap.get(token).shutdownNow();
        disconnectedPlayerNotNotified.add(token);
        disconnectedPlayer.add(token);
    }

    /**
     * Signals the reconnection of a certain player.
     * This method also creates the new thread queue associated with that player.
     *
     * @param token player's token
     */
    public void signalReconnect(String token) {
        executorHashMap.replace(token, Executors.newScheduledThreadPool(1));
        disconnectedPlayer.remove(token);
        disconnectedPlayerNotNotified.remove(token);
    }
}
