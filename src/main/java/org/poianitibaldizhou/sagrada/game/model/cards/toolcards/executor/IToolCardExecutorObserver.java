package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface IToolCardExecutorObserver {
    void notifyNeedDice(List<Dice> diceList) throws RemoteException;
    void notifyNeedNewValue() throws RemoteException;
    void notifyNeedColor(Set<Color> colors) throws RemoteException;
    void notifyNeedNewDeltaForDice(int diceValue, int value) throws RemoteException;
    void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack) throws RemoteException;
    void notifyNeedPosition() throws RemoteException;
    void notifyNeedDicePositionOfCertainColor(Color color) throws RemoteException;
}
