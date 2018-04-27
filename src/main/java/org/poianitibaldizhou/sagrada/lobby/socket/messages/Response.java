package org.poianitibaldizhou.sagrada.lobby.socket.messages;

import java.io.Serializable;

public class Response implements Serializable{

    private final Object object;

    public Response(Object object){
        this.object = object;
    }

    public Object getObject(){
        return object;
    }

}
