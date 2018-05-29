package org.poianitibaldizhou.sagrada.game.model.observers;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

public class ObserverManager {

    private Set<String> disconnectedPlayer;
    private Set<String> disconnectedPlayerNotNotified;

    public ObserverManager() {
        disconnectedPlayer = new HashSet<>();
        disconnectedPlayerNotNotified = new HashSet<>();
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

    public void notifyDisconnection(String token) {
        disconnectedPlayerNotNotified.remove(token);
    }

    public void signalDisconnection(String token) {
        disconnectedPlayerNotNotified.add(token);
        disconnectedPlayer.add(token);
    }

    public void signalReconnect(String token) {
        disconnectedPlayer.remove(token);
        disconnectedPlayerNotNotified.remove(token);
    }
}
