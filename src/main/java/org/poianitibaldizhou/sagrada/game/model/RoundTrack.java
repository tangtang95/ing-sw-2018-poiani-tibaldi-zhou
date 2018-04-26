package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public class RoundTrack {
    private final List<List<Dice>> roundTrack;
    private int currentRound;

    public static final int NUMBER_OF_TRACK = 10;

    /**
     * Constructor.
     * Create a new RoundTrack unique for each Game and set the initial value 1 to the currentRound
     */
    public RoundTrack() {
        this.roundTrack = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TRACK; i++) {
            roundTrack.add(new ArrayList<Dice>());
        }
        currentRound = 1;
    }

    /**
<<<<<<< HEAD
     * place the remainder dice of the DraftPool in the correct position of the roundTrack
     *
     * @param dices the dices which will be placed in the roundTrack
     * @param round current round
     */
    public void addDicesToCurrentRound(List<Dice> dices, int round){
        roundTrack.get(currentRound).addAll(dices);
    }

    /**
     * Return the list of dices of a given round
     *
     * @param round the round from where to get the dices
     * @return the list of dices of a given round
     */
    @Contract(pure = true)
    public List<Dice> getDices(int round){
        return roundTrack.get(round);
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
