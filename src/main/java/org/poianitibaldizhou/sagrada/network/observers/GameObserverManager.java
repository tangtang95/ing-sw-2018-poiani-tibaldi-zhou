package org.poianitibaldizhou.sagrada.network.observers;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.TimeOutFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ITimeOutObserver;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * OVERVIEW: Game observer manager that handles the observers of a specific game by using a thread queue of a single
 * thread active in a certain moment.
 */
public class GameObserverManager {

    private final Lock lock;

    private final Set<String> disconnectedPlayer;
    private final Set<String> disconnectedPlayerNotNotified;
    private final Map<String, ScheduledExecutorService> executorHashMap;
    private final Map<String, ITimeOutObserver> observerTimeoutHashMap;

    private final IGame game;
    private TimeOutFakeObserver timeOutFakeObserver;

    public static final String TIME_OUT = "TimeOut";

    /**
     * Creates an observer manager for a certain game, starting from the the list of the player of that game.
     * This class handles a thread pool for sending updates to the observers.
     *
     * @param tokenList list of player's token of the intended game
     * @param game      game that observers listen to
     */
    public GameObserverManager(List<String> tokenList, IGame game) {
        disconnectedPlayer = new HashSet<>();
        disconnectedPlayerNotNotified = new HashSet<>();
        executorHashMap = new HashMap<>();
        observerTimeoutHashMap = new HashMap<>();

        lock = new ReentrantLock();

        timeOutFakeObserver = null;
        this.game = game;

        tokenList.forEach(token -> {
            ScheduledExecutorService scheduledTask = Executors.newScheduledThreadPool(1);
            executorHashMap.putIfAbsent(token, scheduledTask);
        });

        executorHashMap.putIfAbsent(TIME_OUT, Executors.newScheduledThreadPool(1));
    }

    // GETTER

    /**
     * @return this manages the observer for the game returned
     */
    @Contract(pure = true)
    public IGame getGame() {
        return game;
    }

    /**
     * Returns the map of the timeout observers currently active for this game
     *
     * @return map of timeout observer and the tokens that identify them
     */
    @Contract(pure = true)
    public Map<String, ITimeOutObserver> getObserverTimeoutHashMap() {
        return new HashMap<>(observerTimeoutHashMap);
    }

    /**
     * Returns the list of the disconnected players
     *
     * @return list of disconnected players
     */
    @Contract(pure = true)
    public Set<String> getDisconnectedPlayer() {
        return new HashSet<>(disconnectedPlayer);
    }

    /**
     * Returns the list of disconnected players whose disconnection hasn't been notified to other players yet.
     *
     * @return list of disconnected player whose disconnection hasn't been notified to other players yet
     */
    @Contract(pure = true)
    public Set<String> getDisconnectedPlayerNotNotified() {
        return new HashSet<>(disconnectedPlayerNotNotified);
    }

    /**
     * Return the time missing to time out
     *
     * @return time left to time out (millis)
     */
    @Contract(pure = true)
    public Long getTimeToTimeout() {
        return timeOutFakeObserver.getTimeToTimeout();
    }

    // MODIFIER

    /**
     * Set the timeout fake observer to the game managed by this.
     *
     * @param timeOutFakeObserver timeout fake observer that will be set
     */
    public void setTimeOutFakeObserver(TimeOutFakeObserver timeOutFakeObserver) {
        synchronized (getGame()) {
            this.timeOutFakeObserver = timeOutFakeObserver;
        }
    }

    /**
     * Add a timeout real observer
     *
     * @param token           token that identify the timeout real observer
     * @param timeOutObserver timeout real observer identified by token
     */
    public void attachTimeoutObserver(String token, ITimeOutObserver timeOutObserver) {
        synchronized (getGame()) {
            observerTimeoutHashMap.putIfAbsent(token, timeOutObserver);
        }
    }

    /**
     * Detach a timeout real observer identified by token
     *
     * @param token token of the timeout observer to detach
     */
    private void detachTimeoutObserver(String token) {
        synchronized (getGame()) {
            observerTimeoutHashMap.remove(token);
        }
    }

    /**
     * Adds a thread in the queue regarding a certain player, only if it is signaled as non-disconnected.
     *
     * @param token  player's token
     * @param notify runnable interface that needs to be scheduled
     */
    public void pushThreadInQueue(String token, Runnable notify) {
        synchronized (getGame()) {
            lock.lock();
            if (!disconnectedPlayer.contains(token))
                executorHashMap.get(token).submit(notify);
            lock.unlock();
        }
    }

    /**
     * The players have been notified that a certain player identified by token is disconnected.
     *
     * @param token player's token
     */
    public void notifyDisconnection(String token) {
        synchronized (getGame()) {
            lock.lock();
            disconnectedPlayerNotNotified.remove(token);
            lock.unlock();
        }
    }

    /**
     * Signals the disconnections of a certain player.
     * This method stops the thread queue regarding that player.
     *
     * @param token player's token
     */
    public void signalDisconnection(String token) {
        synchronized (getGame()) {
            lock.lock();
            executorHashMap.get(token).shutdownNow();
            disconnectedPlayerNotNotified.add(token);
            disconnectedPlayer.add(token);
            detachTimeoutObserver(token);
            lock.unlock();
        }
    }

    /**
     * Signals the reconnection of a certain player.
     * This method also creates the new thread queue associated with that player.
     *
     * @param token player's token
     */
    public void signalReconnect(String token) {
        synchronized (getGame()) {
            lock.lock();
            executorHashMap.replace(token, Executors.newScheduledThreadPool(1));
            disconnectedPlayer.remove(token);
            disconnectedPlayerNotNotified.remove(token);
            lock.unlock();
        }
    }

    /**
     * Push the notification of the timeout of a a certain player identified by timedOutToken
     *
     * @param token         token that identifies the thread queue
     * @param notify        notify the timeout
     * @param timedOutToken token of the player who timed out
     */
    public void pushTimeoutThread(String token, Runnable notify, String timedOutToken) {
        synchronized (getGame()) {
            lock.lock();
            if (!disconnectedPlayer.contains(token)) {
                if (token.equals(timedOutToken)) {
                    executorHashMap.get(token).shutdownNow();
                    executorHashMap.replace(token, Executors.newScheduledThreadPool(1));
                    executorHashMap.get(TIME_OUT).submit(notify);
                } else {
                    executorHashMap.get(token).submit(notify);
                }
            }
            lock.unlock();
        }
    }
}