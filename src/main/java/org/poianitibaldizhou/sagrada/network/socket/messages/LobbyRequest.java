package org.poianitibaldizhou.sagrada.network.socket.messages;

/**
 * OVERVIEW: Represents a request during the lobby stage of an user experience
 */
public class LobbyRequest extends Request{

    /**
     * Request for invoking a certain method regarding the lobby phase of the game.
     *
     * @param methodName method that needs to be invoked
     * @param methodParameters parameters for the method invocation
     */
    public LobbyRequest(String methodName, Object... methodParameters) {
        super(methodName, methodParameters);
    }
}
