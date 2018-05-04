package org.poianitibaldizhou.sagrada.lobby.socket;

import org.poianitibaldizhou.sagrada.lobby.socket.messages.NotifyMessage;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Request;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CLIENT-SIDE
 * Class to handle the server response and notify via socket
 */
public class ServerHandler implements Runnable {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Object objectRequested = null;

    private Map<Integer, Object> viewMap;

    private static final Logger LOGGER = Logger.getLogger(ServerHandler.class.getName());

    /**
     * Constructor.
     * Create a Runnable ServerHandler to handle the server response and notifier and send request to the server
     *
     * @param socket the socket connected to the server
     */
    public ServerHandler(Socket socket) {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
        viewMap = new HashMap<>();
    }

    /**
     * Send an Request object to the server via outputStream
     *
     * @param obj the object to be sent
     */
    public synchronized void sendRequest(Request obj) {
        try {
            outputStream.writeObject(obj);
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Get the response of the server if the method of the controller has something to return. If the response
     * from the server hasn't arrived yet, then this method will go to wait mode (waiting for a notify)
     *
     * @return the object requested
     */
    public synchronized Object getResponse() {
        while (objectRequested == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.toString());
                Thread.currentThread().interrupt();
            }
        }
        Object copyObject = objectRequested;
        objectRequested = null;
        return copyObject;

    }

    /**
     * Thread.
     * Wait for object from the inputStream of the socket. if the object is a NotifyMessage then it gets the
     * view of the client and invoke the method inside the message, otherwise if the object is a Response it will
     * notify the getResponse method.
     */
    @Override
    public void run() {
        Object object;
        do {
            try {
                object = inputStream.readObject();
                if (object instanceof NotifyMessage) {
                    NotifyMessage notifyMessage = (NotifyMessage) object;
                    Object target = viewMap.get(notifyMessage.getObserverHashcode());
                    notifyMessage.getRequest().invokeMethod(target);
                }
                if (object instanceof Response) {
                    synchronized (this) {
                        Response response = (Response) object;
                        objectRequested = response.getObject();
                        this.notifyAll();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, e.toString());
                object = null;
            }
        } while (object != null);
        close();
    }

    /**
     * Add the view to the HashMap using a hashcode as a key
     *
     * @param hashcode the key of the HashMap
     * @param view     the object binding the key
     */
    public synchronized void addViewToHashMap(int hashcode, Object view) {
        if(!viewMap.containsKey(hashcode))
            viewMap.put(hashcode, view);
    }

    /**
     * Close every stream opened when the class is instantiated
     */
    private void close() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }
}
