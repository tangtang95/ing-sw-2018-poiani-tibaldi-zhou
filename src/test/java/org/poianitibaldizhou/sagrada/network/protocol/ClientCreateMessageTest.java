package org.poianitibaldizhou.sagrada.network.protocol;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import static org.junit.Assert.*;

public class ClientCreateMessageTest {

    @DataPoint
    public static ClientCreateMessage clientCreateMessage;

    @Before
    public void setUp() throws Exception {
        clientCreateMessage = new ClientCreateMessage();
    }

    @Test
    public void createGameNameMessageTest() {
        String gameName = "Emperor";
        String message = "{\"gameNameKey\":{\"type\":\"string\",\"body\":\"Emperor\"}}";
        assertEquals(message, clientCreateMessage.createGameNameMessage(gameName).buildMessage());
    }

    @Test
    public void createTokenMessageTest() {
        String token = "123456789";
        String message = "{\"tokenKey\":{\"type\":\"string\",\"body\":\"123456789\"}}";
        assertEquals(message, clientCreateMessage.createTokenMessage(token).buildMessage());
    }

    @Test
    public void createSchemaCardMessageTest() {
        TileWrapper[][] constraints = new TileWrapper[SchemaCardWrapper.NUMBER_OF_ROWS][SchemaCardWrapper.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCardWrapper.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCardWrapper.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new TileWrapper(null);
            }
        }
        SchemaCardWrapper emptySchemaCard = new SchemaCardWrapper("test1", 1, constraints);
        String message = "{\"schemaCard\":{\"type\":\"schemaCard\",\"body\":" +
                "{\"difficulty\":1,\"name\":\"test1\",\"matrix\":" +
                "[[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}}";
        assertEquals(message,clientCreateMessage.createSchemaCardMessage(emptySchemaCard).buildMessage());
    }

    @Test
    public void createUsernameMessageTest() {
        String username = "test";
        String message = "{\"usernameKey\":{\"type\":\"string\",\"body\":\"test\"}}";
        assertEquals(message,clientCreateMessage.createUsernameMessage(username).buildMessage());
    }

    @Test
    public void createValueMessageTest() {
        int value = 5;
        String message = "{\"integer\":{\"type\":\"integer\",\"body\":\"5\"}}";
        assertEquals(message,clientCreateMessage.createValueMessage(value).buildMessage());
    }

    @Test
    public void createDiceMessageTest() {
        DiceWrapper diceWrapper = new DiceWrapper(ColorWrapper.BLUE,5);
        String message = "{\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":5}}}";
        assertEquals(message,clientCreateMessage.createDiceMessage(diceWrapper).buildMessage());
    }

    @Test
    public void createColorMessageTest() {
        ColorWrapper colorWrapper = ColorWrapper.GREEN;
        String message = "{\"color\":{\"type\":\"color\",\"body\":\"GREEN\"}}";
        assertEquals(message,clientCreateMessage.createColorMessage(colorWrapper).buildMessage());
    }

    @Test
    public void createBooleanMessageTest() {
        String message = "{\"boolean\":{\"type\":\"boolean\",\"body\":\"true\"}}";
        assertEquals(message,clientCreateMessage.createBooleanMessage(true).buildMessage());
    }

    @Test
    public void createPositionMessageTest() {
        PositionWrapper positionWrapper = new PositionWrapper(1,1);
        String message = "{\"position\":{\"type\":\"position\",\"body\":{\"column\":1,\"row\":1}}}";
        assertEquals(message,clientCreateMessage.createPositionMessage(positionWrapper).buildMessage());
    }

    @Test
    public void createToolCardMessageTest() {
        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("test","test",ColorWrapper.PURPLE, 1);
        String message = "{\"toolCard\":{\"type\":\"toolCard\",\"body\":{\"name\":\"test\",\"token\":1}}}";
        assertEquals(message,clientCreateMessage.createToolCardMessage(toolCardWrapper).buildMessage());
    }

    @Test
    public void createActionMessageTest() {
        IActionWrapper iActionWrapper = new UseToolCardActionWrapper();
        String message = "{\"actionKey\":{\"type\":\"useToolCardAction\",\"body\":{}}}";
        assertEquals(message,clientCreateMessage.createActionMessage(iActionWrapper).buildMessage());
    }

    @Test
    public void createPrivateObjectiveCardMessage() {
        PrivateObjectiveCardWrapper privateObjectiveCardWrapper = new PrivateObjectiveCardWrapper("test",
                "test", ColorWrapper.GREEN);
        String message = "{\"privateObjectiveCard\":{\"type\":\"privateObjectiveCard\",\"body\":{\"color\":\"GREEN\",\"name\":\"test\",\"description\":\"test\"}}}";
        assertEquals(message,
                clientCreateMessage.createPrivateObjectiveCardMessage(
                        privateObjectiveCardWrapper).buildMessage());
    }
}