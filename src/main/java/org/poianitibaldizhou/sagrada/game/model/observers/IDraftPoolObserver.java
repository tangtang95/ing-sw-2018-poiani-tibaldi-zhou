package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.Dice;

import java.util.List;

public interface IDraftPoolObserver {
    void onDiceAdd(Dice dice);
    void onDiceRemove(Dice dice);
    void onDicesAdd(List<Dice> dices);
    void onDraftPoolReroll(List<Dice> dices);
    void onDraftPoolClear();
}
