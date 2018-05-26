package org.poianitibaldizhou.sagrada.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPingController extends Remote {

    void ping(String token) throws RemoteException;
    void disconnect(String token) throws RemoteException;

}
