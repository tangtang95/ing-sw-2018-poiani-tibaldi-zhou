package org.poianitibaldizhou.sagrada.network.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ProxyController {

    protected Socket socket;
    protected ServerHandler serverHandler;
    protected Thread serverHandlerThread;

    /**
     * Constructor.
     * Create a proxy client controller to have transparency of the socket connection
     *
     * @param ipAddress the IP address of the server
     * @param port      the port of the server on which is listening
     */
    public ProxyController(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
        serverHandler = new ServerHandler(socket);
        serverHandlerThread = new Thread(serverHandler);
        serverHandlerThread.start();
    }

    /**
     * Close the proxy controller -> stop the serverHandler thread and close the socket
     */
    public void close() {
        serverHandlerThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }
}
