package org.poianitibaldizhou.sagrada.lobby.model;

import java.rmi.Remote;

public interface ILobbyObserver extends Remote {
    void onUserJoin(User user);
    void onUserExit(User user);
    void onGameStart();
}
