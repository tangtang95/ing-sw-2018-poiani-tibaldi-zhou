package org.poianitibaldizhou.sagrada.game.model;

import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import java.util.ArrayList;
import java.util.Arrays;
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

    @After
    public void tearDown() {
        roundTrack = null;
        dices = null;
    }

    @Test
    public void newInstance() throws Exception {
    }

    @Test
    public void addDicesToRound() throws Exception {
        roundTrack.addDicesToRound(dices, 1);
        for (Dice dice : roundTrack.getDices(1)) {
            assertTrue(dices.contains(dice));
        }
    }

    @Test
    public void addDiceToRound() throws Exception {
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
    public void removeDiceFromRoundTrack() throws Exception {
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
    public void testEmptinessWithAddDice() throws Exception{
        roundTrack.addDiceToRound(dice1, 1);
        assertEquals(false, roundTrack.isEmpty());

        roundTrack.removeDiceFromRoundTrack(1, dice1);
        assertEquals(true, roundTrack.isEmpty());
    }

    @Test
    public void testEmptinessWithAddDices() throws Exception{
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
    public void testNewInstance() throws Exception {
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

    @Test(expected = NullPointerException.class)
    public void testAddNull() throws Exception{
        roundTrack.addDiceToRound(null, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testAddsNull() throws Exception {
        roundTrack.addDicesToRound(null, 0);
    }
}
