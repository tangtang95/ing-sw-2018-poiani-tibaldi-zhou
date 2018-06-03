package org.poianitibaldizhou.sagrada.lobby.model.observers;

import org.poianitibaldizhou.sagrada.lobby.model.User;

public interface ILobbyFakeObserver {

    /**
     * Notifies that an user has joined the lobby
     *
     * @param user user that joined the lobby
     * @ network communication error
     */
    void onUserJoin(User user);

    /**
     * Notifies that an user has left the lobby
     *
     * @param user user who left the lobby
     * @ network communication error
     */
    void onUserExit(User user);

    /**
     * Notifies that the game has started
     *
     * @param gameName name of the game
     * @ network communication error
     */
    void onGameStart(String gameName);

    /**
     * Pings an user to check his connection
     *
     * @ network communication error
     */
    void onPing();
}
