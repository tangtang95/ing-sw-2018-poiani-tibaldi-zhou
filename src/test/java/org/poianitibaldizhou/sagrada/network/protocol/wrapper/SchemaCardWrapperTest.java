package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class SchemaCardWrapperTest {

    @DataPoint
    public static SchemaCardWrapper emptySchemaCard;

    @DataPoint
    public static SchemaCardWrapper fullSchemaCardWrapper;

    @DataPoint
    public static SchemaCardWrapper schemaCardWrapperDice;

    @Before
    public void setUp() throws Exception {
        TileWrapper[][] constraints = new TileWrapper[SchemaCardWrapper.NUMBER_OF_ROWS][SchemaCardWrapper.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCardWrapper.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCardWrapper.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new TileWrapper(null);
            }
        }
        emptySchemaCard = new SchemaCardWrapper("test1", 1, constraints);

        constraints[0][2] = new TileWrapper(ColorWrapper.YELLOW.name());
        constraints[1][3] = new TileWrapper("4");
        constraints[2][4] = new TileWrapper(ColorWrapper.RED.name());
        constraints[0][1] = new TileWrapper("2");
        fullSchemaCardWrapper = new SchemaCardWrapper("test3", 2, constraints);
        constraints[0][4].setDice(new DiceWrapper(ColorWrapper.PURPLE,1));
        schemaCardWrapperDice = new SchemaCardWrapper("test2", 2, constraints);

    }

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"schemaCard\",\"body\":{\"difficulty\":1,\"name\":\"test1\",\"matrix\":[" +
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
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}";
        assertTrue(message.equals(emptySchemaCard.toJSON().toJSONString()));
        message = "{\"type\":\"schemaCard\",\"body\":{\"difficulty\":2,\"name\":\"test3\",\"matrix\":[" +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
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
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}";
        assertTrue(message.equals(fullSchemaCardWrapper.toJSON().toJSONString()));
        message = "{\"type\":\"schemaCard\",\"body\":{\"difficulty\":2,\"name\":\"test3\",\"matrix\":[" +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":2}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":\"YELLOW\"}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null,\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":1}}}}]," +
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
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}";
        System.out.println(message);
        System.out.println(schemaCardWrapperDice.toJSON());
        assertTrue(message.equals(schemaCardWrapperDice.toJSON().toJSONString()));
    }

    @Test
    public void toObjectTest() {

    }


}