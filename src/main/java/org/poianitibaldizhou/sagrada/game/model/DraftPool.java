package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.observers.IDraftPoolObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DraftPool {
    private List<Dice> dices;
    private List<IDraftPoolObserver> observerList;

    /**
     * Constructor.
     * Creates the DraftPool of the game.
     */
    public DraftPool() {
        dices = new ArrayList<>();
        observerList = new ArrayList<>();
    }

    /**
     * Returns the list of the observer of the draftpool
     *
     * @return list of the observers listening to the draftpool
     */
    @Contract(pure = true)
    public List<IDraftPoolObserver> getObserverList() {
        return observerList;
    }

    public void attachObserver(@NotNull IDraftPoolObserver observer) {
        observerList.add(observer);
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
     * Adds a list of dices to the DraftPool.
     * It also notify the observers that some dices are added
     *
     * @param dices the list of dices that needs to be added
     * @throws NullPointerException if dices is null
     */
    public void addDices(@NotNull List<Dice> dices) {
        this.dices.addAll(dices);
        observerList.forEach(obs -> obs.onDicesAdd(dices));
    }

    /**
     * Add one dice to the DraftPool
     *
     * @param dice the dice that needs to be added
     * @throws NullPointerException if dice is null
     */
    public void addDice(@NotNull Dice dice) {
        this.dices.add(dice);
        observerList.forEach(obs -> obs.onDiceAdd(dice));
    }

    /**
     * Uses a dice presents in the DraftPool thus removing it.
     *
     * @param dice dice used
     * @throws DiceNotFoundException    if d is not present in the DraftPool
     * @throws EmptyCollectionException if the DraftPool is empty
     * @throws NullPointerException     if dice is null
     */
    public void useDice(@NotNull Dice dice) throws DiceNotFoundException, EmptyCollectionException {
        if (dices.isEmpty()) {
            throw new EmptyCollectionException();
        }
        for (int i = 0; i < dices.size(); i++) {
            if (dices.get(i).equals(dice)) {
                dices.remove(i);
                observerList.forEach(obs -> obs.onDiceRemove(dice));
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
        observerList.forEach(obs -> obs.onDraftPoolReroll(dices));
    }

    /**
     * Remove every dices in the draftPool
     */
    public void clearPool() {
        dices.clear();
        observerList.forEach(IDraftPoolObserver::onDraftPoolClear);
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
        if (!(o instanceof DraftPool)) {
            return false;
        }
        DraftPool draftPool = (DraftPool) o;
        List<Dice> comparingList = draftPool.getDices();
        int[][] counter = new int[Dice.MAX_VALUE][Color.values().length];
        for (Dice d : dices) {
            counter[d.getNumber() - 1][d.getColor().ordinal()] += 1;
        }
        for (Dice d : comparingList) {
            counter[d.getNumber() - 1][d.getColor().ordinal()] -= 1;
        }
        for (int i = 0; i < Dice.MAX_VALUE; i++) {
            for (int j = 0; j < Color.values().length; j++)
                if (counter[i][j] != 0)
                    return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(DraftPool.class);
    }
}
