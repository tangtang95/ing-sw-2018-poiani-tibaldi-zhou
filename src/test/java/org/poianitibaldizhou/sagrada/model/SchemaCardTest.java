package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.DiceInvalidNumberException;
import org.poianitibaldizhou.sagrada.exception.SchemaCardPointOutOfBoundsException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCardPoint;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;

import static org.junit.Assert.*;

public class SchemaCardTest {
    @DataPoint
    public static SchemaCard emptySchemaCard;

    @DataPoint
    public static SchemaCard schemaCard;


    @BeforeClass
    public static void setUpClass() {

    }

    @Before
    public void setUp()  {
        Dice d1 = null;
        try {
            d1 = new Dice(5, Color.YELLOW);
        } catch (DiceInvalidNumberException e) {
            fail("No exception expected");
        }
        IConstraint[][] constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new NoConstraint();
            }
        }
        emptySchemaCard = new SchemaCard("test1", 1, constraints);
        schemaCard = new SchemaCard("test2", 1, constraints);
        try {
            schemaCard.setDice(d1, new SchemaCardPoint(0,3));
        } catch (Exception e) {
            fail("No exception expected");
        }
    }

    @After
    public void tearDown() {
        emptySchemaCard = null;
        schemaCard = null;
    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Test
    public void testConstructorDeepCopy() {
        boolean isDifferent = false;
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice d1 = null, d2 = null;
                try {
                    d1 = schemaCard.getDice(new SchemaCardPoint(i,j));
                    d2 = emptySchemaCard.getDice(new SchemaCardPoint(i,j));
                } catch (SchemaCardPointOutOfBoundsException e) {
                    fail("no exception expected");
                }
                if(d1 != null && d2 == null)
                    isDifferent = true;
                else if(d1 == null && d2 != null)
                    isDifferent = true;
                else if(d1 != null && d2 != null && !d1.equals(d2))
                    isDifferent = true;
            }
        }
        assertTrue(isDifferent);
    }

    @Test
    public void testIsEmpty(){
        assertFalse(schemaCard.isEmpty());
        assertTrue(emptySchemaCard.isEmpty());
    }

    @Test
    public void testGetTile(){

    }

    @Test
    public void testIsDicePositionable() {
        Dice d1, d2, d3;
        try {
            d1 = new Dice(4, Color.YELLOW);
            d2 = new Dice(3, Color.RED);
            d3 = new Dice(2, Color.BLUE);
            emptySchemaCard.setDice(d1, new SchemaCardPoint(0, 2));
            assertTrue(emptySchemaCard.isDicePositionable(d2, new SchemaCardPoint(0,3)));
        } catch (Exception e) {
            fail("no exception expected");
        }
    }

}
