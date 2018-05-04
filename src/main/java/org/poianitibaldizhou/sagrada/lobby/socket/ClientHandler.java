package org.poianitibaldizhou.sagrada.lobby.socket;

import org.mockito.internal.matchers.Not;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.NotifyMessage;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Request;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Response;
import org.poianitibaldizhou.sagrada.lobby.socket.proxyviews.CLILobbyProxyView;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SERVER-SIDE
 * Class to handle the client request via socket
 */
public class ClientHandler implements Runnable {

    private ILobbyController controller;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    /**
     * Constructor.
     * Create a Runnable ClientHandler to handle the client request and send response and notify to the client
     *
     * @param socket     the socket connected to the client
     * @param controller the "real" controller of the server
     */
    public ClientHandler(Socket socket, ILobbyController controller) {
        this.controller = controller;
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
    public synchronized void sendResponse(Object obj) {
        if (!(obj instanceof Response || obj instanceof NotifyMessage))
            throw new IllegalArgumentException("The object passed is not a Response or a NotifyMessage");
        try {
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
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
                if (object instanceof Request) {
                    Request request = (Request) object;
                    replaceObserver(request);
                    Object reply = request.invokeMethod(controller);
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
            if (parameters.get(i) instanceof CLILobbyView) {
                request.replaceParameter(new CLILobbyProxyView(this, parameters.get(i).hashCode()), i);
            }
        }
    }

}
