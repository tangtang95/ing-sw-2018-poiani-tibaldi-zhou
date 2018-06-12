package org.poianitibaldizhou.sagrada.game.model.cards;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(value = Enclosed.class)
public class PositionTest {

    @RunWith(value = Parameterized.class)
    public static class ParameterizedPart {

        Position position1;
        Position position2;
        public boolean expected;

        @Parameterized.Parameters
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {true, new Position(1,1), new Position(1,1)},
                    {true, new Position(1,2), new Position(1,2)},
                    {true, new Position(0,1), new Position(0,1)},
                    {false, new Position(1,1), new Position(2,1)},
                    {false, new Position(3,4), new Position(2,1)},
                    {false, new Position(3,4), new Position(3,0)},
            });
        }

        public ParameterizedPart(boolean expected, Position p1, Position p2) {
            this.expected = expected;
            this.position1 = p1;
            this.position2 = p2;
        }

        @Test
        public void testEquals() {
            assertEquals(expected, position1.equals(position2));
        }
    }

    public static class NonParametrizedPart {
        @Test(expected = Exception.class)
        public void testFailConstructor() {
            Position position = new Position(-1, 5);
        }

        @Test
        public void testFailEquals(){
            assertNotEquals(new Position(2,2), new ArrayList<>());
        }

        @Test
        public void testToJSON(){
            Position position = new Position(2,4);
            String message = "{\"type\":\"position\",\"body\":{\"column\":4,\"row\":2}}";
            assertTrue(message.equals(position.toJSON().toJSONString()));
        }

        @Test
        public void testSubtract() {
            Position position = new Position(2, 4);
            assertEquals(new Position(1, 1), position.subtract(1, 3));
        }

        @Test
        public void testAdd() {
            Position position = new Position(2, 3);
            assertEquals(new Position(3,3), position.add(1,0));
        }

        @Test
        public void testToString() {
            String positionString = "Position: (2, 4)";
            assertEquals(positionString, new Position(2, 4).toString());
        }

        @Test
        public void testHashCode() {
            assertEquals(new Position(1, 3).hashCode(), new Position(1, 3).hashCode());
            assertEquals(new Position(3, 3).hashCode(), new Position(3, 3).hashCode());
            assertNotEquals(new Position(3, 1).hashCode(), new Position(1, 3).hashCode());
        }

        @Test
        public void testToObject(){
            Position position = new Position(2,4);
            String message = "{\"column\":4,\"row\":2}";
            org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
            try {
                assertTrue((position.toObject((JSONObject) jsonParser.parse(message))).equals(position));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
