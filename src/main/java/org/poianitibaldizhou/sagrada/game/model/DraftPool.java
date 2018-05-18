package org.poianitibaldizhou.sagrada.game.model;

import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.AddDiceToDraftPool;

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
    @Contract(pure = true)
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
     * Uses a dice presents in the DraftPool thus removing it.
     *
     * @param d dice used
     * @throws DiceNotFoundException    if d is not present in the DraftPool
     * @throws EmptyCollectionException if the DraftPool is empty
     */
    public void useDice(Dice d) throws DiceNotFoundException, EmptyCollectionException {
        if (dices.isEmpty()) {
            throw new EmptyCollectionException();
        }
        for (int i = 0; i < dices.size(); i++) {
            if (dices.get(i).equals(d)) {
                dices.remove(i);
                return;
            }
        }
        throw new DiceNotFoundException("DraftPool.useDice() failed due to non existence of the dice in the pool");
    }

    public void reRollDices() {
        Random random = new Random();
        for (int i = 0; i < dices.size(); i++) {
            dices.set(i, new Dice(random.nextInt(Dice.MAX_VALUE) + 1, dices.get(i).getColor()));
        }
    }

    /**
     * Remove every dices in the draftPool
     */
    public void clearPool() {
        dices.clear();
    }


    public static DraftPool newInstance(DraftPool draftPool) {
        if (draftPool == null)
            return null;
        DraftPool newDraftPool = new DraftPool();
        List<Dice> diceList = new ArrayList<>(draftPool.getDices());
        newDraftPool.addDices(diceList);
        return newDraftPool;
    }


    @Contract(pure = true)
    public int size() {
        return dices.size();
    }

    @Override
    public String toString() {
        return dices.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof DraftPool)) {
            return false;
        }
        DraftPool draftPool = (DraftPool) o;
        return getDices().equals(draftPool.getDices());
    }

    @Override
    public int hashCode() {
        return Objects.hash(DraftPool.class);
    }
}
