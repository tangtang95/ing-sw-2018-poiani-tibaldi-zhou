package org.poianitibaldizhou.sagrada.model;

import com.sun.jdi.InvalidLineNumberException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.DiceInvalidNumberException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.NumberConstraint;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiceUnitTest {

    @Mock
    private ColorConstraint colorConstraint;

    @Mock
    private NumberConstraint numberConstraint;

    private Dice dice;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        when(colorConstraint.getColor()).thenReturn(Color.BLUE);
        when(numberConstraint.getNumber()).thenReturn(5);
        try {
            dice = new Dice(numberConstraint, colorConstraint);
        } catch (DiceInvalidNumberException e) {
            fail("no exception expected");
        }
    }

    @Test
    public void testDiceConstructor() {
        when(colorConstraint.getColor()).thenReturn(Color.BLUE);
        when(numberConstraint.getNumber()).thenReturn(5);

        assertEquals("Error dice number",5, dice.getNumber());
        assertEquals("Error dice number", dice.getNumber(), dice.getNumberConstraint().getNumber());
        assertEquals("Error dice color", Color.BLUE, dice.getColor());
        assertEquals("Error dice color", dice.getColor(), dice.getColorConstraint().getColor());
        try {
            dice = null;
            when(colorConstraint.getColor()).thenReturn(Color.GREEN);
            when(numberConstraint.getNumber()).thenReturn(7);

            dice = new Dice(numberConstraint, colorConstraint);

            fail("no exception launched");
        } catch (DiceInvalidNumberException e) {
            assertEquals("Excepted dice is null", null, dice);
        }
    }

    @Test
    public void testDiceEquals(){
        try {
            ColorConstraint cc2 = mock(ColorConstraint.class);
            NumberConstraint nc2 = mock(NumberConstraint.class);
            when(cc2.getColor()).thenReturn(Color.BLUE);
            when(nc2.getNumber()).thenReturn(4);

            ColorConstraint cc3 = mock(ColorConstraint.class);
            NumberConstraint nc3 = mock(NumberConstraint.class);
            when(cc3.getColor()).thenReturn(Color.YELLOW);
            when(nc3.getNumber()).thenReturn(5);

            ColorConstraint cc4 = mock(ColorConstraint.class);
            NumberConstraint nc4 = mock(NumberConstraint.class);
            when(cc4.getColor()).thenReturn(Color.BLUE);
            when(nc4.getNumber()).thenReturn(5);

            Dice d2 = new Dice(nc2, cc2);
            Dice d3 = new Dice(nc3, cc3);
            Dice d4 = new Dice(nc4, cc4);
            assertFalse(dice.equals(d3));
            assertFalse(dice.equals(d2));
            assertTrue(dice.equals(dice));
            assertTrue(dice.equals(d4));
        } catch(Exception e) {
            fail("No exception expected");
        }
    }

}
