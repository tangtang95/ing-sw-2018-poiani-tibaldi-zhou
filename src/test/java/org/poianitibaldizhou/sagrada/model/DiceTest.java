package org.poianitibaldizhou.sagrada.model;

import org.junit.Test;
import org.poianitibaldizhou.sagrada.exception.DiceInvalidNumberException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;

import static org.junit.Assert.*;

public class DiceTest {

    @Test
    public void testDiceConstructor() {
        try {
            Dice d1 = new Dice(5, Color.BLUE);
            assertEquals("Error dice number",5, d1.getNumber());
            assertEquals("Error dice number", d1.getNumber(), d1.getNumberConstraint().getNumber());
            assertEquals("Error dice color", Color.BLUE, d1.getColor());
            assertEquals("Error dice color", d1.getColor(), d1.getColorConstraint().getColor());
        } catch (DiceInvalidNumberException e) {
            fail("no exception excepted");
        }
        Dice d2 = null;
        try {
            d2 = new Dice(7, Color.GREEN);
        } catch (DiceInvalidNumberException e) {
            assertEquals("Excepted dice is null", null, d2);
        }
    }

    @Test
    public void testDiceEquals(){
        try {
            Dice d1 = new Dice(5, Color.BLUE);
            Dice d2 = new Dice(4, Color.BLUE);
            Dice d3 = new Dice(5, Color.YELLOW);
            Dice d4 = new Dice(5, Color.BLUE);
            assertFalse(d1.equals(d3));
            assertFalse(d1.equals(d2));
            assertTrue(d1.equals(d1));
            assertTrue(d1.equals(d4));
        } catch(Exception e) {
            fail("No exception expected");
        }
    }

    @Test
    public void testDicePourOver() {
        Dice d1, pourOver;
        try {
            for (int i = 1; i <= 6; i++) {
                d1 = new Dice(i, Color.BLUE);
                pourOver = d1.pourOverDice();
                assertEquals(d1.getColor(), pourOver.getColor());
                assertEquals(7-i, pourOver.getNumber());
            }
        } catch(Exception e) {
            fail("No exception expected");
        }
    }
}
