package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.IllegalNumberOfTokensOnToolCardException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.FavorToken;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import javax.tools.Tool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class FavorTokenTest {
    @Mock
    private ToolCard toolCard;


    private ICoin favorToken;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        favorToken = null;
        toolCard = null;
    }

    /**
     * Test use method when numberOfFavorToken greater than toolCardCost
     */
    @Test
    public void useTestWithFavorToken() throws Exception {
        int toolCardCost = 1;
        int numberOfFavorToken = 3;
        when(toolCard.getCost()).thenReturn(toolCardCost);
        favorToken = new FavorToken(numberOfFavorToken);
        favorToken.use(toolCard);
        assertEquals(numberOfFavorToken - toolCardCost, favorToken.getCoins());
    }

    /**
     * Test use method when numberOfFavorToken less than toolCardCost
     */
    @Test
    public void useTestWithoutFavorToken() throws Exception {
        int toolCardCost = 4;
        int numberOfFavorToken = 2;
        when(toolCard.getCost()).thenReturn(toolCardCost);
        favorToken = new FavorToken(numberOfFavorToken);
        try {
            favorToken.use(toolCard);
            fail("exception expected");
        } catch (NoCoinsExpendableException e){
            assertEquals(numberOfFavorToken , favorToken.getCoins());
        }
    }
}
