package org.poianitibaldizhou.sagrada.game.model.players;

import org.junit.Test;

import static org.junit.Assert.*;

public class OutcomeTest {

    @Test
    public void toJSON() {
        Outcome outcome = Outcome.WIN;
        String message = "{\"type\":\"outcome\",\"body\":\"WIN\"}";
        assertEquals(outcome.toJSON().toJSONString(), message);
    }
}