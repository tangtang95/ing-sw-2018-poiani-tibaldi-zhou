package org.poianitibaldizhou.sagrada;

import java.io.IOException;
import java.rmi.Remote;

/**
 * OVERVIEW: Represents a client view, with the basic methods that allows the server to communicate with it
 */
public interface IView extends Remote {

    /**
     * Communicate to the client an ack message
     *
     * @param ack string containing the ack message
     * @throws IOException network communication error
     */
    void ack(String ack) throws IOException;

    /**
     * Communicate to the client an error message
     *
     * @param err string containing the error message
     * @throws IOException network communication error
     */
    void err(String err) throws IOException;

    /**
     * Ping the clients, thus allowing to check if the connection is still up
     *
     * @throws IOException network communication error
     */
    void ping() throws IOException;
}
