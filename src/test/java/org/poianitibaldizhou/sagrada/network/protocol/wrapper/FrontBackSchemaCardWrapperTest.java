package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class FrontBackSchemaCardWrapperTest {

    @DataPoint
    public static FrontBackSchemaCardWrapper frontBackSchemaCardWrapper;

    @DataPoint
    public static SchemaCardWrapper fullSchemaCardWrapper;

    @Before
    public void setUp() throws Exception {
        frontBackSchemaCardWrapper = new FrontBackSchemaCardWrapper();
        DiceWrapper d1 = new DiceWrapper(ColorWrapper.YELLOW,4);

        TileWrapper[][] constraints = new TileWrapper[SchemaCardWrapper.NUMBER_OF_ROWS][SchemaCardWrapper.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCardWrapper.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCardWrapper.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new TileWrapper(null);
            }
        }
        SchemaCardWrapper emptySchemaCard = new SchemaCardWrapper("test1", 1, constraints);

        constraints[0][2] = new TileWrapper(ColorWrapper.YELLOW.name());
        constraints[1][3] = new TileWrapper("4");
        constraints[2][4] = new TileWrapper(ColorWrapper.RED.name());
        constraints[0][1] = new TileWrapper("2");
        fullSchemaCardWrapper = new SchemaCardWrapper("test3", 2, constraints);
    }

    @Test
    public void toObjectTest() {
        String message = "{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"schemaCard\",\"body\":" +
                "{\"difficulty\":1,\"name\":\"test1\",\"matrix\":[" +
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
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}," +
                "{\"type\":\"schemaCard\",\"body\":{\"difficulty\":2,\"name\":\"test3\",\"matrix\":[[" +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":2}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":\"YELLOW\"}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":4}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":\"RED\"}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}]}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(frontBackSchemaCardWrapper,
                    FrontBackSchemaCardWrapper.toObject((JSONObject) jsonParser.parse(message)).getBackSchemaCard());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void toJsonTest() {
        assertEquals(null, frontBackSchemaCardWrapper.toJSON());
    }
}