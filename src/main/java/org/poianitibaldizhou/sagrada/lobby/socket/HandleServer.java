package org.poianitibaldizhou.sagrada.lobby.socket;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.NotifyMessage;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Request;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandleServer implements Runnable {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Object objectRequested = null;

    private static final Logger LOGGER = Logger.getLogger(HandleServer.class.getName());

    public HandleServer(Socket socket, ILobbyObserver observer) {
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }

    public HandleServer(Socket socket) {
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }

    public void sendRequest(Object objectToSend) {
        try {
            outputStream.writeObject(objectToSend);
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }

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

    public void close() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }

    @Override
    public void run() {
        Object object;
        do {
            try {
                object = inputStream.readObject();
                if (object instanceof NotifyMessage) {

                }
                if (object instanceof Response) {
                    synchronized (this) {
                        Response response = (Response) object;
                        objectRequested = response.getObject();
                        this.notifyAll();
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.toString());
                object = null;
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, e.toString());
                object = null;
            }
        } while (object != null);
        close();
    }
}
