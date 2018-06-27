package org.poianitibaldizhou.sagrada.network.socket.messages;

import java.io.Serializable;

/**
 * OVERVIEW:
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
    
    public int getObserverHashcode(){
        return observerHashcode;
    }

    public Request getRequest(){
        return request;
    }

}
