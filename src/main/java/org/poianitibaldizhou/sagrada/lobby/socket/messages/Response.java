package org.poianitibaldizhou.sagrada.lobby.socket.messages;

import java.io.Serializable;

public class Response implements Serializable {

    private final Object object;

    /**
     * Constructor.
     * Encapsulate an object serializable into the Response
     *
     * @param object the object to encapsulate inside the Response
     */
    public Response(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

}
