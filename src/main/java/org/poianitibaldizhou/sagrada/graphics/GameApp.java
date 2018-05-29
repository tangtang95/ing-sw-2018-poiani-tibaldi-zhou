package org.poianitibaldizhou.sagrada.graphics;

import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.view.CLIStartGameView;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameApp {
    public static void main(String[] args) {
        ConnectionManager networkManager = new ConnectionManager("localhost", ConnectionType.RMI);
        ScreenManager screenManager = new ScreenManager();

        try {
            screenManager.pushScreen(new CLIStartGameView(networkManager,screenManager));
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "CLIStartGameView cannot initialize");
        }

    }
}
