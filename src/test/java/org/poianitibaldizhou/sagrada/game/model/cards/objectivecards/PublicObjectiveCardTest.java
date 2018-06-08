package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PublicObjectiveCardTest {
    @DataPoint
    public static PublicObjectiveCard publicObjectiveCard;

    @DataPoint
    public static Collection<IConstraint> constraints;

    @BeforeClass
    public static void setUpClass() {
        constraints = new ArrayList<>();
    }

    @Before
    public void setUp() {
        constraints.clear();
    }

    /**
     * Test PublicObjectiveCard constructor.
     */
    @Test
    public void testConstructorException() {
        // Test differents type of tile constraint
        constraints.add(new NumberConstraint(5));
        constraints.add(new ColorConstraint(Color.GREEN));
        constraints.add(new NoConstraint());
        try {
            publicObjectiveCard = new SetPublicObjectiveCard("test", "test", 2, constraints,
                    ObjectiveCardType.NUMBER);
            fail("Exception expected");
        } catch (Exception e) {
            e.printStackTrace();
        }

        constraints.clear();

        // Test constraints containing only only a single type of constraint except one
        constraints.addAll(NumberConstraint.getAllNumberConstraint());
        constraints.add(new ColorConstraint(Color.YELLOW));
        try {
            publicObjectiveCard = new SetPublicObjectiveCard("test", "test", 2, constraints,
                    ObjectiveCardType.NUMBER);
            fail("Exception expected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test PublicObjectiveCard.containsConstraint()
     */
    @Test
    public void testContainsConstraint() {
        for (Color c : Color.values()) {
            constraints.add(new ColorConstraint(c));
        }

        try {
            publicObjectiveCard = new ColumnPublicObjectiveCard("Test", "Test",
                    5, ObjectiveCardType.COLOR);

            for (int i = 1; i <= 6; i++) {
                assertEquals("No NumberConstraint expected",false,
                        publicObjectiveCard.containsConstraint(new NumberConstraint(i)));
            }

            for (Color c : Color.values()) {
                assertEquals("Any ColorConstraint expected",true,
                        publicObjectiveCard.containsConstraint(new ColorConstraint(c)));
            }

        } catch (IllegalArgumentException e) {
            fail("No exception expected");
        }
   }

    @Test
    public void testToJSON(){
        publicObjectiveCard = new ColumnPublicObjectiveCard("Test", "Test",
                5, ObjectiveCardType.COLOR);
        String message = "{\"type\":\"publicObjectiveCard\",\"body\":{\"name\":\"Test\",\"description\":\"Test\",\"cardPoint\":5}}";
        assertTrue(message.equals(publicObjectiveCard.toJSON().toJSONString()));

    }

    @Test
    public void testToObject(){
        publicObjectiveCard = new ColumnPublicObjectiveCard("Test", "Test",
                5, ObjectiveCardType.COLOR);
        String message = "{\"name\":\"Test\"}";
        org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        try {
            assertTrue((PublicObjectiveCard.toObject((JSONObject) jsonParser.parse(message))) == null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
