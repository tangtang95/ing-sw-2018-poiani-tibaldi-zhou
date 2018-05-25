package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.network.socket.messages.NotifyMessage;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * SERVER - SIDE
 */
public class ProxyObserverInvocationHandler implements InvocationHandler {

    private final ClientHandler clientHandler;
    private final int clientObserverHashcode;

    public ProxyObserverInvocationHandler(ClientHandler clientHandler, int clientObserverHashcode){
        this.clientHandler = clientHandler;
        this.clientObserverHashcode = clientObserverHashcode;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws IOException {
        clientHandler.sendResponse(new NotifyMessage(clientObserverHashcode, method.getName(), args));
        return null;
    }
}
