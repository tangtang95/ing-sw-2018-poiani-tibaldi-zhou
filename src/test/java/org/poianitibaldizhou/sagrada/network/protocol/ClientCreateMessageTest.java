package org.poianitibaldizhou.sagrada.network.protocol;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.TileWrapper;

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

    
}