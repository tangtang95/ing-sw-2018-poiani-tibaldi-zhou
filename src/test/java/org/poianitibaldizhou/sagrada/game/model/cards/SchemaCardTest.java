package org.poianitibaldizhou.sagrada.game.model.cards;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.*;
import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import static org.junit.Assert.*;

public class SchemaCardTest {

    private SchemaCard schemaCard ,fullSchemaCard, emptySchemaCard;

    @Before
    public void setUp() throws Exception {
        Dice d1 = new Dice(4, Color.YELLOW);

        IConstraint[][] constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new NoConstraint();
            }
        }
        emptySchemaCard = new SchemaCard("test1", 1, constraints);
        schemaCard = new SchemaCard("test2", 1, constraints);
        schemaCard.setDice(d1,0, 2);

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
                d1 = schemaCard.getDice(new Position(i, j));
                d2 = emptySchemaCard.getDice(new Position(i, j));

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
    public void testHasDiceOfColor() throws Exception{
        for(Color c : Color.values())
            assertFalse(emptySchemaCard.hasDiceOfColor(c));

        emptySchemaCard.setDice(new Dice(5, Color.BLUE), 1,0);
        emptySchemaCard.setDice(new Dice(3, Color.YELLOW), 1, 1);
        assertTrue(emptySchemaCard.hasDiceOfColor(Color.BLUE));
        assertTrue(emptySchemaCard.hasDiceOfColor(Color.YELLOW));
        assertFalse(emptySchemaCard.hasDiceOfColor(Color.RED));
        assertFalse(emptySchemaCard.hasDiceOfColor(Color.PURPLE));
        assertFalse(emptySchemaCard.hasDiceOfColor(Color.GREEN));
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
            assertEquals(d1, emptySchemaCard.getDice(new Position(0,1)));
            emptySchemaCard.removeDice(new Position(0,1));
            assertEquals(null, emptySchemaCard.getDice(new Position(0,1)));
        } catch (Exception e){
            fail("no exception expected");
        }

    }

    @Test
    public void testSetDiceRuleViolationException() throws Exception{
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
        schemaCard.removeDice(new Position(0,3));

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
        fullSchemaCard.removeDice(new Position(0,2));

        try {
            fullSchemaCard.setDice(d4,2, 4);
            fail("no exception launched");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.TILE_UNMATCHED, e.getViolationType());
        } catch (Exception e) {
            fail("no exception expected");
        }

        try{
            fullSchemaCard.setDice(d4,0,2, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.ISOLATED);
            fullSchemaCard.removeDice(new Position(0,2));
            fullSchemaCard.setDice(d4,2,4, PlacementRestrictionType.NUMBER, DiceRestrictionType.NORMAL);
            fullSchemaCard.removeDice(new Position(2,4));
            fullSchemaCard.setDice(d3,0,1, PlacementRestrictionType.COLOR, DiceRestrictionType.NORMAL);
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

        assertTrue(fullSchemaCard.isDicePositionable(d4,2,4, PlacementRestrictionType.NUMBER, DiceRestrictionType.NORMAL));
        assertTrue(fullSchemaCard.isDicePositionable(d4,2, 4, PlacementRestrictionType.NONE, DiceRestrictionType.NORMAL));
        assertTrue(fullSchemaCard.isDicePositionable(d3,0,1, PlacementRestrictionType.COLOR, DiceRestrictionType.NORMAL));
    }

    @Test
    public void toStringTest(){
        String empty = "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n";
        String schema = "  -----   -----   -----   -----   -----  \n" +
                "|       |       |  4/Y  |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n";

        String full = "  -----   -----   -----   -----   -----  \n" +
                "|       |   2   |   Y   |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |   4   |       |\n" +
                "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |       |   R   |\n" +
                "  -----   -----   -----   -----   -----  \n" +
                "|       |       |       |       |       |\n" +
                "  -----   -----   -----   -----   -----  \n";

        assertEquals(empty,emptySchemaCard.toString());
        assertEquals(schema,schemaCard.toString());
        assertEquals(full,fullSchemaCard.toString());
    }

    @Test
    public void testIsolatedSetDice() throws Exception {
        Dice d1 = new Dice(5, Color.PURPLE);
        try {
            schemaCard.isDicePositionable(d1, 0, 1, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.ISOLATED);
            schemaCard.setDice(d1,0, 1, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.ISOLATED);
            fail("exception expected");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.HAS_DICE_NEAR, e.getViolationType());
        }
        schemaCard.isDicePositionable(d1, 0, 0, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.ISOLATED);
        schemaCard.setDice(d1, 0, 0, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.ISOLATED);
        assertEquals(d1, schemaCard.getDice(new Position(0,0)));
    }

    @Test
    public void getNumberOfEmptySpaces() throws Exception{
        assertEquals(20, emptySchemaCard.getNumberOfEmptySpaces());
        assertEquals(19, schemaCard.getNumberOfEmptySpaces());
        schemaCard.removeDice(new Position(0, 2));

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

    @Test
    public void testToJSON(){
        String message = "{\"type\":\"schemaCard\",\"body\":{\"difficulty\":1,\"name\":\"test1\",\"matrix\":[" +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}";
        assertTrue(message.equals(emptySchemaCard.toJSON().toJSONString()));
        message = "{\"type\":\"schemaCard\",\"body\":{\"difficulty\":2,\"name\":\"test3\",\"matrix\":[" +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":2}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":\"YELLOW\"}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":4}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":\"RED\"}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}";
        assertTrue(message.equals(fullSchemaCard.toJSON().toJSONString()));
    }

    @Test
    public void testToObject(){
        String message = "{\"difficulty\":1,\"name\":\"test1\",\"matrix\":[" +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}";
        String message1 = "{\"difficulty\":2,\"name\":\"test3\",\"matrix\":[" +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":2}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":\"YELLOW\"}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":4}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":\"RED\"}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}";
        org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        try {
            assertTrue((emptySchemaCard.toObject((JSONObject) jsonParser.parse(message))).equals(emptySchemaCard));
            assertTrue((fullSchemaCard.toObject((JSONObject) jsonParser.parse(message1))).equals(fullSchemaCard));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
