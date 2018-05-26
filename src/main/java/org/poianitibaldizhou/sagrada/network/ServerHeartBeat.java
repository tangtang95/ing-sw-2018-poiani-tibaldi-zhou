package org.poianitibaldizhou.sagrada.network;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerHeartBeat {

    private Map<String, ScheduledFuture> timers;
    private Set<String> hasUserPing;
    private IPingController pingController;

    private static final int DELAY = 5; // delay value in seconds

    public ServerHeartBeat(IPingController pingController) {
        this.pingController = pingController;
        timers = new HashMap<>();
        hasUserPing = new HashSet<>();
    }

    public void addHeartBeatUser(final String token) {
        Runnable runnable = () -> {
            if (!hasUserPing.contains(token)) {
                timers.get(token).cancel(false);
                try {
                    pingController.disconnect(token);
                } catch (RemoteException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "Shouldn't happen");
                }
                return;
            }
            hasUserPing.remove(token);
        };
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture scheduler = executorService.scheduleAtFixedRate(runnable, DELAY, DELAY, TimeUnit.SECONDS);
        timers.put(token, scheduler);
    }

    public void userPing(final String token) {
        hasUserPing.add(token);

    }

}
