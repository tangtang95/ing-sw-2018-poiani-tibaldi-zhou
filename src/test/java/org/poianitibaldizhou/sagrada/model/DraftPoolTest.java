package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DraftPoolTest {

    @DataPoint
    public static DraftPool dp;

    @DataPoint
    public static List<Dice> dices;

    @BeforeClass
    public static void setUpClass() {
        dices = new ArrayList<Dice>();
    }

    @Before
    public void setUp()  {
        dices.add(new Dice(5, Color.BLUE));
        dices.add(new Dice(2, Color.BLUE));
        dices.add(new Dice(1, Color.PURPLE));
        dices.add(new Dice(6, Color.RED));
        dices.add(new Dice(3, Color.GREEN));
        dices.add(new Dice(6, Color.RED));

        dp = new DraftPool();
        dp.addDices(dices);
    }

    @After
    public void tearDown() {
        dices.removeAll(dices);
        dp = null;
    }

    @AfterClass
    public static void tearDownClass() {
        dp = null;
        dices = null;
    }

    @Test
    public void testUseDice() {
        int order[] = new int[]{5, 2, 1, 2, 0, 0};
        int size = dp.getDices().size();

        try {
            for (int i = 0; i < size; i++) {
                dp.useDice(dices.get(order[i]));
                dices.remove(order[i]);

                assert(dp.getDices().containsAll(dices) && dices.containsAll(dp.getDices()));
            }
        } catch (DiceNotFoundException dnfe){
            fail("No exception expected");
        } catch (EmptyCollectionException ece) {
            fail("No exception expected");
        }
    }

    @Test
    public void testEmptyException() {
        dp = new DraftPool();
        try {
            dp.useDice(new Dice(4,Color.BLUE));
            fail("EmptyCollectionException expected");
        } catch (DiceNotFoundException e) {
            fail("No exception expected");
        } catch (EmptyCollectionException e) {
            assertEquals(0, dp.getDices().size());
        }
    }

    @Test
    public void testDiceNotFoundException() {
        try {
            dp.useDice(new Dice(1, Color.GREEN));
            fail("NotFoundException expected");
        } catch (DiceNotFoundException e) {
            assertTrue(!dp.getDices().contains(new Dice(1, Color.GREEN)));
        } catch (EmptyCollectionException e) {
            fail("Not expected exception");
        }
    }
}