package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;

import static org.junit.Assert.*;

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
    public void testNoDiceOfCardColorConstraint() throws Exception {

        schemaCard = new SchemaCard("testNoDiceOfCardColorConstraint", 1, constraints);
        schemaCard.setDice(new Dice(4, Color.BLUE), 0,1);
        schemaCard.setDice(new Dice(3, Color.RED), 1, 1);
        schemaCard.setDice(new Dice(6, Color.GREEN),2,1);
        schemaCard.setDice(new Dice(5, Color.YELLOW),2, 2);


        privateObjectiveCard = new PrivateObjectiveCard("Sfumature viola", "",
                new ColorConstraint(Color.PURPLE));

        assertEquals("Wrong score",0, privateObjectiveCard.getScore(schemaCard));
    }

    @Test
    /**
     * Test branch coverage of PrivateObjectiveCard.getMultiPlayerScore().
     */
    public void testBranchCoverage() throws Exception {
        schemaCard = new SchemaCard("TestBranchCoverage", 3, constraints);
        schemaCard.setDice(new Dice(4, Color.BLUE),0,1);
        schemaCard.setDice(new Dice(3, Color.YELLOW), 0,2);
        schemaCard.setDice(new Dice(2, Color.GREEN), 1,1);
        schemaCard.setDice(new Dice(6, Color.YELLOW),2,2);
        schemaCard.setDice(new Dice(5, Color.PURPLE), 1,0);

        privateObjectiveCard = new PrivateObjectiveCard("Sfumature gialle", "", Color.YELLOW);
        assertEquals("Wrong score", 9, privateObjectiveCard.getScore(schemaCard));
    }

    @Test
    public void testGetConstraint(){
        ColorConstraint cc = new ColorConstraint(Color.YELLOW);
        privateObjectiveCard = new PrivateObjectiveCard("Sfumature gialle", "", cc);
        assertEquals(cc, privateObjectiveCard.getConstraint());
    }

    @Test
    public void testEquals(){
        PrivateObjectiveCard poc1 = new PrivateObjectiveCard("Sfumature gialle", "", Color.YELLOW);
        PrivateObjectiveCard poc2 = new PrivateObjectiveCard("Sfumature blu", "", Color.BLUE);
        PrivateObjectiveCard poc2a = new PrivateObjectiveCard("Sfumature blu", "a", Color.BLUE);
        PrivateObjectiveCard poc2b = new PrivateObjectiveCard("Sfumature blu", "a", Color.BLUE);

        assertFalse(poc1.equals(PublicObjectiveCard.class));
        assertTrue(poc1.equals(poc1));
        assertFalse(poc1.equals(poc2));
        assertFalse(poc2.equals(poc2a));
        assertTrue(poc2a.equals(poc2b));
    }

    @Test
    public void testHashCode(){
        PrivateObjectiveCard poc1 = new PrivateObjectiveCard("Sfumature gialle", "", Color.YELLOW);
        PrivateObjectiveCard poc2 = new PrivateObjectiveCard("Sfumature blu", "", Color.BLUE);
        PrivateObjectiveCard poc2a = new PrivateObjectiveCard("Sfumature blu", "a", Color.BLUE);
        PrivateObjectiveCard poc2b = new PrivateObjectiveCard("Sfumature blu", "a", Color.BLUE);

        assertEquals(poc1.hashCode(), poc1.hashCode());
        assertNotEquals(poc2.hashCode(), poc1.hashCode());
        assertNotEquals(poc2.hashCode(), poc2a.hashCode());
        assertEquals(poc2a.hashCode(), poc2b.hashCode());
    }
}
