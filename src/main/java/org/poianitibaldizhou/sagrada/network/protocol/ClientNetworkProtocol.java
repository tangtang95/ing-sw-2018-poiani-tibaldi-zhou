package org.poianitibaldizhou.sagrada.network.protocol;

import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.io.IOException;
import java.util.List;

public class ClientNetworkProtocol {
    private JSONClientProtocol jsonClientProtocol;

    public ClientNetworkProtocol() {
        jsonClientProtocol = new JSONClientProtocol();
    }

    public DiceWrapper getDice(String message) throws IOException {
        return null;
    }

    public List<DiceWrapper> getDiceList(String message) throws IOException {
        return null;
    }
}
