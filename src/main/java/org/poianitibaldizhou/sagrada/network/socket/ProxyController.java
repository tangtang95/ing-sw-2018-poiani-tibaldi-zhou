package org.poianitibaldizhou.sagrada.network.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ProxyController {

    protected Socket socket;
    protected ServerHandler serverHandler;
    protected Thread serverHandlerThread;
    protected boolean isEnabled;

    /**
     * Constructor.
     * Create a proxy client controller to have transparency of the socket connection
     *
     * @param socket the socket connected with the server
     */
    public ProxyController(Socket socket) {
        this.socket = socket;
        serverHandler = new ServerHandler(socket);
        isEnabled = false;
    }

    /**
     * Start the thread of server handler
     */
    public void start(){
        isEnabled = true;
        serverHandlerThread = new Thread(serverHandler);
        serverHandlerThread.start();
    }

    /**
     * Close the proxy controller -> send an interrupt to the serverHandler thread
     */
    public void close() {
        serverHandlerThread.interrupt();
        serverHandler.close();
        isEnabled = false;
    }
}
