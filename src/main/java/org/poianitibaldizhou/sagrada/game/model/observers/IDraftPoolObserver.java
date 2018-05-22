package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.Dice;

import java.util.List;

public interface IDraftPoolObserver {
    void notifyDiceAdded(Dice dice);
    void notifyDiceRemoved(Dice dice);
    void notifyDicesAdded(List<Dice> dices);
}
