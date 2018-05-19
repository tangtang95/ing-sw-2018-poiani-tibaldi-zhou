package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.givenToolCard;

import org.junit.Before;
import org.junit.Test;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

public class PinzaSgrossatriceTest {

    private ToolCard toolCard;

    @Before
    public void setUp() throws Exception {
        toolCard = new ToolCard(Color.PURPLE, "Pinza sgrossatrice", "" +
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Modify dice value by 1][4-Add dice to DraftPool][8-CA]",
                false);
    }

    @Test
    public void name() {
    }
}
