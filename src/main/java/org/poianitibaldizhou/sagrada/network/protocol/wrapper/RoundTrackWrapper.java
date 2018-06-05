package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
    public Collection<DiceWrapper> getDicesPerRound(int round){
        if(round >= dicesPerRound.size())
            throw new IllegalArgumentException(round + "has to be from 0 to " + size());
        return new ArrayList<>(dicesPerRound.get(round));
    }

    public int size() {
        return dicesPerRound.size();
    }

    public Stream<Collection<DiceWrapper>> stream(){
        return dicesPerRound.stream();
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
    @Override
    public Object toObject(JSONObject jsonObject) {
        ClientGetMessage parser = new ClientGetMessage();
        List<Collection<DiceWrapper>> rounds = new ArrayList<>();
        JSONArray roundTrack = (JSONArray) jsonObject.get(JSON_ROUND_LIST);

        for (Object o: roundTrack) {
            JSONObject diceList = (JSONObject) o;
            try {
                rounds.add(parser.getDiceList((String) diceList.get(JSON_DICE_LIST)));
            } catch (IOException e) {
                return null;
            }
        }
        return new RoundTrackWrapper(rounds);
    }


}
