package org.poianitibaldizhou.sagrada.graphics.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.poianitibaldizhou.sagrada.network.ConnectionType;
import org.poianitibaldizhou.sagrada.utilities.ClientSettings;

/**
 * Contains the values used for the connection
 */
public class ConnectionModel {

    private StringProperty ipAddress;
    private IntegerProperty port;

    private StringProperty connectionType;

    /**
     * Constructor.
     * Create a connection model that contains ip address, port and type of connection
     */
    public ConnectionModel() {
        ipAddress = new SimpleStringProperty(ClientSettings.getIP());
        port = new SimpleIntegerProperty(ClientSettings.getSocketPort());
        connectionType = new SimpleStringProperty(ConnectionType.SOCKET.name());
    }

    /**
     * Set the type of connection used
     *
     * @param connectionType type of connection used
     */
    public void setConnectionType(String connectionType) {
        this.connectionType.set(connectionType);
    }

    /**
     * Set the server ip address
     *
     * @param ipAddress ip address that will be set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress.set(ipAddress);
    }

    /**
     * Set the server port used in the connection
     *
     * @param port port that will be used
     */
    public void setPort(int port) {
        this.port.set(port);
    }

    /**
     * @return server port that will be used for the connection
     */
    public int getPort() {
        return port.get();
    }

    /**
     * @return type of connection used
     */
    public String getConnectionType() {
        return connectionType.get();
    }

    /**
     * @return server ip address
     */
    public String getIpAddress() {
        return ipAddress.get();
    }

    /**
     * @return integer property of the server port
     */
    public IntegerProperty portProperty() {
        return port;
    }

    /**
     * @return string property of the server ip address
     */
    public StringProperty ipAddressProperty() {
        return ipAddress;
    }

    /**
     * @return string property of the connection type
     */
    public StringProperty connectionTypeProperty() {
        return connectionType;
    }
}
