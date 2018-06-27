package org.poianitibaldizhou.sagrada.network.socket.messages;

public class GameRequest extends Request {
    public GameRequest(String methodName, Object... methodParameters) {
        super(methodName, methodParameters);
    }
}
