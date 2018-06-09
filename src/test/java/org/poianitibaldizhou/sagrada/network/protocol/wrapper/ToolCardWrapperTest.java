package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class ToolCardWrapperTest {

    @DataPoint
    public static ToolCardWrapper toolCardWrapper;

    @Before
    public void setUp() throws Exception {
        toolCardWrapper = new ToolCardWrapper("test", "test", ColorWrapper.BLUE, 1);
    }

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"toolCard\",\"body\":{\"name\":\"test\",\"token\":1}}";
        assertEquals(message, toolCardWrapper.toJSON().toJSONString());
    }

    @Test
    public void toObjectTest() {
        String message = "{\"cost\":1,\"color\":\"BLUE\",\"name\":\"test\",\"description\":\"test\",\"token\":1}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(toolCardWrapper, ToolCardWrapper.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}