package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.*;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;

import static org.junit.Assert.assertEquals;

public class DiagonalPublicObjectiveCardTest {

    @DataPoint
    public static SchemaCard schemaCard;

    @DataPoint
    public static IConstraint[][] constraints;

    @BeforeClass
    public static void setUpClass() {
        constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
    }

    @AfterClass
    public static void tearDownClass(){
        constraints = null;
        schemaCard = null;
    }

    @Before
    public void setUp(){
        schemaCard = new SchemaCard("test", 1, constraints);
    }

    @After
    public void tearDown() {
        schemaCard  = null;
    }

    /**
     * Test score.
     * A single column with different ColorConstraint on each column.
     */
    @Test
    public void testScoreSingleColumnWithDifferentNumber() throws RuleViolationException {
        schemaCard.setDice(new Dice(6, Color.YELLOW), 0, 0);
        schemaCard.setDice(new Dice(1, Color.RED), 1, 0);
        schemaCard.setDice(new Dice(5, Color.PURPLE), 2, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 3, 0);

        PublicObjectiveCard poc = new DiagonalPublicObjectiveCard("test", "test",
                1, TileConstraintType.NUMBER);
        assertEquals(0 ,poc.getScore(schemaCard));

        poc = new DiagonalPublicObjectiveCard("test", "test",
                2, TileConstraintType.COLOR);
        assertEquals(0,poc.getScore(schemaCard));
    }

    /**
     * Test score.
     * A single row with different NumberConstraint on each row.
     */
    @Test
    public void testScoreSingleRowWithDifferentColor() throws RuleViolationException {
        schemaCard.setDice(new Dice(1, Color.YELLOW), 1, 0);
        schemaCard.setDice(new Dice(2, Color.RED), 1, 1);
        schemaCard.setDice(new Dice(3, Color.GREEN), 1, 2);
        schemaCard.setDice(new Dice(2, Color.PURPLE), 1, 3);
        schemaCard.setDice(new Dice(1, Color.BLUE), 1, 4);

        PublicObjectiveCard poc = new DiagonalPublicObjectiveCard("test", "test",
                1, TileConstraintType.NUMBER);
        assertEquals(0 ,poc.getScore(schemaCard));

        poc = new DiagonalPublicObjectiveCard("test", "test",
                2, TileConstraintType.COLOR);
        assertEquals(0,poc.getScore(schemaCard));
    }

    /**
     * Test score.
     * Every dice has at least one diagonal dice with same number
     */
    @Test
    public void testScoreEveryRowDifferentNumbers() throws RuleViolationException {
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 1);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 3);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 4);

        schemaCard.setDice(new Dice(2, Color.BLUE), 1, 0);
        schemaCard.setDice(new Dice(1, Color.RED), 1, 1);
        schemaCard.setDice(new Dice(2, Color.BLUE), 1, 2);
        schemaCard.setDice(new Dice(1, Color.RED), 1, 3);
        schemaCard.setDice(new Dice(2, Color.BLUE), 1, 4);

        schemaCard.setDice(new Dice(1, Color.YELLOW), 2, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 2, 1);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 2, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 2, 3);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 2, 4);

        schemaCard.setDice(new Dice(2, Color.BLUE), 3, 0);
        schemaCard.setDice(new Dice(1, Color.RED), 3, 1);
        schemaCard.setDice(new Dice(2, Color.PURPLE), 3, 2);
        schemaCard.setDice(new Dice(1, Color.RED), 3, 3);
        schemaCard.setDice(new Dice(2, Color.BLUE), 3, 4);

        PublicObjectiveCard poc = new DiagonalPublicObjectiveCard("test", "test",
                1, TileConstraintType.NUMBER);
        assertEquals(20*poc.getCardPoints() ,poc.getScore(schemaCard));

        poc = new DiagonalPublicObjectiveCard("test", "test",
                2, TileConstraintType.COLOR);
        assertEquals(9*poc.getCardPoints(),poc.getScore(schemaCard));
    }

    /**
     * Test score.
     * Every dice has at least one diagonal dice with same color
     */
    @Test
    public void testScoreEveryDiagonalSameColorsDifferentNumbers() throws RuleViolationException {
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 1);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 3);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 4);

        schemaCard.setDice(new Dice(3, Color.BLUE), 1, 0);
        schemaCard.setDice(new Dice(4, Color.YELLOW), 1, 1);
        schemaCard.setDice(new Dice(3, Color.BLUE), 1, 2);
        schemaCard.setDice(new Dice(4, Color.YELLOW), 1, 3);
        schemaCard.setDice(new Dice(3, Color.BLUE), 1, 4);

        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 0);
        schemaCard.setDice(new Dice(6, Color.BLUE), 2, 1);
        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 2);
        schemaCard.setDice(new Dice(6, Color.BLUE), 2, 3);
        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 4);

        schemaCard.setDice(new Dice(1, Color.BLUE), 3, 0);
        schemaCard.setDice(new Dice(2, Color.YELLOW), 3, 1);
        schemaCard.setDice(new Dice(1, Color.BLUE), 3, 2);
        schemaCard.setDice(new Dice(2, Color.YELLOW), 3, 3);
        schemaCard.setDice(new Dice(1, Color.BLUE), 3, 4);

        PublicObjectiveCard poc = new DiagonalPublicObjectiveCard("test", "test",
                1, TileConstraintType.NUMBER);
        assertEquals(0 ,poc.getScore(schemaCard));

        poc = new DiagonalPublicObjectiveCard("test", "test",
                2, TileConstraintType.COLOR);
        assertEquals(poc.getCardPoints()*20,poc.getScore(schemaCard));
    }

    /**
     * Test score.
     * None diagonal with same color or same number with different ColorConstraint nor NumberConstraint on each row.
     */
    @Test
    public void testScoreNoneDiagonalDifferentColorsOrNumber() throws RuleViolationException {
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 1);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 3);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 4);

        schemaCard.setDice(new Dice(3, Color.GREEN), 1, 0);
        schemaCard.setDice(new Dice(4, Color.PURPLE), 1, 1);
        schemaCard.setDice(new Dice(3, Color.GREEN), 1, 2);
        schemaCard.setDice(new Dice(4, Color.PURPLE), 1, 3);
        schemaCard.setDice(new Dice(3, Color.GREEN), 1, 4);

        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 0);
        schemaCard.setDice(new Dice(6, Color.BLUE), 2, 1);
        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 2);
        schemaCard.setDice(new Dice(6, Color.BLUE), 2, 3);
        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 4);

        schemaCard.setDice(new Dice(1, Color.RED), 3, 0);
        schemaCard.setDice(new Dice(2, Color.GREEN), 3, 1);
        schemaCard.setDice(new Dice(1, Color.RED), 3, 2);
        schemaCard.setDice(new Dice(2, Color.GREEN), 3, 3);
        schemaCard.setDice(new Dice(1, Color.PURPLE), 3, 4);

        PublicObjectiveCard poc = new DiagonalPublicObjectiveCard("test", "test",
                1, TileConstraintType.NUMBER);
        assertEquals(0 ,poc.getScore(schemaCard));

        poc = new DiagonalPublicObjectiveCard("test", "test",
                2, TileConstraintType.COLOR);
        assertEquals(0,poc.getScore(schemaCard));
    }

}
