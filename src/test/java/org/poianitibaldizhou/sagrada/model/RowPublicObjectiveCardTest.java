package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.ColumnPublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.RowPublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class RowPublicObjectiveCardTest {

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
     * Test score.
     * A single row with different NumberConstraint on each row.
     */
    @Test
    public void testScoreSingleRowWithDifferentNumber() throws RuleViolationException {
        schemaCard.setDice(new Dice(6, Color.YELLOW), 0, 0);
        schemaCard.setDice(new Dice(1, Color.RED), 0, 1);
        schemaCard.setDice(new Dice(5, Color.YELLOW), 0, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 3);
        schemaCard.setDice(new Dice(3, Color.RED), 0, 4);

        schemaCard.setDice(new Dice(3, Color.RED), 1, 2);

        RowPublicObjectiveCard cpoc = new RowPublicObjectiveCard("test", "test",
                3, numberCollection, TileConstraintType.NUMBER);
        assertEquals(3,cpoc.getScore(schemaCard));

        cpoc = new RowPublicObjectiveCard("test", "test",
                3, colorCollection, TileConstraintType.COLOR);
        assertEquals(0,cpoc.getScore(schemaCard));

    }

    /**
     * Test score.
     * A single row with different NumberConstraint on each row.
     */
    @Test
    public void testScoreSingleColumnWithDifferentColor() throws RuleViolationException {
        schemaCard.setDice(new Dice(1, Color.YELLOW), 1, 0);
        schemaCard.setDice(new Dice(2, Color.RED), 1, 1);
        schemaCard.setDice(new Dice(3, Color.GREEN), 1, 2);
        schemaCard.setDice(new Dice(2, Color.PURPLE), 1, 3);
        schemaCard.setDice(new Dice(1, Color.BLUE), 1, 4);
        schemaCard.setDice(new Dice(6, Color.BLUE), 2, 2);

        RowPublicObjectiveCard cpoc = new RowPublicObjectiveCard("test", "test",
                3, colorCollection, TileConstraintType.COLOR);
        assertEquals(3,cpoc.getScore(schemaCard));

        cpoc = new RowPublicObjectiveCard("test", "test",
                3, numberCollection, TileConstraintType.NUMBER);
        assertEquals(0,cpoc.getScore(schemaCard));

    }

    /**
     * Test score.
     * Every row with different NumberConstraint on each row
     */
    @Test
    public void testScoreEveryRowDifferentNumbers() throws RuleViolationException {
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 1);
        schemaCard.setDice(new Dice(3, Color.YELLOW), 0, 2);
        schemaCard.setDice(new Dice(4, Color.BLUE), 0, 3);
        schemaCard.setDice(new Dice(5, Color.YELLOW), 0, 4);

        schemaCard.setDice(new Dice(5, Color.BLUE), 1, 0);
        schemaCard.setDice(new Dice(4, Color.RED), 1, 1);
        schemaCard.setDice(new Dice(6, Color.BLUE), 1, 2);
        schemaCard.setDice(new Dice(2, Color.RED), 1, 3);
        schemaCard.setDice(new Dice(1, Color.BLUE), 1, 4);

        schemaCard.setDice(new Dice(6, Color.YELLOW), 2, 0);
        schemaCard.setDice(new Dice(1, Color.BLUE), 2, 1);
        schemaCard.setDice(new Dice(4, Color.YELLOW), 2, 2);
        schemaCard.setDice(new Dice(5, Color.BLUE), 2, 3);
        schemaCard.setDice(new Dice(2, Color.YELLOW), 2, 4);

        schemaCard.setDice(new Dice(1, Color.BLUE), 3, 0);
        schemaCard.setDice(new Dice(2, Color.RED), 3, 1);
        schemaCard.setDice(new Dice(3, Color.PURPLE), 3, 2);
        schemaCard.setDice(new Dice(4, Color.RED), 3, 3);
        schemaCard.setDice(new Dice(5, Color.BLUE), 3, 4);

        RowPublicObjectiveCard cpoc = new RowPublicObjectiveCard("test", "test",
                2, numberCollection, TileConstraintType.NUMBER);
        assertEquals(2*SchemaCard.NUMBER_OF_ROWS ,cpoc.getScore(schemaCard));

        cpoc = new RowPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.COLOR);
        assertEquals(0,cpoc.getScore(schemaCard));
    }

    /**
     * Test score.
     * Every row with different ColorConstraint on each row.
     */
    @Test
    public void testScoreEveryRowDifferentColors() throws RuleViolationException {
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 1);
        schemaCard.setDice(new Dice(1, Color.PURPLE), 0, 2);
        schemaCard.setDice(new Dice(2, Color.GREEN), 0, 3);
        schemaCard.setDice(new Dice(1, Color.RED), 0, 4);

        schemaCard.setDice(new Dice(3, Color.BLUE), 1, 0);
        schemaCard.setDice(new Dice(4, Color.PURPLE), 1, 1);
        schemaCard.setDice(new Dice(3, Color.GREEN), 1, 2);
        schemaCard.setDice(new Dice(4, Color.RED), 1, 3);
        schemaCard.setDice(new Dice(3, Color.YELLOW), 1, 4);

        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 0);
        schemaCard.setDice(new Dice(6, Color.RED), 2, 1);
        schemaCard.setDice(new Dice(5, Color.PURPLE), 2, 2);
        schemaCard.setDice(new Dice(6, Color.GREEN), 2, 3);
        schemaCard.setDice(new Dice(5, Color.BLUE), 2, 4);

        schemaCard.setDice(new Dice(1, Color.RED), 3, 0);
        schemaCard.setDice(new Dice(2, Color.YELLOW), 3, 1);
        schemaCard.setDice(new Dice(1, Color.GREEN), 3, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 3, 3);
        schemaCard.setDice(new Dice(1, Color.PURPLE), 3, 4);

        RowPublicObjectiveCard cpoc = new RowPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.COLOR);
        assertEquals(2*SchemaCard.NUMBER_OF_ROWS ,cpoc.getScore(schemaCard));

        cpoc = new RowPublicObjectiveCard("test", "test",
                2, numberCollection, TileConstraintType.NUMBER);
        assertEquals(0,cpoc.getScore(schemaCard));
    }

    /**
     * Test score.
     * None row with different ColorConstraint nor NumberConstraint on each row.
     */
    @Test
    public void testScoreNoneRowDifferentColorsOrNumber() throws RuleViolationException {
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 1);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 0, 3);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 0, 4);

        schemaCard.setDice(new Dice(3, Color.GREEN), 1, 0);
        schemaCard.setDice(new Dice(4, Color.PURPLE), 1, 1);
        schemaCard.setDice(new Dice(3, Color.GREEN), 1, 2);
        schemaCard.setDice(new Dice(4, Color.PURPLE), 1, 3);
        schemaCard.setDice(new Dice(3, Color.BLUE), 1, 4);

        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 0);
        schemaCard.setDice(new Dice(6, Color.BLUE), 2, 1);
        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 2);
        schemaCard.setDice(new Dice(6, Color.GREEN), 2, 3);
        schemaCard.setDice(new Dice(5, Color.YELLOW), 2, 4);

        schemaCard.setDice(new Dice(1, Color.RED), 3, 0);
        schemaCard.setDice(new Dice(2, Color.YELLOW), 3, 1);
        schemaCard.setDice(new Dice(1, Color.RED), 3, 2);
        schemaCard.setDice(new Dice(2, Color.BLUE), 3, 3);
        schemaCard.setDice(new Dice(1, Color.PURPLE), 3, 4);

        ColumnPublicObjectiveCard cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, colorCollection, TileConstraintType.COLOR);
        assertEquals(0,cpoc.getScore(schemaCard));

        cpoc = new ColumnPublicObjectiveCard("test", "test",
                2, numberCollection, TileConstraintType.NUMBER);
        assertEquals(0,cpoc.getScore(schemaCard));
    }
}
