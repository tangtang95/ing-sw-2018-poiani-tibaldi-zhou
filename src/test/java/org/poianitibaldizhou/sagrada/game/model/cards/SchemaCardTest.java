package org.poianitibaldizhou.sagrada.game.model.cards;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.*;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import static org.junit.Assert.*;

public class SchemaCardTest {

    private SchemaCard schemaCard ,fullSchemaCard, emptySchemaCard;

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
    public void testSetDiceRuleViolationException() {
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
            emptySchemaCard.setDice(d1,1, 1);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.NOT_BORDER_TILE, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try {
            schemaCard.setDice(d2,0, 3);
        } catch (Exception e) {
            fail("no exception expected");
        }
        schemaCard.removeDice(0,3);

        try {
            schemaCard.setDice(d3,0, 4);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.NO_DICE_NEAR, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try {
            schemaCard.setDice(d4,0, 3);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.SIMILAR_DICE_NEAR, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try{
            schemaCard.setDice(d5,0, 1);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.SIMILAR_DICE_NEAR, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try{
            schemaCard.setDice(d3,0,2);
            fail("no exception launched");
        } catch(RuleViolationException e){
            assertEquals(RuleViolationType.NO_DICE_NEAR, e.getViolationType());
        } catch(Exception e){
            fail("no exception expected");
        }

        try {
            fullSchemaCard.setDice(d4,0, 2);
        } catch (Exception e) {
            fail("no exception expected");
        }
        fullSchemaCard.removeDice(0,2);

        try {
            fullSchemaCard.setDice(d4,2, 4);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.TILE_UNMATCHED, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try{
            fullSchemaCard.setDice(d4,0,2, TileConstraintType.NUMBER_COLOR, DiceConstraintType.ISOLATED);
            fullSchemaCard.removeDice(0,2);
            fullSchemaCard.setDice(d4,2,4, TileConstraintType.NUMBER, DiceConstraintType.NORMAL);
            fullSchemaCard.removeDice(2,4);
            fullSchemaCard.setDice(d3,0,1, TileConstraintType.COLOR, DiceConstraintType.NORMAL);
        } catch (Exception e) {
            fail("no exception expected");
        }


    }

    @Test
    public void testIsDicePositionable(){
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
        assertFalse(emptySchemaCard.isDicePositionable(d1,1, 1));
        assertFalse(schemaCard.isDicePositionable(d3,0, 4));
        assertFalse(schemaCard.isDicePositionable(d5,0, 1));
        assertFalse(schemaCard.isDicePositionable(d4,0, 3));
        assertFalse(schemaCard.isDicePositionable(d3,0,2));
        assertFalse(schemaCard.isDicePositionable(d5,0, 1));
        assertFalse(schemaCard.isDicePositionable(d3,0,2));
        assertFalse(fullSchemaCard.isDicePositionable(d4,2, 4));

        assertTrue(fullSchemaCard.isDicePositionable(d4,2,4, TileConstraintType.NUMBER, DiceConstraintType.NORMAL));
        assertTrue(fullSchemaCard.isDicePositionable(d4,2, 4, TileConstraintType.NONE, DiceConstraintType.NORMAL));
        assertTrue(fullSchemaCard.isDicePositionable(d3,0,1, TileConstraintType.COLOR, DiceConstraintType.NORMAL));
    }

    @Test
    public void toStringTest(){
        System.out.println(emptySchemaCard.toString() + "\n");
        System.out.println(schemaCard.toString() + "\n");
        System.out.println(fullSchemaCard.toString() + "\n");
    }

    @Test
    public void testIsolatedSetDice() throws RuleViolationException {
        Dice d1 = new Dice(5, Color.PURPLE);
        try {
            schemaCard.isDicePositionable(d1, 0, 1, TileConstraintType.NUMBER_COLOR, DiceConstraintType.ISOLATED);
            schemaCard.setDice(d1,0, 1, TileConstraintType.NUMBER_COLOR, DiceConstraintType.ISOLATED);
            fail("exception expected");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.HAS_DICE_NEAR, e.getViolationType());
        }
        schemaCard.isDicePositionable(d1, 0, 0, TileConstraintType.NUMBER_COLOR, DiceConstraintType.ISOLATED);
        schemaCard.setDice(d1, 0, 0, TileConstraintType.NUMBER_COLOR, DiceConstraintType.ISOLATED);
        assertEquals(d1, schemaCard.getDice(0,0));
    }

    @Test
    public void getNumberOfEmptySpaces() throws RuleViolationException{
        assertEquals(20, emptySchemaCard.getNumberOfEmptySpaces());
        assertEquals(19, schemaCard.getNumberOfEmptySpaces());
        schemaCard.removeDice(0, 2);

        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 1, 0);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 2, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 3, 0);

        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 1);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 1, 1);
        schemaCard.setDice(new Dice(2, Color.BLUE), 2, 1);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 3, 1);

        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 1, 2);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 2, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 3, 2);

        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 3);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 1, 3);
        schemaCard.setDice(new Dice(2, Color.BLUE), 2, 3);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 3, 3);

        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 4);
        schemaCard.setDice(new Dice(2, Color.BLUE), 1, 4);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 2, 4);
        schemaCard.setDice(new Dice(2, Color.BLUE), 3, 4);

        assertEquals(0, schemaCard.getNumberOfEmptySpaces());
    }

    @Test
    public void testEquals(){
        assertTrue(schemaCard.equals(schemaCard));
        assertFalse(schemaCard.equals(emptySchemaCard));
        assertFalse(schemaCard.equals(fullSchemaCard));
        assertFalse(emptySchemaCard.equals(fullSchemaCard));
        assertFalse(schemaCard.equals(Dice.class));
    }

    @Test
    public void testHashCode(){
        assertEquals(schemaCard.hashCode(), schemaCard.hashCode());
        assertNotEquals(schemaCard.hashCode(), emptySchemaCard.hashCode());
        assertNotEquals(schemaCard.hashCode(), fullSchemaCard.hashCode());
        assertNotEquals(emptySchemaCard.hashCode(), fullSchemaCard.hashCode());
        assertNotEquals(schemaCard.hashCode(), Dice.class.hashCode());
    }

}
