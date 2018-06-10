package org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces;

import org.poianitibaldizhou.sagrada.lobby.model.User;

public interface INetworkFakeObserver {

    /**
     * Signal that a certain user has disconnected
     *
     * @param user disconnected user
     */
    void notifyDisconnection(User user);
}
