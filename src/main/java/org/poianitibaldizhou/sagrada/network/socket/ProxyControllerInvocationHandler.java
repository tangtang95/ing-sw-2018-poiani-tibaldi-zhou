package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.network.socket.messages.GameRequest;
import org.poianitibaldizhou.sagrada.network.socket.messages.LobbyRequest;
import org.poianitibaldizhou.sagrada.network.socket.messages.Request;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.rmi.server.UnicastRemoteObject;

public class ProxyControllerInvocationHandler implements InvocationHandler {

    private ServerHandler serverHandler;
    private ControllerType controllerType;

    public ProxyControllerInvocationHandler(ServerHandler serverHandler, ControllerType controllerType) {
        this.serverHandler = serverHandler;
        this.controllerType = controllerType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(args != null) {
            for (Object arg : args) {
                if (arg instanceof UnicastRemoteObject)
                    serverHandler.addViewToHashMap(arg.hashCode(), arg);
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
        if(!method.getReturnType().getSimpleName().equalsIgnoreCase("void"))
            return serverHandler.getResponse();
        return null;
    }
}
