package org.poianitibaldizhou.sagrada.lobby.socket.messages;

import org.poianitibaldizhou.sagrada.lobby.model.Lobby;

import java.util.List;

public class NotifyMessage extends Request {

    private Object target;

    public NotifyMessage(Object target, String methodName, List<Object> methodParameters) {
        super(methodName, methodParameters);
        this.target = target;
    }

    public Object getTarget(){
        return target;
    }

}
