package org.poianitibaldizhou.sagrada.lobby.view;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILobbyView extends Remote {
    void ack(String ack) throws RemoteException;
    void err(String err) throws RemoteException;
}
