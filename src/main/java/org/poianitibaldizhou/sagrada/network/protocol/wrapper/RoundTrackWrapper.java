package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Immutable
public final class RoundTrackWrapper implements JSONable{

    /**
     * RoundTrackWrapper param for network protocol.
     */
    private static final String JSON_DICE_LIST = "diceList";
    private static final String JSON_ROUND_LIST = "roundList";

    private List<Collection<DiceWrapper>> dicesPerRound;

    public RoundTrackWrapper(List<Collection<DiceWrapper>> dicesPerRound) {
        this.dicesPerRound = new ArrayList<>(dicesPerRound);
    }

    /**
     *
     * @param round the round of the roundTrack to get dices
     * @return the collection of diceWrapper of the round given (empty list if the size of dicesPerRound < round)
     */
    public List<DiceWrapper> getDicesPerRound(int round){
        if(round >= dicesPerRound.size())
            throw new IllegalArgumentException(round + "has to be from 0 to " + size());
        return new ArrayList<>(dicesPerRound.get(round));
    }

    public int size() {
        return dicesPerRound.size();
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
        List<Collection<DiceWrapper>> rounds = new ArrayList<>();
        JSONArray roundTrack = (JSONArray) jsonObject.get(JSON_ROUND_LIST);

        for (Object o: roundTrack) {
            JSONObject diceList = (JSONObject)((JSONObject) o).get(JSON_DICE_LIST);
            JSONArray diceInRound = (JSONArray) diceList.get(SharedConstants.BODY);
            Collection<DiceWrapper> diceWrappers = new ArrayList<>();
            for (Object d : diceInRound) {
                JSONObject dice = (JSONObject) ((JSONObject) d).get(SharedConstants.BODY);
                diceWrappers.add(DiceWrapper.toObject(dice));
            }
            rounds.add(diceWrappers);
        }
        return new RoundTrackWrapper(rounds);
    }


}
