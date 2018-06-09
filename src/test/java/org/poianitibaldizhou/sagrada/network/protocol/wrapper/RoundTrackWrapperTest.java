package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class RoundTrackWrapperTest {

    @DataPoint
    public static RoundTrackWrapper roundTrackWrapper;

    @Before
    public void setUp() throws Exception {
        List<DiceWrapper> dices = new ArrayList<>();
        dices.add(new DiceWrapper(ColorWrapper.PURPLE, 3));
        dices.add(new DiceWrapper(ColorWrapper.PURPLE, 3));
        dices.add(new DiceWrapper(ColorWrapper.PURPLE, 3));
        List<List<DiceWrapper>> dicesPerRound = new ArrayList<>();
        dicesPerRound.add(dices);
        dicesPerRound.add(dices);
        dicesPerRound.add(new ArrayList<>());
        dicesPerRound.add(new ArrayList<>());
        dicesPerRound.add(new ArrayList<>());
        dicesPerRound.add(new ArrayList<>());
        dicesPerRound.add(new ArrayList<>());
        dicesPerRound.add(new ArrayList<>());
        dicesPerRound.add(new ArrayList<>());
        dicesPerRound.add(new ArrayList<>());
        roundTrackWrapper = new RoundTrackWrapper(dicesPerRound);
    }

    @Test
    public void toObjectTest() {
        String message = "{\"roundList\":[" +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":3}},{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":3}},{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":3}}]},\"round\":0}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":3}},{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":3}},{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":3}}]},\"round\":1}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":2}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":3}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":4}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":5}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":6}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":7}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":8}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":9}]}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(roundTrackWrapper,
                    RoundTrackWrapper.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void toJsonTest() {
        assertEquals(null, roundTrackWrapper.toJSON());
    }
}