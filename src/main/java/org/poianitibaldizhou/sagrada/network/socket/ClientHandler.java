package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.ControllerManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.network.socket.messages.*;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SERVER-SIDE
 * Class to handle the client request via socket
 */
public class ClientHandler implements Runnable {

    private ControllerManager controllerManager;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    /**
     * Constructor.
     * Create a Runnable ClientHandler to handle the client request and send response and notify to the client
     *
     * @param socket            the socket connected to the client
     * @param controllerManager the controller manager of the server that contains the reference of all the controllers
     */
    public ClientHandler(Socket socket, ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }

    }

    /**
     * Send a Response or NotifyMessage to the server via outputStream
     *
     * @param obj the Response or NotifyMessage to be sent
     */
    public synchronized void sendResponse(Object obj) throws IOException {
        if (!(obj instanceof Response || obj instanceof NotifyMessage))
            throw new IllegalArgumentException(ServerMessage.CLIENT_HANDLER_ILLEGAL_ARGUMENT);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
    }

    /**
     * Thread.
     * Wait for object from the inputStream of the socket. if the object is a Request then it will invoke the
     * method of the request
     */
    @Override
    public void run() {
        Object object;
        do {
            try {
                object = objectInputStream.readObject();
                if (object instanceof LobbyRequest) {
                    Request request = (Request) object;
                    replaceObserver(request);
                    Object reply = request.invokeMethod(controllerManager.getLobbyController());
                    if (reply != null)
                        sendResponse(new Response((Serializable) reply));
                }
                if (object instanceof GameRequest) {
                    Request request = (Request) object;
                    replaceObserver(request);
                    Object reply = request.invokeMethod(controllerManager.getGameController());
                    if (reply != null)
                        sendResponse(new Response((Serializable) reply));
                }
            } catch (IOException | ClassNotFoundException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                object = null;
            }
        } while (object != null);
        close();
    }

    /**
     * Close every stream opened when the class is instantiated
     */
    private void close() {
        try {
            objectInputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Replace the view passed via socket with its counterpart proxy if there is one
     *
     * @param request the request of the client
     */
    private void replaceObserver(Request request) {
        List<Object> parameters = request.getMethodParameters();
        for (int i = 0; i < parameters.size(); i++) {
            Class clazz = parameters.get(i).getClass();
            if(parameters.get(i) instanceof UnicastRemoteObject){
                request.replaceParameter(Proxy.newProxyInstance(
                        clazz.getClassLoader(), clazz.getInterfaces(),
                        new ProxyObserverInvocationHandler(this, parameters.get(i).hashCode())), i);
            }
            if(parameters.get(i) instanceof Map){
                Map map = (Map) parameters.get(i);
                if(map.values().toArray()[0] instanceof IPlayerObserver){
                    Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
                    map.forEach((key, value) -> playerObserverMap.putIfAbsent((String) key, (IPlayerObserver) Proxy.newProxyInstance(
                            IPlayerObserver.class.getClassLoader(), new Class[]{IPlayerObserver.class},
                            new ProxyObserverInvocationHandler(this, value.hashCode()))));
                    request.replaceParameter(playerObserverMap, i);
                }
                else if(map.values().toArray()[0] instanceof ISchemaCardObserver){
                    Map<String, ISchemaCardObserver> schemaCardObserverMap = new HashMap<>();
                    map.forEach((key, value) -> schemaCardObserverMap.putIfAbsent((String) key, (ISchemaCardObserver) Proxy.newProxyInstance(
                            ISchemaCardObserver.class.getClassLoader(), new Class[]{ISchemaCardObserver.class},
                            new ProxyObserverInvocationHandler(this, value.hashCode()))));
                    request.replaceParameter(schemaCardObserverMap, i);
                }
                else if(map.values().toArray()[0] instanceof IToolCardObserver){
                    Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
                    map.forEach((key, value) -> toolCardObserverMap.putIfAbsent((String) key, (IToolCardObserver) Proxy.newProxyInstance(
                            IToolCardObserver.class.getClassLoader(), new Class[]{IToolCardObserver.class},
                            new ProxyObserverInvocationHandler(this, value.hashCode()))));
                    request.replaceParameter(toolCardObserverMap, i);
                }
                else{
                    throw new IllegalStateException();
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof ClientHandler)) return false;
        ClientHandler other = (ClientHandler) obj;
        return objectInputStream.equals(other.objectInputStream) && objectOutputStream.equals(other.objectOutputStream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectInputStream, objectOutputStream);
    }
}
