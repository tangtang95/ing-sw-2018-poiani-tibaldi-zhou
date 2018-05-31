package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.parser.ParseException;
import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ServerNetworkProtocolTest {

    @DataPoint
    public static List<Dice> diceList;

    @DataPoint
    public static ServerNetworkProtocol serverNetworkProtocol;

    @DataPoint
    public static Map<String, Dice> diceMap;

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

    }

    @Test
    public void test() {
        System.out.println(serverNetworkProtocol.createMessage(new Dice(3, Color.YELLOW)));
        System.out.println(serverNetworkProtocol.createMessage("ciao", "antonio", 45, 78));
        System.out.println(serverNetworkProtocol.createMessage(diceMap));
    }

    @Test
    public void test2() {
        String message = serverNetworkProtocol.createMessage(diceList, new Dice(3, Color.YELLOW));
        try {
            System.out.println(serverNetworkProtocol.parsingResponse(message).toString());
        } catch (ParseException e) {
            System.out.println("PARSING ERROR");
        }
        message = serverNetworkProtocol.createMessage(diceMap);
        try {
            System.out.println(serverNetworkProtocol.parsingResponse(message).toString());
        } catch (ParseException e) {
            System.out.println("PARSING ERROR");
        }

    }
}