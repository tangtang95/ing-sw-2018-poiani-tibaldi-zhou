package org.poianitibaldizhou.sagrada.network.socket.messages;

import java.io.Serializable;

/**
 * OVERVIEW: Represents a response
 */
public class Response implements Serializable {

    private final Serializable object;

    /**
     * Constructor.
     * Encapsulate an object serializable into the Response
     *
     * @param object the object to encapsulate inside the Response
     */
    public Response(Serializable object) {
        this.object = object;
    }

    /**
     * Return the serializable object that needs to be sent over the network
     *
     * @return serializable object that needs to be sent over the network
     */
    public Serializable getObject() {
        return object;
    }

}
