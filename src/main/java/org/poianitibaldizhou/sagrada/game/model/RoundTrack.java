package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RoundTrack {

    private final List<List<Dice>> listOfDices;
    private int currentRound;

    public static final int NUMBER_OF_TRACK = 10;

    /**
     * Constructor.
     * Create a new RoundTrack unique for each Game and set the initial value 1 to the currentRound
     */
    public RoundTrack() {
        this.listOfDices = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TRACK; i++) {
            listOfDices.add(new ArrayList<>());
        }
        currentRound = 1;
    }

    /**
     * Copy constructor.
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
        newRoundTrack.currentRound = roundTrack.currentRound;
        return newRoundTrack;
    }

    /**
<<<<<<< HEAD
     * place the remainder dice of the DraftPool in the correct position of the roundTrack
     *
     * @param dices the dices which will be placed in the roundTrack
     */
    public void addDicesToRound(List<Dice> dices, int round){
        listOfDices.get(round).addAll(dices);
    }

    /**
     * Return the list of dices of a given round
     *
     * @param round the round from where to get the dices
     * @return the list of dices of a given round
     */
    @Contract(pure = true)
    public List<Dice> getDices(int round){
        return listOfDices.get(round);
    }

    /**
     * Increase the currentRound by 1 (called at the end of RoundEndState)
     *
     */
    public void nextRound() {
        currentRound++;
    }

    @Contract(pure = true)
    public int getCurrentRound() {
        return currentRound;
    }

}
