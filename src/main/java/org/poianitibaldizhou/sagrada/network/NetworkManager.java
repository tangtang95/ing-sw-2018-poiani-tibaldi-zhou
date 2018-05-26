package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.network.strategycontroller.RMIStrategyController;
import org.poianitibaldizhou.sagrada.network.strategycontroller.SocketStrategyController;
import org.poianitibaldizhou.sagrada.network.strategycontroller.StrategyController;

//TODO make network manager lazy
public class NetworkManager implements StrategyController{

    private NetworkType networkType;
    private StrategyController strategyController;
    private final String ipAddress;

    /**
     * Constructor.
     * Create a Network Manager with which it's possible to change the network connection type (RMI or Socket)
     *
     * @param ipAddress the ip address of the server
     * @param networkType the network type of connection desired
     */
    public NetworkManager(String ipAddress, NetworkType networkType) {
        this.ipAddress = ipAddress;
        setNetworkType(networkType);
    }

    @Override
    public ILobbyController getLobbyController() {
        return strategyController.getLobbyController();
    }

    @Override
    public IGameController getGameController(){
        return strategyController.getGameController();
    }

    @Override
    public void close() {
        strategyController.close();
    }

    /**
     * Set the network type if it is different from the current one. If it's different then
     * the strategyController will be updated
     *
     * @param networkType the new network type
     * @throws IllegalArgumentException if the network type is something different from RMI or SOCKET
     */
    public void setNetworkType(NetworkType networkType) {
        if(networkType == this.networkType)
            return;
        this.networkType = networkType;
        if(strategyController != null)
            strategyController.close();
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
