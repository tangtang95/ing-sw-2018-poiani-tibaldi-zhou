package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class CommandFlowTest {

    @DataPoint
    public static CommandFlow commandFlow;

    @Test
    public void toJsonTest() {
        String message = "";
    }

    @Test
    public void toObjectTest() {

    }

    @Before
    public void setUp() throws Exception {
        commandFlow = CommandFlow.REPEAT;
    }
}