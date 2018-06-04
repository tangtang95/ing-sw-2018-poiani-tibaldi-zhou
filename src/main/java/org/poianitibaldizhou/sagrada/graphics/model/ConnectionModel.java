package org.poianitibaldizhou.sagrada.graphics.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

public class ConnectionModel {

    private StringProperty ipAddress;
    private IntegerProperty port;

    private StringProperty connectionType;

    private static final String LOCAL_HOST = "localhost";

    public ConnectionModel(){
        ipAddress = new SimpleStringProperty(LOCAL_HOST);
        port = new SimpleIntegerProperty(ConnectionType.RMI.getPort());
        connectionType = new SimpleStringProperty(ConnectionType.RMI.name());
    }

    public void setConnectionType(String connectionType) {
        this.connectionType.set(connectionType);
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress.set(ipAddress);
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public int getPort() {
        return port.get();
    }

    public String getConnectionType() {
        return connectionType.get();
    }

    public String getIpAddress() {
        return ipAddress.get();
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public StringProperty ipAddressProperty() {
        return ipAddress;
    }

    public StringProperty connectionTypeProperty() {
        return connectionType;
    }
}
