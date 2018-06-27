package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Copy class of RoundTrack in the game model.
 */
@Immutable
public final class RoundTrackWrapper implements JSONable{

    /**
     * RoundTrackWrapper param for network protocol.
     */
    private static final String JSON_DICE_LIST = "diceList";
    private static final String JSON_ROUND_LIST = "roundList";

    /**
     * RoundTrack parameter.
     */
    public static final int NUMBER_OF_TRACK = 10;

    /**
     * List of diceWrapper per rounds.
     */
    private List<List<DiceWrapper>> dicesForRound;

    /**
     * Constructor.
     *
     * @param dicesPerRound list of diceWrapper per round.
     */
    public RoundTrackWrapper(List<List<DiceWrapper>> dicesPerRound) {
        this.dicesForRound = new ArrayList<>(dicesPerRound);

    }

    /**
     *
     * @param round the round of the roundTrack to get dices
     * @return the collection of diceWrapper of the round given (empty list if the size of dicesForRound < round)
     */
    public List<DiceWrapper> getDicesForRound(int round){
        if(round < 0 || round >= NUMBER_OF_TRACK)
            throw new IllegalArgumentException(round + " has to be from 0 to " + NUMBER_OF_TRACK);
        if(dicesForRound.size() <= round)
            return new ArrayList<>();
        return new ArrayList<>(dicesForRound.get(round));
    }

    /**
     * Convert a roundTrackWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        /*This method is empty because the client never send a publicObjectiveCard*/
        return null;
    }

    /**
     * Convert a json string in a roundTrackWrapper object.
     *
     * @param jsonObject a JSONObject that contains a roundTrack.
     * @return a roundTrackWrapper object or null.
     */
    public static RoundTrackWrapper toObject(JSONObject jsonObject) {
        List<List<DiceWrapper>> rounds = new ArrayList<>();
        JSONArray roundTrack = (JSONArray) jsonObject.get(JSON_ROUND_LIST);

        for (Object o: roundTrack) {
            JSONObject diceList = (JSONObject)((JSONObject) o).get(JSON_DICE_LIST);
            JSONArray diceInRound = (JSONArray) diceList.get(SharedConstants.BODY);
            List<DiceWrapper> diceWrappers = new ArrayList<>();
            for (Object d : diceInRound) {
                JSONObject dice = (JSONObject) ((JSONObject) d).get(SharedConstants.BODY);
                diceWrappers.add(DiceWrapper.toObject(dice));
            }
            rounds.add(diceWrappers);
        }
        return new RoundTrackWrapper(rounds);
    }

    /**
     * @param o the other object to compare.
     * @return true if the RoundTrackWrapper is the same object or if the dicePeRound is the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoundTrackWrapper)) return false;
        RoundTrackWrapper that = (RoundTrackWrapper) o;
        Boolean hasSameDices = true;
        for (int i = 0; i < NUMBER_OF_TRACK; i++) {
            for (int j = 0; j < getDicesForRound(i).size(); j++) {
                if (!getDicesForRound(i).get(j).equals(that.getDicesForRound(i).get(j))) {
                    hasSameDices = false;
                    break;
                }
            }
            if (!hasSameDices)
                break;
        }
        return hasSameDices;
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(dicesForRound);
    }
}
