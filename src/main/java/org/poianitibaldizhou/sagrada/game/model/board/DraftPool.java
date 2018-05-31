package org.poianitibaldizhou.sagrada.game.model.board;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.DraftPoolFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class DraftPool implements Serializable, JSONable {
    private final List<Dice> dices;
    private final transient Map<String, DraftPoolFakeObserver> observerMap;


    /**
     * Constructor.
     * Creates the DraftPool of the game.
     */
    public DraftPool() {
        dices = new ArrayList<>();
        observerMap = new HashMap<>();
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
    public Map<String, DraftPoolFakeObserver> getObserverMap() {
        return new HashMap<>(observerMap);
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
    public void attachObserver(String token, @NotNull DraftPoolFakeObserver observer) {
        observerMap.put(token, observer);
    }

    public void detachObserver(String token) {
        observerMap.remove(token);
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
        observerMap.forEach((key, value) -> value.onDicesAdd(dices));
    }

    /**
     * Add one dice to the DraftPool
     *
     * @param dice the dice that needs to be added
     * @throws NullPointerException if dice is null
     */
    public void addDice(@NotNull Dice dice) {
        this.dices.add(dice);
        observerMap.forEach((key, value) -> value.onDiceAdd(dice));
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
                observerMap.forEach((key, value) -> value.onDiceRemove(dice));
                return;
            }
        }
        throw new DiceNotFoundException("DraftPool.useDice() failed due to non existence of the dice in the pool");
    }

    /**
     * Re-roll every dice inside the draftPool (the color doesn't change, only the number of the dice can change)
     */
    public void reRollDices() {
        Random random = new Random();
        for (int i = 0; i < dices.size(); i++) {
            dices.set(i, new Dice(random.nextInt(Dice.MAX_VALUE) + 1, dices.get(i).getColor()));
        }

        observerMap.forEach((key, value) -> value.onDraftPoolReroll(dices));
    }

    /**
     * Remove every dices in the draftPool
     */
    public void clearPool() {
        dices.clear();
        observerMap.forEach((key, value) -> value.onDraftPoolClear());
    }

    /**
     * Creates a new instance of draftPool. Observers are copied for references.
     *
     * @param draftPool draftPool that needs to be copied
     * @return new instance with the same elements of draftPool
     */
    public static DraftPool newInstance(DraftPool draftPool) {
        if (draftPool == null)
            return null;
        DraftPool newDraftPool = new DraftPool();
        List<Dice> diceList = new ArrayList<>(draftPool.getDices());
        newDraftPool.addDices(diceList);
        newDraftPool.observerMap.putAll(draftPool.getObserverMap());
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
        return Objects.hash(DraftPool.class, dices);
    }

    @Override
    public JSONObject toJSON() {
        JSONArray diceListJson = new JSONArray();
        JSONObject dice;
        JSONObject draftPoolJson = new JSONObject();

        for(Dice d : this.getDices()) {
            dice = d.toJSON();
            diceListJson.add(dice);
        }

        draftPoolJson.putIfAbsent("diceList", diceListJson);

        return draftPoolJson;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return null;
    }
}
