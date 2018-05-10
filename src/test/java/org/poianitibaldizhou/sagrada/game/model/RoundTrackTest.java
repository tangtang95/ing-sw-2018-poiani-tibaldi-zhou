package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import java.util.ArrayList;
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
    public void setUp(){
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
    }

    @Test
    public void newInstance() throws Exception {
    }

    @Test
    public void addDicesToRound() throws Exception {
        roundTrack.addDicesToRound(dices, 1);
        for (Dice dice: roundTrack.getDices(1)) {
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
        for (Dice dice: roundTrack.getDices(2)) {
            assertTrue(dices.contains(dice));
        }
        roundTrack.removeDiceFromRoundTrack(2, dice1);
        assertFalse(roundTrack.getDices(2).contains(dice1));

        //Test exception
        try {
            roundTrack.removeDiceFromRoundTrack(1, dice1);
            fail("exception expected");
        }catch (IllegalArgumentException e){
            assertNotEquals(null, e);
        }
    }

}
