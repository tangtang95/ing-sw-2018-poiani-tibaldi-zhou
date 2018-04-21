package org.poianitibaldizhou.sagrada.model;

import org.junit.Test;
import org.poianitibaldizhou.sagrada.game.model.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConstraintTest {

    @Test
    public void testConstraintGetValue(){
        ColorConstraint c1 = new ColorConstraint(Color.PURPLE);
        assertEquals(Color.PURPLE, c1.getColor());
        assertEquals(4, c1.getIndexValue());

        NumberConstraint c2 = new NumberConstraint(3);
        assertEquals(3, c2.getNumber());
        assertEquals(3, c2.getIndexValue() + 1);
    }

    @Test
    public void testMatches(){
        IConstraint c1 = new ColorConstraint(Color.PURPLE);
        IConstraint c2 = new NumberConstraint(3);
        IConstraint c3 = new ColorConstraint(Color.BLUE);
        IConstraint c4 = new ColorConstraint(Color.PURPLE);
        IConstraint c5 = new NoConstraint();

        assertFalse(c1.matches(c3));
        assertTrue(c1.matches(c2));
        assertTrue(c1.matches(c4));
        assertTrue(c5.matches(c2));
        assertTrue(c1.matches(c5));
    }
}
