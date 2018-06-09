package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ServerGetMessageTest {

    @DataPoint
    public static List<Dice> diceList;

    @DataPoint
    public static JSONProtocol serverNetworkProtocol;

    @DataPoint
    public static Map<String, Dice> diceMap;

    @DataPoint
    public static SchemaCard schemaCard;

    @BeforeClass
    public static void setUpClass() {
        diceList = new ArrayList<>();
        diceMap = new HashMap<>();
    }

    @Before
    public void setUp() {
        serverNetworkProtocol = new JSONProtocol();

        diceList.add(new Dice(1, Color.BLUE));
        diceList.add(new Dice(2, Color.GREEN));
        diceList.add(new Dice(3, Color.BLUE));
        diceList.add(new Dice(4, Color.BLUE));
        diceList.add(new Dice(5, Color.YELLOW));

        diceMap.put("1", new Dice(1, Color.BLUE));
        diceMap.put("3", new Dice(1, Color.YELLOW));
        diceMap.put("4", new Dice(1, Color.RED));
        diceMap.put("6", new Dice(1, Color.PURPLE));

        IConstraint[][] matrix = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                matrix[i][j] = new ColorConstraint(Color.YELLOW);
            }
        }
        schemaCard = new SchemaCard("TestToolCard", 100, matrix);
        try {
            schemaCard.setDice(new Dice(4, Color.YELLOW), 0, 0);
        } catch (RuleViolationException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDiceMessage() {
        String message = "{\"test\":{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":3}}}";
        serverNetworkProtocol.appendMessage("test", new Dice(3, Color.YELLOW));
        assertEquals(message, serverNetworkProtocol.buildMessage());
    }



    @Test
    public void booleanTest() {
        serverNetworkProtocol.appendMessage("boolean", true);
        String bool = serverNetworkProtocol.buildMessage();
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(bool, "boolean");
            assertEquals(true, Boolean.valueOf(jsonObject.get(SharedConstants.BODY).toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}