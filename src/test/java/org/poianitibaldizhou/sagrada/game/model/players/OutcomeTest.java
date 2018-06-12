package org.poianitibaldizhou.sagrada.game.model.players;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OutcomeTest {

    @Test
    public void toJSONtest() {
        Outcome outcome = Outcome.WIN;
        String message = outcome.toJSON().toJSONString();
        String test = "{\"type\":\"outcome\",\"body\":\"WIN\"}";
        assertEquals(message,test);

        outcome = Outcome.LOSE;
        message = outcome.toJSON().toJSONString();
        test = "{\"type\":\"outcome\",\"body\":\"LOSE\"}";
        assertEquals(message,test);

        outcome = Outcome.IN_GAME;
        message = outcome.toJSON().toJSONString();
        test = "{\"type\":\"outcome\",\"body\":\"IN_GAME\"}";
        assertEquals(message,test);

    }
}
