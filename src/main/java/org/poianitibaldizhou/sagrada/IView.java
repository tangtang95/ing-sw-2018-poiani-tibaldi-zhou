package org.poianitibaldizhou.sagrada;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IView extends Remote {
    void ack(String ack) throws RemoteException;
    void err(String err) throws RemoteException;
}
