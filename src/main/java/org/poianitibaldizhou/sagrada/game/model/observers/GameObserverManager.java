package org.poianitibaldizhou.sagrada.game.model.observers;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.TimeOutFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ITimeOutObserver;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GameObserverManager {

    private Set<String> disconnectedPlayer;
    private Set<String> disconnectedPlayerNotNotified;
    private HashMap<String, ScheduledExecutorService> executorHashMap;
    private HashMap<String, ITimeOutObserver> observerTimeoutHashMap;

    private IGame game;
    private TimeOutFakeObserver timeOutFakeObserver;

    public static final String TIME_OUT = "TimeOut";

    /**
     * Creates an observer manager for a certain game, starting from the the list of the player of that game.
     * This class handles a thread pool for sending updates to the observers.
     *
     * @param tokenList list of player's token of the intended game
     * @param game game that observers listen to
     */
    public GameObserverManager(List<String> tokenList, IGame game) {
        disconnectedPlayer = new HashSet<>();
        disconnectedPlayerNotNotified = new HashSet<>();
        executorHashMap = new HashMap<>();
        observerTimeoutHashMap = new HashMap<>();

        timeOutFakeObserver = null;
        this.game = game;

        tokenList.forEach(token -> {
            ScheduledExecutorService scheduledTask = Executors.newScheduledThreadPool(1);
            executorHashMap.putIfAbsent(token, scheduledTask);
        });

        executorHashMap.putIfAbsent(TIME_OUT, Executors.newScheduledThreadPool(1));
    }

    // GETTER

    @Contract(pure = true)
    public IGame getGame() {
        return game;
    }

    @Contract(pure = true)
    public Map<String, ITimeOutObserver> getObserverTimeoutHashMap() {
        return new HashMap<>(observerTimeoutHashMap);
    }

    @Contract(pure = true)
    public Set<String> getDisconnectedPlayer() {
        return new HashSet<>(disconnectedPlayer);
    }

    @Contract(pure = true)
    public Set<String> getDisconnectedPlayerNotNotified() {
        return new HashSet<>(disconnectedPlayerNotNotified);
    }

    @Contract(pure = true)
    public Long getTimeToTimeout() {
        return timeOutFakeObserver.getTimeToTimeout();
    }

    // MODIFIER

    public void setTimeOutFakeObserver(TimeOutFakeObserver timeOutFakeObserver) {
        this.timeOutFakeObserver = timeOutFakeObserver;
    }

    public void attachTimeoutObserver(String token, ITimeOutObserver timeOutObserver) {
        observerTimeoutHashMap.putIfAbsent(token, timeOutObserver);
    }

    public void detachTimeoutObserver(String token) {
        observerTimeoutHashMap.remove(token);
    }

    /**
     * Adds a thread in the queue regarding a certain player, only if it is signaled as non-disconnected.
     *
     * @param token  player's token
     * @param notify runnable interface that needs to be scheduled
     */
    public void pushThreadInQueue(String token, Runnable notify) {
        System.out.println("Pushing thread in queue");
        if (!disconnectedPlayer.contains(token))
            executorHashMap.get(token).submit(notify);
    }

    /**
     * The players have been notified that a certain player identified by token is disconnected.
     *
     * @param token player's token
     */
    public void notifyDisconnection(String token) {
        System.out.println("Disconnection of a certain user has been notified");
        disconnectedPlayerNotNotified.remove(token);
    }

    /**
     * Signals the disconnections of a certain player.
     * This method stops the thread queue regarding that player.
     *
     * @param token player's token
     */
    public void signalDisconnection(String token) {
        System.out.println("Disconnection signaled");
        executorHashMap.get(token).shutdownNow();
        disconnectedPlayerNotNotified.add(token);
        disconnectedPlayer.add(token);
        detachTimeoutObserver(token);
    }

    /**
     * Signals the reconnection of a certain player.
     * This method also creates the new thread queue associated with that player.
     *
     * @param token player's token
     */
    public void signalReconnect(String token) {
        System.out.println("Reconnection signaled");
        executorHashMap.replace(token, Executors.newScheduledThreadPool(1));
        disconnectedPlayer.remove(token);
        disconnectedPlayerNotNotified.remove(token);
    }

    /**
     * Add the last notification thread for a certain token. Needed when the game ends
     *
     * @param token  player's token
     * @param notify runnable interface that needs to be scheduled
     */
    public void pushLastThreadInQueue(String token, Runnable notify) {
        if (!disconnectedPlayer.contains(token)) {
            executorHashMap.get(token).submit(notify);
            executorHashMap.get(token).shutdown();
        }
    }

    public void pushTimeoutThread(String token, Runnable notify, String timedOutToken) {
        if(!disconnectedPlayer.contains(token)) {
            if(token.equals(timedOutToken)) {
                executorHashMap.get(token).shutdownNow();
                executorHashMap.replace(token, Executors.newScheduledThreadPool(1));
                executorHashMap.get(TIME_OUT).submit(notify);
            } else {
                executorHashMap.get(token).submit(notify);
            }
        }
    }
}
