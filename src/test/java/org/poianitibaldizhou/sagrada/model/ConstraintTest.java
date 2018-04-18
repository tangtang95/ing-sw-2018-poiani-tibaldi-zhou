package org.poianitibaldizhou.sagrada.model;

import org.junit.Test;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.NumberConstraint;

import static org.junit.Assert.assertEquals;

public class ConstraintTest {

    @Test
    public void testConstraintGetValue(){
        ColorConstraint c1 = new ColorConstraint(Color.PURPLE);
        assertEquals(Color.PURPLE, c1.getColor());
        assertEquals(4, c1.getValue());

        NumberConstraint c2 = new NumberConstraint(3);
        assertEquals(3, c2.getNumber());
        assertEquals(3, c2.getValue() + 1);

    }
}
