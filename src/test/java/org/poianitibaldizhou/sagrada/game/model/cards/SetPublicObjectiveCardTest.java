package org.poianitibaldizhou.sagrada.game.model.cards;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.SetPublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.ObjectiveCardType;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


public class SetPublicObjectiveCardTest {

    @DataPoint
    public static Collection<IConstraint> allNumberConstraint;

    @DataPoint
    public static Collection<IConstraint> allColorConstraint;

    @DataPoint
    public static SchemaCard schemaCard;

    @BeforeClass
    public static void setUpClass() {
        allNumberConstraint = new ArrayList<>();
        for (int i = 1; i <= Dice.MAX_VALUE; i++)
            allNumberConstraint.add(new NumberConstraint(i));

        allColorConstraint = new ArrayList<>();
        for (Color c : Color.values())
            allColorConstraint.add(new ColorConstraint(c));
    }

    @AfterClass
    public static void tearDownClass() {
        allNumberConstraint = null;
        allColorConstraint = null;
    }

    @Before
    public void setUp() {
        IConstraint[][] constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        schemaCard = new SchemaCard("Test", 4, constraints);
    }

    @After
    public void tearDown() {
        schemaCard = null;
    }

    /**
     * Test a RowPublicObjectiveCard with the all the possible ColorConstraint.
     * The constraints of the tuple aren't present in equal number in the SchemaCard.
     */
    @Test
    public void testWholeColorSet() throws RuleViolationException {
        schemaCard.setDice(new Dice(2, Color.YELLOW), 0, 4);
        schemaCard.setDice(new Dice(3, Color.BLUE), 1, 4);
        schemaCard.setDice(new Dice(4, Color.GREEN), 1, 3);
        schemaCard.setDice(new Dice(2, Color.PURPLE), 2, 3);
        schemaCard.setDice(new Dice(1, Color.RED), 2, 2);
        schemaCard.setDice(new Dice(6, Color.YELLOW), 2, 1);
        schemaCard.setDice(new Dice(1, Color.BLUE), 2, 0);
        schemaCard.setDice(new Dice(6, Color.GREEN), 3, 0);
        schemaCard.setDice(new Dice(5, Color.PURPLE), 3, 1);
        schemaCard.setDice(new Dice(5, Color.RED), 0, 3);


        SetPublicObjectiveCard setPublicObjectiveCard = new SetPublicObjectiveCard("Test", "Test",
                3, allColorConstraint, ObjectiveCardType.COLOR);

        assertEquals(2*setPublicObjectiveCard.getCardPoints(), setPublicObjectiveCard.getScore(schemaCard));
    }

    /**
     * Test a RowPublicObjectiveCard with the all the possible ColorConstraint.
     * The constraints of the tuple aren't present in equal number in the SchemaCard.
     */
    @Test
    public void testWholeColorSetWithOffset() throws RuleViolationException {
        schemaCard.setDice(new Dice(2, Color.YELLOW), 0, 1);
        schemaCard.setDice(new Dice(1, Color.BLUE), 0, 2);
        schemaCard.setDice(new Dice(3, Color.YELLOW), 0, 3);
        schemaCard.setDice(new Dice(2, Color.BLUE), 1, 3);
        schemaCard.setDice(new Dice(5, Color.RED), 1, 4);
        schemaCard.setDice(new Dice(5, Color.GREEN), 1, 1);
        schemaCard.setDice(new Dice(6, Color.YELLOW), 2, 4);
        schemaCard.setDice(new Dice(6, Color.BLUE), 2, 1);
        schemaCard.setDice(new Dice(4, Color.PURPLE), 1, 0);
        schemaCard.setDice(new Dice(4, Color.YELLOW), 3, 1);

        SetPublicObjectiveCard setPublicObjectiveCard = new SetPublicObjectiveCard("Test", "Test",
                4, allColorConstraint, ObjectiveCardType.COLOR);

        assertEquals(setPublicObjectiveCard.getCardPoints(), setPublicObjectiveCard.getScore(schemaCard));
    }

    /**
     * Test a RowPublicObjectiveCard with the all the possible NumberConstraint.
     * The constraints of the tuple are present in equal number in the SchemaCard.
     */
    @Test
    public void testWholeNumberSetWithOffset() throws RuleViolationException {
        schemaCard.setDice(new Dice(2, Color.YELLOW), 0, 1);
        schemaCard.setDice(new Dice(1, Color.BLUE), 0, 2);
        schemaCard.setDice(new Dice(3, Color.YELLOW), 0, 3);
        schemaCard.setDice(new Dice(2, Color.BLUE), 1, 3);
        schemaCard.setDice(new Dice(5, Color.RED), 1, 4);
        schemaCard.setDice(new Dice(5, Color.GREEN), 1, 1);
        schemaCard.setDice(new Dice(6, Color.YELLOW), 2, 4);
        schemaCard.setDice(new Dice(6, Color.BLUE), 2, 1);
        schemaCard.setDice(new Dice(4, Color.PURPLE), 1, 0);
        schemaCard.setDice(new Dice(4, Color.YELLOW), 3, 1);
        schemaCard.setDice(new Dice(3, Color.BLUE), 3, 2);

        SetPublicObjectiveCard setPublicObjectiveCard = new SetPublicObjectiveCard("Test", "Test",
                4, allNumberConstraint, ObjectiveCardType.NUMBER);

        assertEquals(setPublicObjectiveCard.getCardPoints(), setPublicObjectiveCard.getScore(schemaCard));
    }

