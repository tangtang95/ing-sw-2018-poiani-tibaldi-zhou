package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.observers.IDraftPoolObserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

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

    // GETTER

    /**
     * Returns the list of the observer of the draftpool
     * Another list is created for this task, but the single elements are not deep
     * copied.
     *
     * @return list of the observers listening to the draftpool
     */
    @Contract(pure = true)
    public List<IDraftPoolObserver> getObserverList() {
        return new ArrayList<>(observerList);
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
     * Return the list of dice with the same color of the parameter passed
     *
     * @param color the color requirement
     * @return the list of dice with the color given
     */
    @Contract(pure = true)
    public List<Dice> getDices(final Color color) {
        return dices.stream().filter(dice -> dice.getColor() == color).collect(Collectors.toList());
    }

    // MODIFIERS
    public void attachObserver(@NotNull IDraftPoolObserver observer) {
        observerList.add(observer);
    }

    /**
     * Adds a list of dices to the DraftPool.
     * It also notify the observers that some dices are added
     *
     * @param dices the list of dices that needs to be added
     * @throws NullPointerException if dices is null
     * @throws RemoteException      network error
     */
    public void addDices(@NotNull List<Dice> dices) throws RemoteException {
        this.dices.addAll(dices);
        for (IDraftPoolObserver obs : observerList) obs.onDicesAdd(dices);
    }

    /**
     * Add one dice to the DraftPool
     *
     * @param dice the dice that needs to be added
     * @throws NullPointerException if dice is null
     * @throws RemoteException      network error
     */
    public void addDice(@NotNull Dice dice) throws RemoteException {
        this.dices.add(dice);
        for (IDraftPoolObserver obs : observerList) obs.onDiceAdd(dice);
    }

    /**
     * Uses a dice presents in the DraftPool thus removing it.
     *
     * @param dice dice used
     * @throws DiceNotFoundException    if d is not present in the DraftPool
     * @throws EmptyCollectionException if the DraftPool is empty
     * @throws NullPointerException     if dice is null
     * @throws RemoteException          network error
     */
    public void useDice(@NotNull Dice dice) throws DiceNotFoundException, EmptyCollectionException, RemoteException {
        if (dices.isEmpty()) {
            throw new EmptyCollectionException();
        }
        for (int i = 0; i < dices.size(); i++) {
            if (dices.get(i).equals(dice)) {
                dices.remove(i);
                for (IDraftPoolObserver obs : observerList) obs.onDiceRemove(dice);
                return;
            }
        }
        throw new DiceNotFoundException("DraftPool.useDice() failed due to non existence of the dice in the pool");
    }

    /**
     * Re-roll every dice inside the draftPool (the color doesn't change, only the number of the dice can change)
     *
     * @throws RemoteException network error
     */
    public void reRollDices() throws RemoteException {
        Random random = new Random();
        for (int i = 0; i < dices.size(); i++) {
            dices.set(i, new Dice(random.nextInt(Dice.MAX_VALUE) + 1, dices.get(i).getColor()));
        }
        for (IDraftPoolObserver obs : observerList) obs.onDraftPoolReroll(dices);
    }

    /**
     * Remove every dices in the draftPool
     *
     * @throws RemoteException network error
     */
    public void clearPool() throws RemoteException {
        dices.clear();
        for (IDraftPoolObserver obs : observerList) obs.onDraftPoolClear();
    }

    /**
     * Creates a new instance of draftPool. Observers are copied for references.
     *
     * @param draftPool draftPool that needs to be copied
     * @return new instance with the same elements of draftPool
     * @throws RemoteException network error
     */
    public static DraftPool newInstance(DraftPool draftPool) throws RemoteException {
        if (draftPool == null)
            return null;
        DraftPool newDraftPool = new DraftPool();
        List<Dice> diceList = new ArrayList<>(draftPool.getDices());
        newDraftPool.addDices(diceList);
        draftPool.getObserverList().forEach(newDraftPool::attachObserver);
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
