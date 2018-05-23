package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.givenToolCard;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;

import java.util.List;

public class PinzaSgrossatriceTest {

    private ToolCard toolCard;
    private List<IToolCardExecutorObserver> observerList;

    @Mock
    private IToolCardExecutorObserver toolCardExecutorObserver1;

    @Mock
    private IToolCardExecutorObserver toolCardExecutorObserver2;

    @Mock
    private IToolCardExecutorObserver toolCardExecutorObserver3;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        toolCard = new ToolCard(Color.PURPLE, "Pinza sgrossatrice", "" +
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Modify dice value by 1][4-Add dice to DraftPool][8-CA]");

    }

    @Test
    public void test() {

    }
}
