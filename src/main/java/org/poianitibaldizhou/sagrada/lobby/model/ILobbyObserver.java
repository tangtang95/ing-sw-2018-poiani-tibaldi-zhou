package org.poianitibaldizhou.sagrada.lobby.model;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILobbyObserver extends Remote {
    void onUserJoin(User user) throws IOException;
    void onUserExit(User user) throws IOException;
    void onGameStart(String gameName) throws IOException;
    void onPing() throws IOException;
}
