package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.network.strategycontroller.RMIStrategyController;
import org.poianitibaldizhou.sagrada.network.strategycontroller.SocketStrategyController;
import org.poianitibaldizhou.sagrada.network.strategycontroller.StrategyController;

public class ConnectionManager implements StrategyController{

    private ConnectionType networkType;
    private StrategyController strategyController;
    private final String ipAddress;
    private int port;

    /**
     * Constructor.
     * Create a Network Manager with which it's possible to change the network connection type (RMI or Socket)
     *
     * @param ipAddress the ip address of the server
     * @param networkType the network type of connection desired
     */
    public ConnectionManager(String ipAddress, int port, ConnectionType networkType) {
        this.ipAddress = ipAddress;
        this.port = port;
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
    public void setNetworkType(ConnectionType networkType) {
        if(networkType == this.networkType)
            return;
        this.networkType = networkType;
        this.port = networkType.getPort();
        if(strategyController != null)
            strategyController.close();
        switch (networkType){
            case RMI:
                strategyController = new RMIStrategyController(ipAddress, port);
                break;
            case SOCKET:
                strategyController = new SocketStrategyController(ipAddress, port);
                break;
            default:
                throw new IllegalArgumentException("network type undefined");
        }
    }

    public ConnectionType getNetworkType() {
        return networkType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

}
