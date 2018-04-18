package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DrawableCollectionTest {
    @DataPoint
    public static DrawableCollection<String> drawableCollection;

    @DataPoint
    public static List<String> stringList;

    @BeforeClass
    public static void setUpClass() {
        stringList = new ArrayList<String>();
    }

    @AfterClass
    public static void tearDownClass() {
        stringList = null;
        drawableCollection = null;
    }

    @Before
    public void setUp(){
        for (int i = 0; i < 5; i++) {
            stringList.add("string"+i);
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
        assertArrayEquals( "Error adding a single element", stringList.toArray(), drawableCollection.toArray());

        int initial_size = drawableCollection.size();
        try {
            for (int i = 0; i < initial_size - 1; i++) {
                String temp = drawableCollection.draw();
                stringList.remove(temp);
                assertArrayEquals("Error drawing an element",stringList.toArray(), drawableCollection.toArray());
                assertEquals("Error size",stringList.size(), drawableCollection.size());
            }
        } catch (EmptyCollectionException e) {
            fail("No exception expected");
        }
    }

    @Test
    public void testDrawException() {
        int size = drawableCollection.size();
        try {
            for (int i = 0; i < size+1; i++) {
                drawableCollection.draw();
            }

            fail("No Exception launched");
        } catch (EmptyCollectionException e) {
            assertEquals("Random collection is not empty", 0, drawableCollection.size());
        }
    }

    @Test
    public void testAddElementsMethod() {
        List<String>  newStrings = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            newStrings.add("newString"+i);
        }

        drawableCollection.addElements(newStrings);

        stringList.addAll(newStrings);

        assertArrayEquals("Add elements not working properly", stringList.toArray(), drawableCollection.toArray());
    }
}
