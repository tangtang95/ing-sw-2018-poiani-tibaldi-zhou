package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class CommandFlowTest {

    @DataPoint
    public static CommandFlow commandFlow;

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"commandFlow\",\"body\":\"REPEAT\"}";
        assertEquals(message, commandFlow.toJSON().toJSONString());
    }

    @Test
    public void toObjectTest() {
        String message = "{\"type\":\"commandFlow\",\"body\":\"REPEAT\"}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(commandFlow, CommandFlow.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        commandFlow = CommandFlow.REPEAT;
    }
}