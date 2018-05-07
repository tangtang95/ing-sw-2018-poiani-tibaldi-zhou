package org.poianitibaldizhou.sagrada.game.model;

import java.util.ArrayList;
import java.util.List;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;

public class DraftPool {
    private List<Dice> dices;

    /**
     * Constructor.
     * Creates the DraftPool of the game.
     */
    public DraftPool() {
        dices = new ArrayList<>();
    }

    /**
     * Returns the current dices present in DraftPool.
     *
     * @return list of dices present in the DraftPool of the game
     */
    public List<Dice> getDices() {
        return new ArrayList<>(dices);
    }

    /**
     * Adds a list of dices to the DraftPool
     *
     * @param dices the list of dices that needs to be added
     */
    public void addDices(List<Dice> dices) {
        this.dices.addAll(dices);
    }


    /**
     * Add one dice to the DraftPool
     *
     * @param dice the dice that needs to be added
     */
    public void addDice(Dice dice) {
        this.dices.add(dice);
    }

    /**
     * Uses a dice presents in the DrafPool thus removing it.
     *
     * @param d dice used
     * @throws DiceNotFoundException if d is not presente in the DraftPool
     * @throws EmptyCollectionException if the DraftPool is empty
     */
    public void useDice(Dice d) throws DiceNotFoundException, EmptyCollectionException {
        if(dices.isEmpty())  {
            throw new EmptyCollectionException();
        }
        for(int i = 0; i < dices.size(); i++) {
            if(dices.get(i).equals(d)) {
                dices.remove(i);
                return;
            }
        }
        throw new DiceNotFoundException("DraftPool.useDice() failed due to non existence of the dice in the pool");
    }

    /**
     * Remove every dices in the draftPool
     *
     */
    public void clearPool() {
        dices.clear();
    }

    @Override
    public String toString() {
        return dices.toString();
    }

    public int size(){return dices.size();}

}
