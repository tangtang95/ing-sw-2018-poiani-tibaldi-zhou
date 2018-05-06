package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;

import java.rmi.RemoteException;
import java.util.List;

public interface IToolCardObserver {

    void onTokenChange(int tokens);
    void onCardDestroy();

    void notifyNeedDice(Player player, List<Dice> diceList) throws RemoteException;
    void notifyNeedNewValue(Player player) throws RemoteException;
    void notifyNeedColor(Player player) throws RemoteException;
    void notifyNeedNewDeltaForDice(int diceValue, int value) throws RemoteException;
}
