package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.network.socket.messages.GameRequest;
import org.poianitibaldizhou.sagrada.network.socket.messages.LobbyRequest;
import org.poianitibaldizhou.sagrada.network.socket.messages.Request;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

/**
 * OVERVIEW: Represents a proxy controller client-side that reflects the calls with the
 * the usage of a server handler.
 */
public class ProxyControllerInvocationHandler implements InvocationHandler {

    private ServerHandler serverHandler;
    private ControllerType controllerType;

    /**
     * Constructor.
     * Creates a proxy controller client-side
     *
     * @param serverHandler  server handler that sends the call to the server
     * @param controllerType type of controller depending on the network communication system chosen
     */
    public ProxyControllerInvocationHandler(ServerHandler serverHandler, ControllerType controllerType) {
        this.serverHandler = serverHandler;
        this.controllerType = controllerType;
    }

    /**
     * {@inheritDoc}
     * With respect to the controller type creates different type of requests and send
     * them via a server handler
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof UnicastRemoteObject)
                    serverHandler.addViewToHashMap(arg.hashCode(), arg);
                if(arg instanceof Map){
                    Map map = (Map) arg;
                    map.forEach((key, value)-> serverHandler.addViewToHashMap(value.hashCode(), value));
                }
            }
        }
        Request request = null;
        switch (controllerType) {
            case GAME_CONTROLLER:
                request = new GameRequest(method.getName(), args);
                break;
            case LOBBY_CONTROLLER:
                request = new LobbyRequest(method.getName(), args);
                break;
            default:
                break;
        }
        serverHandler.sendRequest(request);
        if (!method.getReturnType().getSimpleName().equalsIgnoreCase("void"))
            return serverHandler.getResponse();
        return null;
    }
}
