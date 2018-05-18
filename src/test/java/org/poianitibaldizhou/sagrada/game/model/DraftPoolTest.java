package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.AddDiceToDiceBagTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

public class DraftPoolTest {

    @DataPoint
    public static DraftPool dp;

    @DataPoint
    public static List<Dice> dices;

    @BeforeClass
    public static void setUpClass() {
        dices = new ArrayList<>();
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
    public void testAddDice() {
        dp.addDice(new Dice(5, Color.PURPLE));
        dices.add(new Dice(5, Color.PURPLE));
        assertEquals(dices, dp.getDices());
    }

    @Test
    public void testRerollDices() {
        List<Dice> prevList = dp.getDices();
        dp.reRollDices();
        List<Dice> newList = dp.getDices();
        assertEquals(prevList.size(), newList.size());
        int[] prevcolors = new int[Color.values().length];
        int[] newcolors = new int[Color.values().length];
        for(Dice d: prevList) {
            prevcolors[d.getColor().ordinal()] += 1;
        }
        for(Dice d : newList) {
            newcolors[d.getColor().ordinal()] += 1;
        }

        for (int i = 0; i < prevcolors.length; i++) {
            System.out.println(prevcolors[i] + "  " + newcolors[i]);
        }
        assertArrayEquals(prevcolors, newcolors);
    }

    @Test
    public void testEquals() {
        DraftPool draftPool = new DraftPool();
        draftPool.addDice(new Dice(1, Color.PURPLE));
        assertNotEquals(dp, draftPool);

        assertNotEquals(dp, new AddDiceToDiceBagTest());
    }

    @Test
    public void testClear() {
        dp.clearPool();
        assertEquals(0, dp.size());
    }

    @Test
    public void testNewInstance() {
        DraftPool draftPool = DraftPool.newInstance(dp);
        assertEquals(dp, draftPool);
    }

    @Test
    public void testUseDice() {
        int order[] = new int[]{5, 2, 1, 2, 0, 0};
        int size = dp.getDices().size();

        try {
            for (int i = 0; i < size; i++) {
                dp.useDice(dices.get(order[i]));
                dices.remove(order[i]);

                assertTrue(dp.getDices().containsAll(dices) && dices.containsAll(dp.getDices()));
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