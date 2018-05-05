package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;


public class NetworkManager {

    private NetworkType networkType;
    private StrategyController strategyController;
    private final String ipAddress;

    public NetworkManager(String ipAddress, NetworkType networkType) {
        this.ipAddress = ipAddress;
        setNetworkType(networkType);
    }

    public ILobbyController getLobbyController() {
        return strategyController.getLobbyController();
    }

    public IGameController getGameController(){
        return strategyController.getGameController();
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
        switch (networkType){
            case RMI:
                strategyController = new RMIStrategyController(ipAddress, networkType.getPort());
                break;
            case SOCKET:
                strategyController = new SocketStrategyController(ipAddress, networkType.getPort());
                break;
            default:
                throw new IllegalArgumentException("network type undefined");
        }
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

}
