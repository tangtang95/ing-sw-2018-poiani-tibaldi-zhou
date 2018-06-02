package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.*;
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

import static org.junit.Assert.*;

public class ProtocolTest {

    @DataPoint
    public static List<Dice> diceList;

    @DataPoint
    public static ServerNetworkProtocol serverNetworkProtocol;

    @DataPoint
    public static Map<String, Dice> diceMap;

    @DataPoint
    public static SchemaCard schemaCard;

    @BeforeClass
    public static void setUpClass() {
        diceList = new ArrayList<>();
        serverNetworkProtocol = new ServerNetworkProtocol();
        diceMap = new HashMap<>();
    }

    @Before
    public void setUp() {
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
                matrix[i][j]= new ColorConstraint(Color.YELLOW);
            }
        }
        schemaCard = new SchemaCard("TestToolCard", 100, matrix);
        try {
            schemaCard.setDice(new Dice(4,Color.YELLOW),0,0);
        } catch (RuleViolationException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test() {
        String message = "{\"test\":{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":3}}}";
        assertEquals(message,serverNetworkProtocol.createMessage( "test",new Dice(3, Color.YELLOW)));
        String message1 = "{\"1\":{\"type\":\"string\",\"body\":\"ciao\"},\"2\":{\"type\":\"string\",\"body\":\"antonio\"}," +
                "\"3\":{\"type\":\"integer\",\"body\":\"45\"},\"4\":{\"type\":\"integer\",\"body\":\"78\"}}";
        assertEquals(message1,serverNetworkProtocol.createMessage("1","2","3", "4", "ciao", "antonio", 45, 78));
        String message2 = "{\"map\":{\"type\":\"map\",\"body\":" +
                "{\"{\\\"type\\\":\\\"string\\\",\\\"body\\\":\\\"1\\\"}\":\"{\\\"type\\\":\\\"dice\\\"," +
                "\\\"body\\\":{\\\"color\\\":\\\"BLUE\\\",\\\"value\\\":1}}\",\"{\\\"type\\\":\\\"string\\\"," +
                "\\\"body\\\":\\\"4\\\"}\":\"{\\\"type\\\":\\\"dice\\\",\\\"body\\\":{\\\"color\\\":\\\"RED\\\"," +
                "\\\"value\\\":1}}\",\"{\\\"type\\\":\\\"string\\\",\\\"body\\\":\\\"3\\\"}\":\"{\\\"type\\\":\\\"dice\\\"," +
                "\\\"body\\\":{\\\"color\\\":\\\"YELLOW\\\",\\\"value\\\":1}}\",\"{\\\"type\\\":\\\"string\\\"," +
                "\\\"body\\\":\\\"6\\\"}\":\"{\\\"type\\\":\\\"dice\\\",\\\"body\\\":{\\\"color\\\":\\\"PURPLE\\\"," +
                "\\\"value\\\":1}}\"}}}";
        assertEquals(message2,serverNetworkProtocol.createMessage("map",diceMap));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test2() {
        String message = serverNetworkProtocol.createMessage("1", "2", "3",diceList, diceMap, schemaCard);
        try {
            assertEquals(diceList,serverNetworkProtocol.getResponseByKey(message,"1"));
            assertEquals(diceMap,serverNetworkProtocol.getResponseByKey(message,"2"));
            assertEquals(schemaCard,serverNetworkProtocol.getResponseByKey(message,"3"));
        } catch (ParseException e) {
            fail("PARSING ERROR");
        }

        ClientNetworkProtocol clientNetworkProtocol = new ClientNetworkProtocol();
        String send = serverNetworkProtocol.createMessage("1", "2", "3",diceList, diceMap, schemaCard);
        try {
            List<JSONObject> listOfDice = (List<JSONObject>) clientNetworkProtocol.getResponseByKey(send,"1");
            String response = clientNetworkProtocol.createMessage("1", listOfDice);
            assertEquals(diceList,serverNetworkProtocol.getResponseByKey(response,"1"));
        } catch (ParseException e) {
            fail("PARSING ERROR");
        }

    }
}