    /**
     * Test a RowPublicObjectiveCard with the all the possible NumberConstraint.
     * The constraints of the tuple are present in equal number in the SchemaCard.
     */
    @Test
    public void testWholeNumberSet() throws RuleViolationException {
        schemaCard.setDice(new Dice(2, Color.YELLOW), 0, 1);
        schemaCard.setDice(new Dice(1, Color.BLUE), 0, 2);
        schemaCard.setDice(new Dice(3, Color.YELLOW), 0, 3);
        schemaCard.setDice(new Dice(2, Color.BLUE), 1, 3);
        schemaCard.setDice(new Dice(5, Color.RED), 1, 4);
        schemaCard.setDice(new Dice(5, Color.GREEN), 1, 1);
        schemaCard.setDice(new Dice(6, Color.YELLOW), 2, 4);
        schemaCard.setDice(new Dice(6, Color.BLUE), 2, 1);
        schemaCard.setDice(new Dice(1, Color.RED), 0, 0);
        schemaCard.setDice(new Dice(4, Color.PURPLE), 1, 0);
        schemaCard.setDice(new Dice(4, Color.YELLOW), 3, 1);
        schemaCard.setDice(new Dice(3, Color.BLUE), 3, 2);

        SetPublicObjectiveCard setPublicObjectiveCard = new SetPublicObjectiveCard("Test", "Test",
                4, allNumberConstraint, ObjectiveCardType.NUMBER);

        assertEquals(2 * setPublicObjectiveCard.getCardPoints(), setPublicObjectiveCard.getScore(schemaCard));
    }

    /**
     * Test a RowPublicObjectiveCard with a NumberConstraint pair.
     * The constraints of the pair are present in equal number in the SchemaCard.
     */
    @Test
    public void testNumberSetPair() throws RuleViolationException {
        Collection<IConstraint> pair = new ArrayList<>();
        pair.add(new NumberConstraint(3));
        pair.add(new NumberConstraint(4));

        schemaCard.setDice(new Dice(2, Color.YELLOW), 0, 1);
        schemaCard.setDice(new Dice(4, Color.BLUE), 1, 1);
        schemaCard.setDice(new Dice(3, Color.PURPLE), 1, 2);
        schemaCard.setDice(new Dice(6, Color.YELLOW), 2, 1);
        schemaCard.setDice(new Dice(4, Color.BLUE), 2, 0);
        schemaCard.setDice(new Dice(2, Color.BLUE), 2, 2);
        schemaCard.setDice(new Dice(3, Color.RED), 2, 3);

        SetPublicObjectiveCard setPublicObjectiveCard = new SetPublicObjectiveCard("Test", "Test",
                4, pair, ObjectiveCardType.NUMBER);

        assertEquals(2 * setPublicObjectiveCard.getCardPoints(), setPublicObjectiveCard.getScore(schemaCard));
    }

    /**
     * Test a RowPublicObjectiveCard with a NumberConstraint pair.
     * The constraints of the pair aren't present in equal number in the SchemaCard.
     */
    @Test
    public void testNumberSetPairWithOffset() throws RuleViolationException {
        Collection<IConstraint> pair = new ArrayList<>();
        pair.add(new NumberConstraint(1));
        pair.add(new NumberConstraint(2));

        schemaCard.setDice(new Dice(2, Color.YELLOW), 0, 1);
        schemaCard.setDice(new Dice(3, Color.BLUE), 1, 1);
        schemaCard.setDice(new Dice(2, Color.PURPLE), 1, 2);
        schemaCard.setDice(new Dice(1, Color.YELLOW), 2, 1);
        schemaCard.setDice(new Dice(4, Color.BLUE), 2, 0);
        schemaCard.setDice(new Dice(1, Color.BLUE), 0, 0);
        schemaCard.setDice(new Dice(2, Color.RED), 2, 3);

        SetPublicObjectiveCard setPublicObjectiveCard = new SetPublicObjectiveCard("Test", "Test",
                4, pair, ObjectiveCardType.NUMBER);

        assertEquals(2 * setPublicObjectiveCard.getCardPoints(), setPublicObjectiveCard.getScore(schemaCard));
    }
}

