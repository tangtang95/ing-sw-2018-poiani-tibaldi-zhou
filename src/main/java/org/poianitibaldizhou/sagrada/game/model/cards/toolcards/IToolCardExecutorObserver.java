package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;

import java.rmi.RemoteException;
import java.util.List;

public interface IToolCardExecutorObserver {
    void notifyNeedDice(Player player, List<Dice> diceList) throws RemoteException;
    void notifyNeedNewValue(Player player) throws RemoteException;
    void notifyNeedColor(Player player) throws RemoteException;
    void notifyNeedNewDeltaForDice(int diceValue, int value) throws RemoteException;
    void notifyNeedDiceFromRoundTrack(Player player, RoundTrack roundTrack) throws RemoteException;
    void notifyNeedPosition(Player player) throws RemoteException;
    void notifyNeedDicePositionOfCertainColor(Player player, Color color) throws RemoteException;
}
