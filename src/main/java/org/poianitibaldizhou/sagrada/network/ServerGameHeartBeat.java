package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.ServerSettings;
import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyObserverManager;

import java.util.Map;

/**
 * OVERVIEW: Heart beat for detection client disconnection
 */
public class ServerGameHeartBeat extends Thread {

    private final String gameName;
    private final GameManager gameManager;
    private static final long SLEEP_TIME = ServerSettings.getHearthBeatMillis();

    /**
     * Constructor.
     * Creates a new server heart beat for detecting client disconnection
     * related to a certain game.
     *
     * @param gameManager game manager of the server
     * @param gameName    name of the game on which the heart beat acts
     */
    public ServerGameHeartBeat(GameManager gameManager, String gameName) {
        this.gameName = gameName;
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
                    System.out.println("cleaning obs in heart beat");
                    if (gameManager.clearObservers(gameName))
                        break;
                    System.out.println("End cleaning obs in hearth beat");
                } catch (NullPointerException npe) {
                    if (!gameManager.notContainsGame(gameName)) {
                        System.out.println("Heath beat interrupted");
                        throw new IllegalStateException();
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Hearth beat interrupted");
        }
    }
}