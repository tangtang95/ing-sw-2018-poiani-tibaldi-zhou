package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.network.socket.messages.NotifyMessage;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

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
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("equals")){
            return equalsMethod(proxy, args[0]);
        }
        if(method.getName().equals("hashCode")){
            return this.hashCode();
        }
        clientHandler.sendResponse(new NotifyMessage(clientObserverHashcode, method.getName(), args));
        return null;
    }

    private Object equalsMethod(Object me, Object other) {
        if(other == null)
            return false;
        if(other.getClass() == me.getClass())
            return false;
        InvocationHandler handler = Proxy.getInvocationHandler(other);
        return this.equals(handler);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof ProxyObserverInvocationHandler)) return false;

        ProxyObserverInvocationHandler handler = (ProxyObserverInvocationHandler) obj;
        return this.clientHandler.equals(handler.clientHandler)
                && clientObserverHashcode == handler.clientObserverHashcode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientHandler, clientObserverHashcode);
    }


}
