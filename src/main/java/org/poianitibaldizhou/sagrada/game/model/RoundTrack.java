package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mockito.internal.matchers.Null;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoundTrack {

    private final List<List<Dice>> listOfDices;
    private int numberOfDices;

    public static final int NUMBER_OF_TRACK = 10;
    public static final int FIRST_ROUND = 0;
    public static final int LAST_ROUND = NUMBER_OF_TRACK - 1;

    /**
     * Constructor.
     * Create a new RoundTrack unique for each Game and set the initial value 1 to the currentRound
     */
    public RoundTrack() {
        this.listOfDices = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TRACK; i++) {
            listOfDices.add(new ArrayList<>());
        }
        numberOfDices = 0;
    }

    /**
     * Copy constructor.
     * Place a dice in the specified round of the roundTrack
     *
     * @param roundTrack the roundTrack to copy
     * @return copy of roundTrack
     */
    public static RoundTrack newInstance(RoundTrack roundTrack) {
        if (roundTrack == null)
            return null;
        RoundTrack newRoundTrack = new RoundTrack();
        List<Dice> diceList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TRACK; i++) {
            for (Dice d : roundTrack.listOfDices.get(i))
                diceList.add(new Dice(d.getNumber(), d.getColor()));
            newRoundTrack.addDicesToRound(diceList, i);
        }
        return newRoundTrack;
    }


    /**
     * Returns true if the RoundTrack doesn't contain any dice, false otherwise
     *
     * @return true if the RoundTrack doesn't contain any dice, false otherwise
     */
    @Contract(pure = true)
    public boolean isEmpty() {
        return numberOfDices == 0;
    }

    /**
     * Place a list of dices in the specified round of the roundTrack
     *
     * @param dices dice that need to be placed
     * @param round specified round
     */
    public void addDicesToRound(List<Dice> dices, int round) {
        if (dices == null)
            throw new NullPointerException();
        if (dices.size() > 0)
            numberOfDices += dices.size();
        listOfDices.get(round).addAll(dices);
    }

    /**
     * Place a dice in the specified round of the roundTrack
     *
     * @param dice  dice that need to be placed
     * @param round specified round
     */
    public void addDiceToRound(Dice dice, int round) {
        if (dice == null)
            throw new NullPointerException();
        listOfDices.get(round).add(dice);
        numberOfDices += 1;
    }

    /**
     * Return the list of dices of a given round
     *
     * @param round the round from where to get the dices
     * @return the list of dices of a given round
     */
    @Contract(pure = true)
    public List<Dice> getDices(int round) {
        List<Dice> diceList = new ArrayList<>();
        for (Dice dice : listOfDices.get(round)) {
            diceList.add(new Dice(dice.getNumber(), dice.getColor()));
        }
        return diceList;
    }

    /**
     * Deletes a dice from the RoundTrack at a specified round
     *
     * @param round removes dice from this round
     * @param dice  dice that needs to be removed
     * @throws IllegalArgumentException if dice is not present at specified round
     */
    public void removeDiceFromRoundTrack(int round, Dice dice) {
        if (!listOfDices.get(round).remove(dice))
            throw new IllegalArgumentException("Dice not present in round track");
        numberOfDices -= 1;
    }

    /**
     * Replace a dice from the list of dices of "round" number with a new one
     *
     * @param oldDice the oldDice to replace
     * @param newDice the newDice to replace
     * @param round   the round from where to replace the dice
     * @throws DiceNotFoundException if dice is not founded at the specified round
     */
    public void swapDice(Dice oldDice, Dice newDice, int round) throws DiceNotFoundException {
        boolean diceFounded = false;
        List<Dice> dices = listOfDices.get(round);
        for (int i = 0; i < dices.size(); i++) {
            if (dices.get(i).equals(oldDice)) {
                dices.set(i, newDice);
                diceFounded = true;
            }
        }
        if (!diceFounded)
            throw new DiceNotFoundException("oldDice not founded!");
    }
}
