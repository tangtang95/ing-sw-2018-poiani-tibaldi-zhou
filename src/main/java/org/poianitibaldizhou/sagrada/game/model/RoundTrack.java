package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class RoundTrack {

    private final List<List<Dice>> listOfDices;

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
                diceList.add(Dice.newInstance(d));
            newRoundTrack.addDicesToRound(diceList,i);
        }
        return newRoundTrack;
    }

    /**
     * Place a dice in the specified round of the roundTrack
     *
     * @param dices dice that need to be placed
     * @param round specified round
     */
    public void addDicesToRound(List<Dice> dices, int round){
        listOfDices.get(round).addAll(dices);
    }

    public void addDiceToRound(Dice dice, int round){
        listOfDices.get(round).add(dice);
    }

    /**
     * Return the list of dices of a given round
     *
     * @param round the round from where to get the dices
     * @return the list of dices of a given round
     */
    @Contract(pure = true)
    public List<Dice> getDices(int round){
        List<Dice> diceList = new ArrayList<>();
        for (Dice dice: listOfDices.get(round)) {
            diceList.add(Dice.newInstance(dice));
        }
        return diceList;
    }

    /**
     * Deletes a dice from the RoundTrack at a specified round
     *
     * @param round removes dice from this round
     * @param dice dice that needs to be removed
     * @throws IllegalArgumentException if dice is not present at specified round
     */
    public void removeDiceFromRoundTrack(int round, Dice dice) {
        if(!listOfDices.get(round).remove(dice))
            throw new IllegalArgumentException("Dice not present in round track");
    }

    /**
     * Replace a dice from the list of dices of "round" number with a new one
     *
     * @param oldDice the oldDice to replace
     * @param newDice the newDice to replace
     * @param round the round from where to replace the dice
     * @throws DiceNotFoundException if dice is not founded at the specified round
     */
    public void swapDice(Dice oldDice, Dice newDice, int round) throws DiceNotFoundException {
        boolean diceFounded = false;
        List<Dice> dices = listOfDices.get(round);
        for (int i = 0; i < dices.size(); i++) {
            if(dices.get(i).equals(oldDice)) {
                dices.set(i, newDice);
                diceFounded = true;
            }
        }
        if(!diceFounded)
            throw new DiceNotFoundException("oldDice not founded!");
    }
}
