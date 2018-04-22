package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.ColumnPublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ColumnPublicObjectiveCardTest {

    @DataPoint
    public static SchemaCard schemaCard;

    @DataPoint
    public static IConstraint[][] constraints;

    @DataPoint
    public static Collection<IConstraint> colorCollection;

    @DataPoint
    public static Collection<IConstraint> numberCollection;

    @BeforeClass
    public static void setUpClass() {
        constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        constraints[1][1] = new ColorConstraint(Color.YELLOW);
        constraints[1][2] = new ColorConstraint(Color.RED);

        colorCollection = new ArrayList<>();
        for(Color c:Color.values()) {
            colorCollection.add(new ColorConstraint(c));
        }

        numberCollection = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            numberCollection.add(new NumberConstraint(i));
        }
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
     * Every column with different NumberConstraint or ColorConstraint on each row
     */
    @Test
    public void testScoreEveryColumnDifferentNumbersOrColor() throws RuleViolationException {
        for (int i = 0; i < SchemaCard.NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_ROWS; j++) {
                if (j % 2 == 0)
                    schemaCard.setDice(new Dice(j+1, Color.BLUE), j, i);
                else
                    schemaCard.setDice(new Dice(i+1, Color.values()[j]), j, i);
            }
        }

        ColumnPublicObjectiveCard cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.NUMBER);
        assertEquals(2*3 ,cpoc.getScore(schemaCard));

        cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.COLOR);
        assertEquals(2*2 ,cpoc.getScore(schemaCard));

    }

    /**
     * Test score.
     * A single column with different NumberConstraint on each row.
     */
    @Test
    public void testScoreSingleColumnWithDifferentNumber() throws RuleViolationException {
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            schemaCard.setDice(new Dice(i+1, Color.BLUE), i, 1);
        }
        ColumnPublicObjectiveCard cpoc = new ColumnPublicObjectiveCard("test", "test",
                3, colorCollection, TileConstraintType.NUMBER);
        assertEquals(3,cpoc.getScore(schemaCard));

        cpoc = new ColumnPublicObjectiveCard("test", "test",
                3, colorCollection, TileConstraintType.COLOR);
        assertEquals(0,cpoc.getScore(schemaCard));

    }

    /**
     * Test score.
     * A single column with different NumberConstraint on each row.
     */
    @Test
    public void testScoreSingleColumnWithDifferentColor() throws RuleViolationException {
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            schemaCard.setDice(new Dice(1, Color.values()[i]), i, 2);
        }
        ColumnPublicObjectiveCard cpoc = new ColumnPublicObjectiveCard("test", "test",
                3, colorCollection, TileConstraintType.COLOR);
        assertEquals(3,cpoc.getScore(schemaCard));

        cpoc = new ColumnPublicObjectiveCard("test", "test",
                3, colorCollection, TileConstraintType.NUMBER);
        assertEquals(0,cpoc.getScore(schemaCard));

    }

    /**
     * Test score.
     * Every column with different NumberConstraint on each row
     */
    @Test
    public void testScoreEveryColumnDifferentNumbers() throws RuleViolationException {
        for (int i = 0; i < SchemaCard.NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_ROWS; j++) {
                schemaCard.setDice(new Dice(j+1, Color.BLUE), j, i);
            }
        }

        ColumnPublicObjectiveCard cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.NUMBER);
        assertEquals(2*SchemaCard.NUMBER_OF_COLUMNS ,cpoc.getScore(schemaCard));

        cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.NUMBER);
        assertEquals(0,cpoc.getScore(schemaCard));

    }

    /**
     * Test score.
     * Every column with different ColorConstraint on each row.
     */
    @Test
    public void testScoreEveryColumnDifferentColors() throws RuleViolationException {
        for (int i = 0; i < SchemaCard.NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_ROWS; j++) {
                schemaCard.setDice(new Dice(i+1, Color.values()[j]), j, i);
            }
        }

        ColumnPublicObjectiveCard cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.COLOR);
        assertEquals(2*SchemaCard.NUMBER_OF_COLUMNS ,cpoc.getScore(schemaCard));

        cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.NUMBER);
        assertEquals(0,cpoc.getScore(schemaCard));
    }

    /**
     * Test score.
     * Every column with different ColorConstraint on each row.
     */
    @Test
    public void testScoreNoneColumnDifferentColorsOrNumber() throws RuleViolationException {
        for (int i = 0; i < SchemaCard.NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_ROWS; j++) {
                schemaCard.setDice(new Dice(1,Color.YELLOW), i, j);
            }
        }

        ColumnPublicObjectiveCard cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.COLOR);
        assertEquals(0,cpoc.getScore(schemaCard));

        cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.NUMBER);
        assertEquals(0,cpoc.getScore(schemaCard));
    }
}
