package org.poianitibaldizhou.sagrada.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INetworkObserver extends Remote {
    void ack(String ack) throws RemoteException;
    void err(String err) throws RemoteException;
}
