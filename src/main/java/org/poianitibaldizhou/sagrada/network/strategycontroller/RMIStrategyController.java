package org.poianitibaldizhou.sagrada.network.strategycontroller;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIStrategyController implements StrategyController{

    private ILobbyController lobbyController;
    private IGameController gameController;

    /**
     * Constructor.
     * Create a RMI strategy controller; get the controller from the registry located in the ipAddress with
     * a specific port
     * @param ipAddress the ip address of the registry
     * @param port the listening port of the registry
     */
    public RMIStrategyController(String ipAddress, int port){
        try {
            lobbyController = (ILobbyController) LocateRegistry.getRegistry(ipAddress, port)
                    .lookup("lobbycontroller");
            gameController = (IGameController) LocateRegistry.getRegistry(ipAddress, port)
                    .lookup("gamecontroller");
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot find RMI registry");
        } catch (NotBoundException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot find RMI controller");
        }
    }

    @Override
    public ILobbyController getLobbyController() {
        return lobbyController;
    }

    @Override
    public IGameController getGameController() {
        return gameController;
    }

    @Override
    public void close() {
        lobbyController = null;
        gameController = null;
    }
}
