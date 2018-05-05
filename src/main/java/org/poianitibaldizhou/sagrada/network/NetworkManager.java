package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.network.socket.ProxyController;
import org.poianitibaldizhou.sagrada.network.socket.ProxyGameController;
import org.poianitibaldizhou.sagrada.network.socket.ProxyLobbyController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NetworkManager {

    private NetworkType networkType;
    private ILobbyController lobbyController;
    private IGameController gameController;
    private final String ipAddress;

    public NetworkManager(String ipAddress, NetworkType networkType) {
        this.ipAddress = ipAddress;
        this.networkType = networkType;
        this.lobbyController = null;
        this.gameController = null;
    }

    public ILobbyController getLobbyController() {
        setLobbyController(networkType);
        return lobbyController;
    }

    public IGameController getGameController(){
        setGameController(networkType);
        return gameController;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void closeLobbyController(){
        if (lobbyController != null && lobbyController instanceof ProxyLobbyController) {
            ((ProxyController) lobbyController).close();
        }
        lobbyController = null;
    }

    public void closeGameController(){
        if (gameController != null && gameController instanceof ProxyLobbyController) {
            ((ProxyController) gameController).close();
        }
        gameController = null;
    }

    private void setGameController(NetworkType networkType){
        switch (networkType) {
            case RMI:
                break;
            case SOCKET:
                if(gameController != null && gameController instanceof ProxyGameController)
                    break;
                gameController = new ProxyGameController(ipAddress, networkType.getPort());
                break;
            default:
        }
    }

    private void setLobbyController(NetworkType networkType) {
        switch (networkType) {
            case RMI:
                closeLobbyController();
                try {
                    lobbyController = (ILobbyController) LocateRegistry.getRegistry(ipAddress, networkType.getPort())
                            .lookup("lobbycontroller");
                } catch (RemoteException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot find RMI registry");
                } catch (NotBoundException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot find RMI controller");
                }
                break;
            case SOCKET:
                if (lobbyController != null && lobbyController instanceof ProxyLobbyController)
                    break;
                lobbyController = new ProxyLobbyController(ipAddress, networkType.getPort());
                break;
            default:
                break;
        }
    }
}
