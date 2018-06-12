package org.poianitibaldizhou.sagrada.game.model.board;

import edu.emory.mathcs.backport.java.util.Collections;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.*;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Dependency class with:
 * - Dice
 */
public class RoundTrackTest {

    private Dice dice1, dice2, dice3, dice4;

    private List<Dice> dices;
    private RoundTrack roundTrack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dice1 = new Dice(4, Color.BLUE);
        dice2 = new Dice(2, Color.RED);
        dice3 = new Dice(1, Color.BLUE);
        dice4 = new Dice(6, Color.YELLOW);
        dices = new ArrayList<>();
        dices.add(dice1);
        dices.add(dice2);
        dices.add(dice3);
        dices.add(dice4);
        roundTrack = new RoundTrack();
    }

    @Test
    public void testToJSON() {
        roundTrack.addDiceToRound(dice1, 3);
        roundTrack.addDiceToRound(dice2, 3);
        roundTrack.addDiceToRound(dice3, 2);
        String message = "{\"type\":\"roundTrack\",\"body\":{\"roundList\":[" +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":0}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":1}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":1}}]},\"round\":2}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":4}},{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":2}}]},\"round\":3}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":4}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":5}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":6}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":7}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":8}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":9}]}}";
        assertTrue(message.equals(roundTrack.toJSON().toJSONString()));

    }

    @Test
    public void testToObject() {
        String message = "{\"roundList\":[" +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":0}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":1}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":1}}]},\"round\":2}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":4}},{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":2}}]},\"round\":3}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":4}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":5}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":6}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":7}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":8}," +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]},\"round\":9}]}";
        org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        try {
            assertTrue((RoundTrack.toObject((JSONObject) jsonParser.parse(message))) == null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        roundTrack = null;
        dices = null;
    }

    @Test
    public void newInstance() {
        assertNull(RoundTrack.newInstance(null));

        RoundTrack tempRoundTrack = RoundTrack.newInstance(roundTrack);
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            assertEquals(roundTrack.getDices(i), tempRoundTrack.getDices(i));
        }
    }

    @Test(expected = Exception.class)
    public void testIllegalArgumentExceptionAddDice() {
        roundTrack.addDiceToRound(dice1, RoundTrack.FIRST_ROUND - 1);
    }

    @Test(expected = Exception.class)
    public void testIllegalArgumentExceptionAddDices() {
        roundTrack.addDicesToRound(dices, RoundTrack.NUMBER_OF_TRACK * 2);
    }

    @Test(expected = Exception.class)
    public void testIllegalArgumentExceptionSwapDice() throws Exception {
        roundTrack.swapDice(new Dice(2, Color.RED), new Dice(2, Color.GREEN),
                RoundTrack.NUMBER_OF_TRACK);
    }

    @Test(expected = Exception.class)
    public void testIllegalArgumentExceptionRemoveDice() {
        roundTrack.removeDiceFromRoundTrack(-5, new Dice(2, Color.PURPLE));
    }

    @Test
    public void addDicesToRound() {
        roundTrack.addDicesToRound(dices, 1);
        for (Dice dice : roundTrack.getDices(1)) {
            assertTrue(dices.contains(dice));
        }
    }

    @Test
    public void addDiceToRound() {
        roundTrack.addDiceToRound(dice1, 3);
        assertTrue(roundTrack.getDices(3).contains(dice1));
        roundTrack.addDiceToRound(dice2, 3);
        assertTrue(roundTrack.getDices(3).contains(dice1));
        assertTrue(roundTrack.getDices(3).contains(dice2));
        roundTrack.addDiceToRound(dice3, 2);
        assertTrue(roundTrack.getDices(3).contains(dice1));
        assertTrue(roundTrack.getDices(3).contains(dice2));
        assertTrue(roundTrack.getDices(2).contains(dice3));
        assertFalse(roundTrack.getDices(3).contains(dice3));
    }

    @Test
    public void removeDiceFromRoundTrack() {
        roundTrack.addDicesToRound(dices, 2);
        for (Dice dice : roundTrack.getDices(2)) {
            assertTrue(dices.contains(dice));
        }
        roundTrack.removeDiceFromRoundTrack(2, dice1);
        assertFalse(roundTrack.getDices(2).contains(dice1));

        try {
            roundTrack.removeDiceFromRoundTrack(1, dice1);
            fail("exception expected");
        } catch (IllegalArgumentException e) {
            assertNotEquals(null, e);
        }
    }

    @Test
    public void testEmptinessWithAddDice() {
        roundTrack.addDiceToRound(dice1, 1);
        assertEquals(false, roundTrack.isEmpty());

        roundTrack.removeDiceFromRoundTrack(1, dice1);
        assertEquals(true, roundTrack.isEmpty());
    }

    @Test
    public void testEmptinessWithAddDices() {
        roundTrack.addDicesToRound(dices, 1);
        assertEquals(false, roundTrack.isEmpty());

        roundTrack.addDicesToRound(dices, 2);
        assertEquals(false, roundTrack.isEmpty());

        for (Dice dice : dices) {
            roundTrack.removeDiceFromRoundTrack(1, dice);
        }
        assertEquals(false, roundTrack.isEmpty());

        for (Dice dice : dices) {
            roundTrack.removeDiceFromRoundTrack(2, dice);
        }
        assertEquals(true, roundTrack.isEmpty());
    }

    @Test(expected = DiceNotFoundException.class)
    public void testSwapDiceNoDicePresent() throws Exception {
        Dice notPresentDice = new Dice(3, Color.GREEN);
        roundTrack.addDicesToRound(dices, 1);
        roundTrack.swapDice(notPresentDice, dice1, 1);
    }

    @Test
    public void testSwapDice() throws Exception {
        Dice newDice = new Dice(3, Color.GREEN);
        roundTrack.addDicesToRound(dices, 1);
        roundTrack.swapDice(dice1, newDice, 1);
        dices.remove(dice1);
        dices.add(newDice);

        List<Dice> newRoundTrack = roundTrack.getDices(1);

        Collections.sort(dices, Comparator.comparingInt(Object::hashCode));
        Collections.sort(newRoundTrack, Comparator.comparingInt(Object::hashCode));

        assertEquals(dices, newRoundTrack);
    }

    @Test
    public void testNewInstance() {
        roundTrack.addDicesToRound(dices, 1);
        dices.remove(dice1);
        roundTrack.addDicesToRound(dices, 2);
        roundTrack.addDiceToRound(new Dice(5, Color.BLUE), 3);
        roundTrack.addDiceToRound(dice1, 4);
        roundTrack.addDiceToRound(dice2, 5);
        roundTrack.addDiceToRound(dice3, 6);
        roundTrack.addDicesToRound(dices, 7);
        roundTrack.addDiceToRound(dice1, 8);
        roundTrack.addDicesToRound(dices, 9);

        RoundTrack copiedRoundTrack = RoundTrack.newInstance(roundTrack);

        List<Dice> list1;
        List<Dice> list2;
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            list1 = roundTrack.getDices(i);
            list2 = copiedRoundTrack.getDices(i);
            Collections.sort(list1, Comparator.comparingInt(Object::hashCode));
            Collections.sort(list2, Comparator.comparingInt(Object::hashCode));
            assertEquals(list1, list2);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNull() {
        roundTrack.addDiceToRound(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddsNull() {
        roundTrack.addDicesToRound(null, 0);
    }
}
