package org.poianitibaldizhou.sagrada.network.socket.messages;

import java.io.Serializable;

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

    public Serializable getObject() {
        return object;
    }

}
