package org.poianitibaldizhou.sagrada.network.socket.messages;

/**
 * OVERVIEW: Represents a request regarding the game stage of the application
 */
public class GameRequest extends Request {

    /**
     * Creates a game request for the call of a certain method with the parameters specified
     *
     * @param methodName       name of the method that need to be called
     * @param methodParameters parameters for the method call
     */
    public GameRequest(String methodName, Object... methodParameters) {
        super(methodName, methodParameters);
    }
}
