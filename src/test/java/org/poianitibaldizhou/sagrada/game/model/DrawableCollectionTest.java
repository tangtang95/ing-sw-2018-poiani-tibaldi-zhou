package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoint;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class DrawableCollectionTest {

    public static class NonParameterizedPart {

        private static DrawableCollection<String> drawableCollection;

        private static List<String> stringList;

        @BeforeClass
        public static void setUpClass() {
            stringList = new ArrayList();
        }

        @AfterClass
        public static void tearDownClass() {
            stringList = null;
            drawableCollection = null;
        }

        @Before
        public void setUp() {
            for (int i = 0; i < 5; i++) {
                stringList.add("string" + i);
            }
            drawableCollection = new DrawableCollection<String>(stringList);
        }

        @After
        public void tearDown() {
            stringList.removeAll(stringList);
            drawableCollection = null;
        }

        @Test
        public void testAddElementAndDraw() {
            drawableCollection.addElement(new String("stringX"));
            stringList.add(new String("stringX"));
            assertArrayEquals("Error adding a single element", stringList.toArray(), drawableCollection.toArray());

            int initial_size = drawableCollection.size();
            try {
                for (int i = 0; i < initial_size - 1; i++) {
                    String temp = drawableCollection.draw();
                    stringList.remove(temp);
                    assertArrayEquals("Error drawing an element", stringList.toArray(), drawableCollection.toArray());
                    assertEquals("Error size", stringList.size(), drawableCollection.size());
                }
            } catch (EmptyCollectionException e) {
                fail("No exception expected");
            }
        }

        @Test
        public void testDrawException() {
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
        public void testAddElementsMethod() {
            List<String> newStrings = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                newStrings.add("newString" + i);
            }

            drawableCollection.addElements(newStrings);

            stringList.addAll(newStrings);

            assertArrayEquals("Add elements not working properly", stringList.toArray(), drawableCollection.toArray());
        }
    }

    @RunWith(value = Parameterized.class)
    public static class ParameterizedPart {

        private DrawableCollection<String> collection1;
        private DrawableCollection<String> collection2;
        private boolean expected;

        public ParameterizedPart(boolean expected, Object[] list1, Object[] list2) {
            this.expected = expected;
            collection2 = new DrawableCollection<>();
            if(list1 != null) {
                collection1 = new DrawableCollection<>();
                for (Object o : list1)
                    collection1.addElement((String) o);
            } else {
                collection1 = null;
            }
            if(list2 != null) {
                collection2 = new DrawableCollection<>();
                for (Object o : list2)
                    collection2.addElement((String) o);
            } else {
                collection2 = null;
            }
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {true, new Object[] {"1", "2", "1"}, new Object[]{"1", "2", "1"}},
                    {false, new Object[] {"1", "1"}, new Object[]{"1"}},
                    {true, new Object[] {"1", "2", "3"}, new Object[] {"1", "2", "3"}},
                    {true, new Object[] {"1", "2", "3"}, new Object[] {"3", "2", "1"}},
                    {true, new Object[] {"1", "2", "2"}, new Object[] {"2", "1", "2"}},
                    {false, new Object[] {"2", "2", "1"}, new Object[] {"2", "2", "3"}},
                    {false, new Object[] {"1", "2", "4"}, new Object[] {"4", "4", "4"}},
                    {true, new Object[] {"", "", ""}, new Object[] {"", "", ""}},
                    {false, new Object[] {""}, null},
                    {true, new Object[] {"1", "1", "1", "2", "2"}, new Object[] {"2", "1", "2", "1", "1"}},
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
