package org.poianitibaldizhou.sagrada.game.model;

import java.util.ArrayList;
import java.util.List;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;

public class DraftPool {
    private List<Dice> dices;

    public DraftPool() {
        dices = new ArrayList<Dice>();
    }

    public List<Dice> getDices() {
        return new ArrayList<Dice>(dices);
    }

    public void useDice(Dice d) throws DiceNotFoundException {
        for(int i = 0; i < dices.size(); i++) {
            if(dices.get(i).equals(d)) {
                dices.remove(i);
                return;
            }
        }
        throw new DiceNotFoundException("DrafPool.useDice failed due to non existance of the draft in the pool");
    }
}
