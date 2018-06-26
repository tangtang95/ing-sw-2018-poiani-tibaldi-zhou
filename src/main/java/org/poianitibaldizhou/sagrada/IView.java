package org.poianitibaldizhou.sagrada;

import java.io.IOException;
import java.rmi.Remote;

public interface IView extends Remote {
    void ack(String ack) throws IOException;
    void err(String err) throws IOException;
    void ping() throws IOException;
}
