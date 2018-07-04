package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.network.strategycontroller.RMIStrategyController;
import org.poianitibaldizhou.sagrada.network.strategycontroller.SocketStrategyController;
import org.poianitibaldizhou.sagrada.network.strategycontroller.NetworkStrategyController;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

/**
 * OVERVIEW: Manages the connection of a certain client to the server, allowing to switch the network connection
 * type of client easily
 */
public class ConnectionManager implements NetworkStrategyController {

    private ConnectionType networkType;
    private NetworkStrategyController strategyController;
    private String ipAddress;
    private int port;

    /**
     * Constructor.
     * Create a Network Manager with which it's possible to change the network connection type (RMI or Socket)
     *
     * @param ipAddress   the ip address of the server
     * @param networkType the network type of connection desired
     */
    public ConnectionManager(String ipAddress, int port, ConnectionType networkType) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.networkType = networkType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ILobbyController getLobbyController() {
        return strategyController.getLobbyController();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGameController getGameController() {
        return strategyController.getGameController();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        strategyController.close();
    }

    /**
     * Set the network type
     *
     * @param networkType the new network type
     * @throws IllegalArgumentException if the network type is something different from RMI or SOCKET
     */
    public void setNetworkType(ConnectionType networkType) {
        this.networkType = networkType;
        activateConnection();
    }

    /**
     * Activate the strategy controller by creating them
     */
    public void activateConnection(){
        if (strategyController != null)
            strategyController.close();
        switch (networkType) {
            case RMI:
                strategyController = new RMIStrategyController(ipAddress, port);
                break;
            case SOCKET:
                strategyController = new SocketStrategyController(ipAddress, port);
                break;
            default:
                throw new IllegalArgumentException(ServerMessage.NETWORK_UNDEFINED);
        }
    }

    /**
     * Returns the type of connection currently active
     *
     * @return type of connection that is currently active
     */
    public ConnectionType getNetworkType() {
        return networkType;
    }

    /**
     * @return ip address of the server
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @return port of the connection
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the new ip address of the server
     *
     * @param ipAddress the new ip address of the server
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Set the new port of the server
     *
     * @param port the new port of the server
     */
    public void setPort(int port) {
        this.port = port;
    }
}
