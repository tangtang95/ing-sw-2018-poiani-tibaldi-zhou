package org.poianitibaldizhou.sagrada.lobby.model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILobbyObserver extends Remote {
    void onUserJoin(User user) throws RemoteException;
    void onUserExit(User user) throws RemoteException;
    void onGameStart() throws RemoteException;
}
