package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;

import java.io.IOException;
import java.util.Objects;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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


    /**
     * Test branch coverage of PrivateObjectiveCard.getMultiPlayerScore().
     */
    @Test
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

    @Test
    public void testToJSON(){
        privateObjectiveCard = new PrivateObjectiveCard("Sfumature gialle", "Test", Color.YELLOW);
        String message = "{\"type\":\"privateObjectiveCard\",\"body\":{\"color\":\"YELLOW\",\"name\":\"Sfumature gialle\",\"description\":\"Test\"}}";
        assertTrue(message.equals(privateObjectiveCard.toJSON().toJSONString()));

    }

    @Test
    public void testToObject(){
        privateObjectiveCard = new PrivateObjectiveCard("Sfumature Rosse - Privata",
                "Somma dei valori su tutti i dadi rossi", Color.RED);
        String message = "{\"name\":\"Sfumature Rosse - Privata\"}";
        org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        try {
            assertTrue((Objects.requireNonNull(PrivateObjectiveCard.toObject((JSONObject) jsonParser.parse(message)))).equals(privateObjectiveCard));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToObjectException(){
        String message = "{}";
        org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        try {
            assertNull(PrivateObjectiveCard.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToObjectException2(){
        String message = "{\"name\":\"Sfumature Marroni\"}";
        org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        try {
            assertNull(PrivateObjectiveCard.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = Exception.class)
    public void testToObjectObserver() {
        Dice dice = new Dice(2, Color.PURPLE);
        JSONObject jsonObject = dice.toJSON();

        PrivateObjectiveCard.toObject(jsonObject);
    }
    
}
