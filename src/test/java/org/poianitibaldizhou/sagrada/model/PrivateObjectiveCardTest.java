package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCardPoint;

import static org.junit.Assert.assertEquals;

public class PrivateObjectiveCardTest {

    @DataPoint
    public static SchemaCard schemaCard;

    @DataPoint
    public static PrivateObjectiveCard privateObjectiveCard;

    @DataPoint
    public static IConstraint[][] constraints;

    @BeforeClass
    public static void setUpClass() {
        constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
    }

    @AfterClass
    public static void tearDownClass(){
        constraints = null;
    }

    @After
    public void tearDown() {
        schemaCard = null;
        privateObjectiveCard = null;
    }

    @Before
    public void setUp(){
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new NoConstraint();
            }
        }
    }

    /**
     * Test the absence of tile with ColorConstraint of the PrivateObjectiveCard.
     * Score should be 0.
     */
    @Test
    public void testNoDiceOfCardColorConstraint() throws RuleViolationException {

        schemaCard = new SchemaCard("testNoDiceOfCardColorConstraint", 1, constraints);
        schemaCard.setDice(new Dice(4, Color.BLUE), new SchemaCardPoint(0,1));
        schemaCard.setDice(new Dice(3, Color.RED), new SchemaCardPoint(1, 1));
        schemaCard.setDice(new Dice(6, Color.GREEN), new SchemaCardPoint(2,1));
        schemaCard.setDice(new Dice(5, Color.YELLOW), new SchemaCardPoint(2,
                2));


        privateObjectiveCard = new PrivateObjectiveCard("Sfumature viola", "",
                new ColorConstraint(Color.PURPLE));

        assertEquals("Wrong score",0, privateObjectiveCard.getScore(schemaCard));
    }

    @Test
    /**
     * Test branch coverage of PrivateObjectiveCard.getScore().
     */
    public void testBranchCoverage() throws RuleViolationException {
        schemaCard = new SchemaCard("TestBranchCoverage", 3, constraints);
        schemaCard.setDice(new Dice(4, Color.BLUE), new SchemaCardPoint(0,1));
        schemaCard.setDice(new Dice(3, Color.YELLOW), new SchemaCardPoint(0,2));
        schemaCard.setDice(new Dice(2, Color.GREEN), new SchemaCardPoint(1,1));
        schemaCard.setDice(new Dice(6, Color.YELLOW), new SchemaCardPoint(2,2));
        schemaCard.setDice(new Dice(5, Color.PURPLE), new SchemaCardPoint(1,0));

        privateObjectiveCard = new PrivateObjectiveCard("Sfumature gialle", "", Color.YELLOW);
        assertEquals("Wrong score", 9, privateObjectiveCard.getScore(schemaCard));
    }
}
