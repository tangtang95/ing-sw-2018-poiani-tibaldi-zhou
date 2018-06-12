package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class EndTurnActionTest {

    @DataPoint
    public static EndTurnAction endTurnAction, endTurnAction1;

    @Before
    public void setUp() throws Exception {
        endTurnAction = new EndTurnAction();
        endTurnAction1 = new EndTurnAction();
    }

    @Test
    public void equals() {
        assertTrue(endTurnAction.equals(endTurnAction1));
    }

    @Test
    public void toJSON() {
        assertNull(endTurnAction.toJSON());
    }

    @Test
    public void toObject() {
        EndTurnAction.toObject();
    }
}