package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.*;

import static org.junit.Assert.*;

public class SchemaCardTest {
    @DataPoint
    public static SchemaCard emptySchemaCard;

    @DataPoint
    public static SchemaCard schemaCard;

    @DataPoint
    public static SchemaCard fullSchemaCard;


    @Before
    public void setUp() {
        Dice d1 = null;
        d1 = new Dice(4, Color.YELLOW);

        IConstraint[][] constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new NoConstraint();
            }
        }
        emptySchemaCard = new SchemaCard("test1", 1, constraints);
        schemaCard = new SchemaCard("test2", 1, constraints);
        try {
            schemaCard.setDice(d1,0, 2);
        } catch (Exception e) {
            fail("No exception expected");
        }
        constraints[0][2] = new ColorConstraint(Color.YELLOW);
        constraints[1][3] = new NumberConstraint(4);
        constraints[2][4] = new ColorConstraint(Color.RED);
        constraints[0][1] = new NumberConstraint(2);
        fullSchemaCard = new SchemaCard("test3", 2, constraints);
    }

    @After
    public void tearDown() {
        emptySchemaCard = null;
        schemaCard = null;
    }

    @Test
    public void testConstructorDeepCopy() {
        boolean isDifferent = false;
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice d1 = null, d2 = null;
                d1 = schemaCard.getDice(i, j);
                d2 = emptySchemaCard.getDice(i, j);

                if (d1 != null && d2 == null)
                    isDifferent = true;
                else if (d1 == null && d2 != null)
                    isDifferent = true;
                else if (d1 != null && d2 != null && !d1.equals(d2))
                    isDifferent = true;
            }
        }
        assertTrue(isDifferent);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(schemaCard.isEmpty());
        assertTrue(emptySchemaCard.isEmpty());
    }

    @Test
    public void testSetGetRemoveDice() {
        Dice d1 = null;
        try {
            d1 = new Dice(4, Color.YELLOW);
        } catch (Exception e) {
            fail("no exception expected");
        }

        try {
            emptySchemaCard.setDice(d1,0,1);
            assertEquals(d1, emptySchemaCard.getDice(0,1));
            emptySchemaCard.removeDice(0,1);
            assertEquals(null, emptySchemaCard.getDice(0,1));
        } catch (Exception e){
            fail("no exception expected");
        }

    }

    @Test
    public void testIsDicePositionable() {
        Dice d1 = null, d2 = null, d3 = null, d4 = null, d5 = null;
        try {
            d1 = new Dice(4, Color.YELLOW);
            d2 = new Dice(3, Color.RED);
            d3 = new Dice(2, Color.BLUE);
            d4 = new Dice(3, Color.YELLOW);
            d5 = new Dice(4, Color.PURPLE);
        } catch (Exception e) {
            fail("no exception expected");
        }

        try {
            emptySchemaCard.isDicePositionable(d1,1, 1);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.NOT_BORDER_TILE, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try {
            schemaCard.isDicePositionable(d2,0, 3);
        } catch (Exception e) {
            fail("no exception expected");
        }

        try {
            schemaCard.isDicePositionable(d3,0, 4);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.NO_DICE_NEAR, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try {
            schemaCard.isDicePositionable(d4,0, 3);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.SIMILAR_DICE_NEAR, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try{
            schemaCard.isDicePositionable(d5,0, 1);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.SIMILAR_DICE_NEAR, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try{
            schemaCard.isDicePositionable(d3,0,2);
            fail("no exception launched");
        } catch(RuleViolationException e){
            assertEquals(RuleViolationType.TILE_FILLED, e.getViolationType());
        } catch(Exception e){
            fail("no exception expected");
        }

        try {
            assertTrue(fullSchemaCard.isDicePositionable(d4,0, 2));
        } catch (Exception e) {
            fail("no exception expected");
        }

        try {
            fullSchemaCard.isDicePositionable(d4,2, 4);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.TILE_UNMATCHED, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try {
            assertTrue(fullSchemaCard.isDicePositionable(d4,2,4, TileConstraintType.NUMBER, DiceConstraintType.NORMAL));
            assertTrue(fullSchemaCard.isDicePositionable(d4,2, 4, TileConstraintType.NONE, DiceConstraintType.NORMAL));
            assertTrue(fullSchemaCard.isDicePositionable(d3,0,1, TileConstraintType.COLOR, DiceConstraintType.NORMAL));
        } catch (Exception e) {
            fail("no exception expected");
        }

        try{
            fullSchemaCard.isDicePositionable(d4,0,2, TileConstraintType.NUMBER_COLOR, DiceConstraintType.ISOLATED);
        } catch (Exception e) {
            fail("no exception expected");
        }


    }

}
