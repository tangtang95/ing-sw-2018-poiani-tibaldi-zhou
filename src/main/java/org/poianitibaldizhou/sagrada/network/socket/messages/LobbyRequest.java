package org.poianitibaldizhou.sagrada.network.socket.messages;

public class LobbyRequest extends Request{

    public LobbyRequest(String methodName, Object... methodParameters) {
        super(methodName, methodParameters);
    }

    public LobbyRequest(Request request) {
        super(request);
    }
}
