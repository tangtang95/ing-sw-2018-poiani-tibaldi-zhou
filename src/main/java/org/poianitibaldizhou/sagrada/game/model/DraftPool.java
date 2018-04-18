package org.poianitibaldizhou.sagrada.game.model;

import java.util.ArrayList;
import java.util.List;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;

public class DraftPool {
    private List<Dice> dices;

    public DraftPool() {
        dices = new ArrayList<Dice>();
    }

    public List<Dice> getDices() {
        return new ArrayList<Dice>(dices);
    }

    public void addDices(List<Dice> dices) {
        this.dices.addAll(dices);
    }

    public void useDice(Dice d) throws DiceNotFoundException, EmptyCollectionException {
        if(dices.size() == 0)  {
            throw new EmptyCollectionException();
        }
        for(int i = 0; i < dices.size(); i++) {
            if(dices.get(i).equals(d)) {
                dices.remove(i);
                return;
            }
        }
        throw new DiceNotFoundException("DrafPool.useDice failed due to non existance of the draft in the pool");
    }

    @Override
    public String toString() {
        return dices.toString();
    }
}
