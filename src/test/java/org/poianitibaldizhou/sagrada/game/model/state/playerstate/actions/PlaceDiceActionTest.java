package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class PlaceDiceActionTest {

    @DataPoint
    public static PlaceDiceAction placeDiceAction;

    @Before
    public void setUp() throws Exception {
        placeDiceAction = new PlaceDiceAction();
    }

    @Test
    public void toJSON() {
        assertNull(placeDiceAction.toJSON());
    }

    @Test
    public void toObject() {
        PlaceDiceAction.toObject();
    }
}