package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.network.socket.messages.NotifyMessage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * OVERVIEW: Send the observer notify to the clients using reflection.
 * This is a server-side class.
 */
public class ProxyObserverInvocationHandler implements InvocationHandler {

    private final ClientHandler clientHandler;
    private final int clientObserverHashcode;

    /**
     * Constructor.
     * Creates a proxy observer
     *
     * @param clientHandler          client handler that handle the responses that needs to be sent
     * @param clientObserverHashcode hash code that identifies the observer of a certain client
     */
    public ProxyObserverInvocationHandler(ClientHandler clientHandler, int clientObserverHashcode) {
        this.clientHandler = clientHandler;
        this.clientObserverHashcode = clientObserverHashcode;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Creates
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("equals")) {
            return equalsMethod(proxy, args[0]);
        }
        if (method.getName().equals("hashCode")) {
            return this.hashCode();
        }
        clientHandler.sendResponse(new NotifyMessage(clientObserverHashcode, method.getName(), args));
        return null;
    }

    /**
     * Check if two objects are equals: they are equals if they have the same client
     * observer hashcode and client handler
     *
     * @param me    object to compare with other
     * @param other object to compare with me
     * @return true if equals, false otherwise
     */
    private boolean equalsMethod(Object me, Object other) {
        if (other == null)
            return false;
        if (other.getClass() == me.getClass())
            return false;
        InvocationHandler handler = Proxy.getInvocationHandler(other);
        return this.equals(handler);
    }

    /**
     * Two ProxyObserverInvocationHandler are equals if they have the same client handler
     * and the male client observer hash code
     *
     * @param obj object to compare
     * @return true if equals, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProxyObserverInvocationHandler)) return false;

        ProxyObserverInvocationHandler handler = (ProxyObserverInvocationHandler) obj;
        return this.clientHandler.equals(handler.clientHandler)
                && clientObserverHashcode == handler.clientObserverHashcode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientHandler, clientObserverHashcode);
    }


}
