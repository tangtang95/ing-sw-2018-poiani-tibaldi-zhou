package org.poianitibaldizhou.sagrada.network.protocol;

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


public class ServerNetworkProtocolTest {

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
    public void test() {/*
        System.out.println(serverNetworkProtocol.createMessage( new Dice(3, Color.YELLOW)));
        System.out.println(serverNetworkProtocol.createMessage("1","2","3", "4", "ciao", "antonio", 45, 78));
        System.out.println(serverNetworkProtocol.createMessage("map",diceMap));
        */
        System.out.println(schemaCard.toString());
        System.out.println(serverNetworkProtocol.createMessage("schema",schemaCard));
    }

    @Test
    public void test2() {
        String message = serverNetworkProtocol.createMessage("1", "2",diceList, new Dice(3, Color.YELLOW));
        try {
            System.out.println(serverNetworkProtocol.getResponseByKey(message,"1").toString());
        } catch (ParseException e) {
            System.out.println("PARSING ERROR");
        }
        message = serverNetworkProtocol.createMessage("1",diceMap);
        try {
            System.out.println(serverNetworkProtocol.getResponseByKey(message, "1").toString());
        } catch (ParseException e) {
            System.out.println("PARSING ERROR");
        }
        message = serverNetworkProtocol.createMessage("schema",schemaCard);
        try {
            System.out.println(schemaCard.equals(serverNetworkProtocol.getResponseByKey(message, "schema")));
        } catch (ParseException e) {
            System.out.println("PARSING ERROR");
        }
    }
}