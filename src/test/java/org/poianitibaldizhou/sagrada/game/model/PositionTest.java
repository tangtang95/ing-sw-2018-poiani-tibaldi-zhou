package org.poianitibaldizhou.sagrada.game.model;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(value = Enclosed.class)
public class PositionTest {

    @RunWith(value = Parameterized.class)
    public static class ParameterizedPart {

        Position position1;
        Position position2;
        public boolean expected;

        @Parameterized.Parameters
        public static Collection<Object[]> data() throws IllegalAccessException {
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
        public void testEquals() throws Exception {
            assertEquals(expected, position1.equals(position2));
        }
    }

    public static class NonParametrizedPart {
        @Test(expected = Exception.class)
        public void testFailConstructor() throws Exception {
            Position position = new Position(-1, 5);
        }

        @Test
        public void testFailEquals() throws Exception {
            assertNotEquals(new Position(2,2), new ArrayList<>());
        }
    }
}
