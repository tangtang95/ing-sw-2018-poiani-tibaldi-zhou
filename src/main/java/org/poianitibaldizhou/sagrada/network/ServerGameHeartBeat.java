package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.ServerSettings;
import org.poianitibaldizhou.sagrada.game.model.GameManager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: Heart beat for detection client disconnection
 */
public class ServerGameHeartBeat extends Thread {

    private final String gameName;
    private final GameNetworkManager gameNetworkManager;
    private final GameManager gameManager;
    private static final long SLEEP_TIME = ServerSettings.getHearthBeatMillis();

    /**
     * Constructor.
     * Creates a new server heart beat for detecting client disconnection
     * related to a certain game.
     *
     * @param gameNetworkManager game network manager of the server
     *
     * @param gameName    name of the game on which the heart beat acts
     */
    public ServerGameHeartBeat(GameNetworkManager gameNetworkManager, GameManager gameManager, String gameName) {
        this.gameName = gameName;
        this.gameNetworkManager= gameNetworkManager;
        this.gameManager = gameManager;
    }

    /**
     * Heart beats the observers of the client for detecting disconnections
     */
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(SLEEP_TIME);
                try {
                    if (gameNetworkManager.clearObservers(gameName))
                        break;
                } catch (NullPointerException npe) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, npe.toString());
                    if (!gameManager.notContainsGame(gameName)) {
                        throw new IllegalStateException();
                    }
                }
            }
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
            Thread.currentThread().interrupt();
        }
    }
}