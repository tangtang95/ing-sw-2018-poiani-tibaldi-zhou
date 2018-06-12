package org.poianitibaldizhou.sagrada.game.model.board;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.AddDiceToDiceBagTest;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.DraftPoolFakeObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DraftPoolTest {

    private static DraftPool dp;
    private static List<Dice> dices;
    private Map<String, DraftPoolFakeObserver> observerList;

    @Mock
    private DraftPoolFakeObserver observer1, observer2, observer3;


    @BeforeClass
    public static void setUpClass() {
        dices = new ArrayList<>();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dices.add(new Dice(5, Color.BLUE));
        dices.add(new Dice(2, Color.BLUE));
        dices.add(new Dice(1, Color.PURPLE));
        dices.add(new Dice(6, Color.RED));
        dices.add(new Dice(3, Color.GREEN));
        dices.add(new Dice(6, Color.RED));

        dp = new DraftPool();
        dp.addDices(dices);

        observerList = new HashMap<>();
        observerList.put("obs1", observer1);
        observerList.put("obs2", observer2);
        observerList.put("obs3", observer3);
        observerList.forEach((key, value) -> dp.attachObserver(key, value));
    }

    @Test
    public void testToJSON() {
        String message = "{\"type\":\"draftPool\",\"body\":{\"diceList\":{\"type\":\"collection\",\"body\":" +
                "[{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":5}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":1}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":6}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"GREEN\",\"value\":3}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":6}}]}}}";

        assertTrue(message.equals(dp.toJSON().toJSONString()));
    }

    @Test
    public void testToObject() {
        String message = "{\"diceList\":{\"type\":\"collection\",\"body\":" +
                "[{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":5}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":1}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":6}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"GREEN\",\"value\":3}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":6}}]}}";
       org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        try {
            assertTrue(dp.toObject((JSONObject) jsonParser.parse(message)) == null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testAddDices() {
        List<Dice> diceList = new ArrayList<>();
        diceList.add(new Dice(6, Color.GREEN));
        diceList.add(new Dice(4, Color.RED));
        dp.addDices(diceList);
        dp.getObserverMap().forEach((key, value) -> verify(value, times(1)).onDicesAdd(diceList));
    }

    @After
    public void tearDown() {
        dices.removeAll(dices);
        dp = null;
        observerList = null;
    }

    @AfterClass
    public static void tearDownClass() {
        dp = null;
        dices = null;
    }

    @Test
    public void testGetObserver() {
        assertEquals(observerList, dp.getObserverMap());
    }

    @Test
    public void testAddDice() {
        Dice dice = new Dice(5, Color.PURPLE);
        dp.addDice(dice);
        dices.add(dice);
        assertEquals(dices, dp.getDices());
        dp.getObserverMap().forEach((key, value) -> verify(value, times(1)).onDiceAdd(dice));
    }

    @Test
    public void testRerollDices() {
        List<Dice> prevList = dp.getDices();
        dp.reRollDices();
        List<Dice> newList = dp.getDices();
        assertEquals(prevList.size(), newList.size());
        int[] prevcolors = new int[Color.values().length];
        int[] newcolors = new int[Color.values().length];
        for (Dice d : prevList) {
            prevcolors[d.getColor().ordinal()] += 1;
        }
        for (Dice d : newList) {
            newcolors[d.getColor().ordinal()] += 1;
        }

        for (int i = 0; i < prevcolors.length; i++) {
            System.out.println(prevcolors[i] + "  " + newcolors[i]);
        }
        assertArrayEquals(prevcolors, newcolors);
        dp.getObserverMap().forEach((key, value) -> verify(value, times(1)).onDraftPoolReroll(dp.getDices()));
    }

    @Test
    public void testEquals() throws Exception {
        DraftPool draftPool = new DraftPool();
        draftPool.addDice(new Dice(1, Color.PURPLE));
        assertNotEquals(dp, draftPool);
        assertNotEquals(dp, new AddDiceToDiceBagTest());

        draftPool = new DraftPool();
        draftPool.addDice(new Dice(5, Color.BLUE));
        draftPool.addDice(new Dice(2, Color.BLUE));
        draftPool.addDice(new Dice(1, Color.PURPLE));
        draftPool.addDice(new Dice(6, Color.RED));
        draftPool.addDice(new Dice(6, Color.RED));
        draftPool.addDice(new Dice(3, Color.GREEN));

        assertEquals(dp, draftPool);
    }

    @Test
    public void testClear() {
        dp.clearPool();
        assertEquals(0, dp.size());
        dp.getObserverMap().forEach((key, value) -> verify(value, times(1)).onDraftPoolClear());
    }

    @Test
    public void testNewInstance() {
        DraftPool draftPool = DraftPool.newInstance(dp);
        assertEquals(dp, draftPool);
        assertEquals(dp.getObserverMap(), draftPool.getObserverMap());

        assertNull(DraftPool.newInstance(null));
    }

    @Test
    public void testToString() {
        List<Dice> diceList = new ArrayList<>();
        diceList.add(new Dice(4, Color.BLUE));
        diceList.add(new Dice(4, Color.RED));

        DraftPool toStringDraftPool = new DraftPool();
        toStringDraftPool.addDices(diceList);

        assertEquals(diceList.toString(), toStringDraftPool.toString());
    }

    @Test
    public void testHashCode()throws Exception {
        DraftPool draftPool = DraftPool.newInstance(dp);
        assertEquals(draftPool.hashCode(), dp.hashCode());

        draftPool.useDice(new Dice(5, Color.BLUE));
        assertNotEquals(draftPool.hashCode(), dp.hashCode());
    }

    @Test
    public void testUseDice() throws Exception {
        int order[] = new int[]{5, 2, 1, 2, 0, 0};
        int size = dp.getDices().size();

        for (int i = 0; i < size; i++) {
            Dice removed = dices.get(order[i]);
            dp.useDice(removed);
            dices.remove(removed);

            assertTrue(dp.getDices().containsAll(dices) && dices.containsAll(dp.getDices()));
            dp.getObserverMap().forEach((key, value) -> {
                verify(value, times(1)).onDiceRemove(removed);
                reset(value);
            });
        }

    }

    @Test
    public void testEmptyException() {
        dp = new DraftPool();
        try {
            dp.useDice(new Dice(4, Color.BLUE));
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