package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.Player;

import java.rmi.RemoteException;

public interface IToolCardObserver {

    void onTokenChange(int tokens);
    void onCardDestroy();

    void notifyNeedDice(Player player) throws RemoteException;
}
