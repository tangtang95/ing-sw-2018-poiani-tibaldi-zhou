package org.poianitibaldizhou.sagrada.game.model;

import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.DrawableCollectionFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IDrawableCollectionFakeObserver;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class DrawableCollectionTest {

    public static class NonParameterizedPart {

        private static DrawableCollection<Dice> drawableCollection;

        private static List<Dice> diceList;

        @Mock
        private IDrawableCollectionFakeObserver observer1, observer2, observer3;

        @BeforeClass
        public static void setUpClass() {
            diceList = new ArrayList<>();
        }

        @AfterClass
        public static void tearDownClass() {
            diceList = null;
            drawableCollection = null;
        }

        @Before
        public void setUp() throws Exception{
            MockitoAnnotations.initMocks(this);
            for (int i = 0; i < 5; i++) {
                diceList.add(new Dice(i+1, Color.GREEN));
            }
            drawableCollection = new DrawableCollection<>();
            drawableCollection.addElements(diceList);
        }

        @Test
        public void testConstructorWithParam() {
            DrawableCollection<Dice> testDrawable = new DrawableCollection<>(diceList);
            assertEquals(drawableCollection, testDrawable);
        }

        @Test
        public void testDetachObserver() {
            drawableCollection.attachObserver("obs1", observer1);
            drawableCollection.detachObserver("obs1");
            assertTrue(drawableCollection.getObserverMap().isEmpty());
        }

        @Test
        public void testAttachObserver() {
            Map<String, IDrawableCollectionFakeObserver> expectedMap = new HashMap<>();
            expectedMap.putIfAbsent("obs1", observer1);

            drawableCollection.attachObserver("obs1", observer1);

            assertEquals(expectedMap, drawableCollection.getObserverMap());
        }

        @After
        public void tearDown() {
            diceList.clear();
            drawableCollection = null;
        }

        @Test
        public void testAddElementAndDraw() throws Exception{
            drawableCollection.addElement(new Dice(2, Color.GREEN));
            diceList.add(new Dice(2, Color.GREEN));
            assertArrayEquals("Error adding a single element", diceList.toArray(), drawableCollection.toArray());

            int initial_size = drawableCollection.size();
            try {
                for (int i = 0; i < initial_size - 1; i++) {
                    Dice temp = drawableCollection.draw();
                    diceList.remove(temp);
                    assertArrayEquals("Error drawing an element", diceList.toArray(), drawableCollection.toArray());
                    assertEquals("Error size", diceList.size(), drawableCollection.size());
                }
            } catch (EmptyCollectionException e) {
                fail("No exception expected");
            }
        }

        @Test
        public void testDrawException() throws Exception{
            int size = drawableCollection.size();
            try {
                for (int i = 0; i < size + 1; i++) {
                    drawableCollection.draw();
                }

                fail("No Exception launched");
            } catch (EmptyCollectionException e) {
                assertEquals("Random collection is not empty", 0, drawableCollection.size());
            }
        }

        @Test
        public void testAddElementsMethod() throws Exception{
            List<Dice> diceArrayList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                diceArrayList.add(new Dice(1+i, Color.PURPLE));
            }

            drawableCollection.addElements(diceArrayList);

            diceList.addAll(diceArrayList);

            assertArrayEquals("Add elements not working properly", diceList.toArray(), drawableCollection.toArray());
        }
    }

    @RunWith(value = Parameterized.class)
    public static class ParameterizedPart {

        private DrawableCollection<Dice> collection1;
        private DrawableCollection<Dice> collection2;
        private boolean expected;

        public ParameterizedPart(boolean expected, Object[] list1, Object[] list2) throws Exception{
            this.expected = expected;
            collection2 = new DrawableCollection<>();
            if(list1 != null) {
                collection1 = new DrawableCollection<>();
                for (Object o : list1)
                    collection1.addElement((Dice) o);
            } else {
                collection1 = null;
            }
            if(list2 != null) {
                collection2 = new DrawableCollection<>();
                for (Object o : list2)
                    collection2.addElement((Dice) o);
            } else {
                collection2 = null;
            }
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {true, new Object[] {new Dice(1, Color.BLUE), new Dice(2, Color.BLUE), new Dice(1, Color.BLUE)}, new Object[]{new Dice(1, Color.BLUE), new Dice(2, Color.BLUE), new Dice(1, Color.BLUE)}},
                    {false, new Object[] {new Dice(1, Color.BLUE), new Dice(1, Color.BLUE)}, new Object[]{new Dice(1, Color.BLUE)}},
                    {true, new Object[] {new Dice(1, Color.BLUE), new Dice(2, Color.BLUE), new Dice(3, Color.BLUE)}, new Object[] {new Dice(1, Color.BLUE), new Dice(2, Color.BLUE), new Dice(3, Color.BLUE)}},
                    {true, new Object[] {new Dice(1, Color.BLUE), new Dice(2, Color.BLUE), new Dice(3, Color.BLUE)}, new Object[] {new Dice(3, Color.BLUE), new Dice(2, Color.BLUE), new Dice(1, Color.BLUE)}},
                    {true, new Object[] {new Dice(1, Color.BLUE), new Dice(2, Color.BLUE), new Dice(2, Color.BLUE)}, new Object[] {new Dice(2, Color.BLUE), new Dice(1, Color.BLUE), new Dice(2, Color.BLUE)}},
                    {false, new Object[] {new Dice(2, Color.BLUE), new Dice(2, Color.BLUE), new Dice(1, Color.BLUE)}, new Object[] {new Dice(2, Color.BLUE), new Dice(2, Color.BLUE), new Dice(3, Color.BLUE)}},
                    {false, new Object[] {new Dice(1, Color.BLUE), new Dice(2, Color.BLUE), new Dice(4, Color.BLUE)}, new Object[] {new Dice(4, Color.BLUE), new Dice(4, Color.BLUE), new Dice(4, Color.BLUE)}},
                    {false, new Object[] {new Dice(1, Color.BLUE)}, null},
            });
        }

        @Test
        public void testEquals() {
            assertEquals(expected, collection1.equals(collection2));
        }

        @Test
        public void testHashCode() {
            if(collection2 != null)
                assertEquals(expected, collection1.hashCode() == collection2.hashCode());
        }
    }

}
