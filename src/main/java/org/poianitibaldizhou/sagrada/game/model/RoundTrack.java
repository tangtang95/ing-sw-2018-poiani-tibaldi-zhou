package org.poianitibaldizhou.sagrada.game.model;

import java.util.List;

public class RoundTrack {
    private List<Dice>[] roundTrack;

    public static final int NUMBER_OF_TRACK = 10;

    public RoundTrack() {
        this.roundTrack = new List[NUMBER_OF_TRACK];
    }

    /**
     * place the remainder dice of the DraftPool in the correct position of the roundTrack
     *
     * @param dices the dices which will be placed in the roundTrack
     * @param round current round
     */
    public void addDice(List<Dice> dices, int round){
        roundTrack[round] = dices;
    }

    public List<Dice>[] getRoundTrack() {
        return roundTrack;
    }

    public List<Dice> getDices(int round){
        return roundTrack[round];
    }
}
