package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.DiceInvalidNumberException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

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
    public void setUp() throws DiceInvalidNumberException {
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
        int size = dp.getDices().size();
        try {
            for (int i = 0; i < size; i++) {
                dp.useDice(dices.get(i));
                dices.remove(i);
                assertArrayEquals("Use dice not working", dices.toArray(), dp.getDices().toArray());
            }
        } catch (DiceNotFoundException dnfe){
            fail("No exception expected");
        } catch (EmptyCollectionException ece) {
            fail("No exception expected");
        }
    }
}