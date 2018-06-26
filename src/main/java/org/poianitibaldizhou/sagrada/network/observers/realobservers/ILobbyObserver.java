package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

public interface ILobbyObserver extends Remote {

    /**
     * Notifies that an user has joined the lobby
     *
     * @param user user that joined the lobby
     * @throws IOException network communication error
     */
    void onUserJoin(String user) throws IOException;

    /**
     * Notifies that an user has left the lobby
     *
     * @param user user who left the lobby
     * @throws IOException network communication error
     */
    void onUserExit(String user) throws IOException;

    /**
     * Notifies that the game has started
     *
     * @param gameName name of the game
     * @throws IOException network communication error
     */
    void onGameStart(String gameName) throws IOException;

    /**
     * Pings an user to check his connection
     *
     * @throws IOException network communication error
     */
    void onPing() throws IOException;
}
