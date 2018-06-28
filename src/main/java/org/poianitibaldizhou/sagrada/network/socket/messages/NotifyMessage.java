package org.poianitibaldizhou.sagrada.network.socket.messages;

import java.io.Serializable;

/**
 * OVERVIEW: Represents a notify message that are sent from the server to the client
 */
public class NotifyMessage implements Serializable{

    private final int observerHashcode;
    private final Request request;

    /**
     * Constructor.
     * Encapsulate the client observer(e.g. target) and the Request invocation given by methodName and methodParameters
     *
     * @param observerHashcode the hashcode of the observer needed by the client to find the observer
     * @param methodName the method name to create the request
     * @param methodParameters the method parameters to create the request
     */
    public NotifyMessage(int observerHashcode, String methodName, Object... methodParameters) {
        this.request = new Request(methodName, methodParameters);
        this.observerHashcode = observerHashcode;
    }

    /**
     * Returns the observer hash code for identify the observer
     *
     * @return observer hash code
     */
    public int getObserverHashcode(){
        return observerHashcode;
    }

    /**
     * Return the request that the server will make to the client
     *
     * @return request that the server will make to the client
     */
    public Request getRequest(){
        return request;
    }

}
