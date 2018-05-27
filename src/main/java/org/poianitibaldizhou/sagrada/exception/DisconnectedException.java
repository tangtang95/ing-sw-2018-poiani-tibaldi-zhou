package org.poianitibaldizhou.sagrada.exception;

import java.util.List;

public class DisconnectedException extends Exception {

    List<String> disconnectedPlayerList;

    public DisconnectedException(List<String> disconnectedPlayerList) {
        this.disconnectedPlayerList = disconnectedPlayerList;
    }

    public List<String> getDisconnectedPlayerList() {
        return disconnectedPlayerList;
    }
}
