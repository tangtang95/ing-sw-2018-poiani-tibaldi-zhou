package org.poianitibaldizhou.sagrada.game.model.board;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IRoundTrackFakeObserver;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OVERVIEW: A round track contains a list of dices for each single round that it contains.
 * Order in the dice list does not matter.
 */
public class RoundTrack implements JSONable {

    private final List<List<Dice>> listOfDices;
    private final Map<String, IRoundTrackFakeObserver> observerMap;
    private int numberOfDices;

    public static final int NUMBER_OF_TRACK = 10;
    public static final int FIRST_ROUND = 0;
    public static final int LAST_ROUND = NUMBER_OF_TRACK - 1;

    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Round must be in [" + LAST_ROUND + ",  " + FIRST_ROUND + "]. " +
            "Round specified:  ";

    /**
     * RoundTrack param for network protocol.
     */
    private static final String JSON_ROUND = "round";
    private static final String JSON_DICE_LIST = "diceList";
    private static final String JSON_ROUND_LIST = "roundList";

    /**
     * Constructor.
     * Create a new RoundTrack unique for each Game and set the initial value 1 to the currentRound
     */
    public RoundTrack() {
        this.listOfDices = new ArrayList<>();
        this.observerMap = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_TRACK; i++) {
            listOfDices.add(new ArrayList<>());
        }
        numberOfDices = 0;
    }

    /**
     * Copy constructor.
     * It copies the observers for references.
     *
     * @param roundTrack the roundTrack to copy
     * @return copy of roundTrack
     */
    public static RoundTrack newInstance(RoundTrack roundTrack) {
        if (roundTrack == null)
            return null;

        RoundTrack newRoundTrack = new RoundTrack();

        for (int i = 0; i < NUMBER_OF_TRACK; i++) {
            for (Dice d : roundTrack.listOfDices.get(i))
                newRoundTrack.addDiceToRound(new Dice(d.getNumber(), d.getColor()), i);
        }

        newRoundTrack.observerMap.putAll(roundTrack.getObserverMap());

        return newRoundTrack;
    }

    //GETTER

    /**
     * Returns the a copied list of the observer. The single elements are not copied.
     *
     * @return copied observer list
     */
    public Map<String, IRoundTrackFakeObserver> getObserverMap() {
        return new HashMap<>(observerMap);
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
     * Return the list of dices of a given round.
     * It deep copies the single elements present in RoundTrack and also the list, so it is totally "safe".
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

    // MODIFIERS

    /**
     * Place a list of dices in the specified round of the roundTrack.
     * Notifies the observers that a list of dices has been added to a certain round.
     *
     * @param dices dice that need to be placed
     * @param round specified round
     * @throws IllegalArgumentException if round exceeds [FIRST_ROUND, LAST_ROUND]
     */
    public void addDicesToRound(@NotNull List<Dice> dices, int round) {
        if (isAcceptedRound(round))
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + round);
        if (!dices.isEmpty()) {
            numberOfDices += dices.size();
            listOfDices.get(round).addAll(dices);
            observerMap.forEach((key, value) -> value.onDicesAddToRound(dices, round));
        }
    }

    /**
     * Place a dice in the specified round of the roundTrack.
     * Notifies the observers that a dice's been added to a certain round.
     *
     * @param dice  dice that need to be placed
     * @param round specified round
     * @throws IllegalArgumentException if round exceeds [FIRST_ROUND, LAST_ROUND]
     */
    public void addDiceToRound(@NotNull Dice dice, int round) {
        if (isAcceptedRound(round))
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + round);

        listOfDices.get(round).add(dice);
        numberOfDices += 1;
        observerMap.forEach((key, value) -> value.onDiceAddToRound(dice, round));
    }

    /**
     * Deletes a dice from the RoundTrack at a specified round
     * It notifies the observers that a certain dice has been removed from a specified round.
     *
     * @param round removes dice from this round
     * @param dice  dice that needs to be removed
     * @throws IllegalArgumentException if dice is not present at specified round or if the round specified
     *                                  exceeds [FIRST_ROUND, LAST_ROUND]
     */
    public void removeDiceFromRoundTrack(int round, @NotNull Dice dice) {
        if (isAcceptedRound(round))
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + round);
        if (!listOfDices.get(round).remove(dice))
            throw new IllegalArgumentException("Dice not present in round track");
        numberOfDices -= 1;
        observerMap.forEach((key, value) -> value.onDiceRemoveFromRound(dice, round));
    }

    /**
     * Replace a dice from the list of dices of "round" number with a new one
     * It notifies the observers that oldDice has been swapped with newDice: this operations happens at round.
     *
     * @param oldDice the oldDice to replace
     * @param newDice the newDice to replace
     * @param round   the round from where to replace the dice
     * @throws DiceNotFoundException    if dice is not founded at the specified round
     * @throws IllegalArgumentException if round exceeds [FIRST_ROUND, LAST_ROUND]
     */
    public void swapDice(Dice oldDice, Dice newDice, int round) throws DiceNotFoundException {
        if (isAcceptedRound(round))
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + round);

        boolean diceFounded = false;
        List<Dice> dices = listOfDices.get(round);
        for (int i = 0; i < dices.size(); i++) {
            if (dices.get(i).equals(oldDice)) {
                dices.set(i, newDice);
                diceFounded = true;
                observerMap.forEach((key, value) -> value.onDiceSwap(oldDice, newDice, round));
                break;
            }
        }

        if (!diceFounded)
            throw new DiceNotFoundException("oldDice not founded!");
    }

    public void attachObserver(String token, IRoundTrackFakeObserver roundTrackObserver) {
        observerMap.put(token, roundTrackObserver);
    }

    public void detachObserver(String token) {
        observerMap.remove(token);
    }

    /**
     * Returns true if round belongs to [FIRST_ROUND, LAST_ROUND], false otherwise
     *
     * @param round value to check
     * @return true if round is in the defined interval, false otherwise
     */
    private boolean isAcceptedRound(int round) {
        return round < FIRST_ROUND || round > LAST_ROUND;
    }

    /**
     * Convert a roundTrack in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONArray rounds = new JSONArray();
        JSONObject roundTrackJson = new JSONObject();

        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            JSONObject collection = new JSONObject();
            JSONArray listOfDicePerRoundJSON = new JSONArray();
            JSONObject  roundJSON = new JSONObject();
            for (Dice d : this.getDices(i)) {
                listOfDicePerRoundJSON.add(d.toJSON());
            }
            collection.put(SharedConstants.TYPE, SharedConstants.COLLECTION);
            collection.put(SharedConstants.BODY,listOfDicePerRoundJSON);
            roundJSON.put(JSON_ROUND, i);
            roundJSON.put(JSON_DICE_LIST, collection);
            rounds.add(roundJSON);
        }
        roundTrackJson.put(JSON_ROUND_LIST, rounds);
        main.put(SharedConstants.TYPE, SharedConstants.ROUND_TRACK);
        main.put(SharedConstants.BODY,roundTrackJson);
        return main;
    }

    /**
     * Convert a json string in a roundTrack object.
     *
     * @param jsonObject a JSONObject that contains a roundTrack.
     * @return a roundTrack object.
     */
    public static RoundTrack toObject(JSONObject jsonObject) {
        /*This method is empty because the client never send a publicObjectiveCard*/
        return null;
    }
}
