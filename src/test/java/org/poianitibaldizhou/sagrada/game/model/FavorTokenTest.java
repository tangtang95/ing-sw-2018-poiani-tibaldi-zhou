package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.coin.FavorToken;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ICoin;

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
     * Test when numberOfFavorToken > toolCardCost
     */
    @Test
    public void isCardUsableTrueTest() throws Exception {
        int toolCardCost = 1;
        int numberOfFavorToken = 3;
        when(toolCard.getCost()).thenReturn(toolCardCost);
        favorToken = new FavorToken(numberOfFavorToken);
        assertEquals(true, favorToken.isCardUsable(toolCard));
    }

    /**
     * Test when numberOfFavorToken < toolCardCost
     */
    @Test
    public void isCardUsableFalseTest() throws Exception {
        int toolCardCost = 4;
        int numberOfFavorToken = 2;
        when(toolCard.getCost()).thenReturn(toolCardCost);
        favorToken = new FavorToken(numberOfFavorToken);
        assertEquals(false, favorToken.isCardUsable(toolCard));
    }

    @Test
    public void getCoins() throws Exception {
        int numberOfFavorToken = 3;
        favorToken = new FavorToken(numberOfFavorToken);
        assertEquals(numberOfFavorToken, favorToken.getCoins());
    }

    /**
     * Test when numberOfFavorToken equals cost
     */
    @Test
    public void removeCoinsSucceed() throws Exception {
        int numberOfFavorToken = 3;
        int cost = 3;
        favorToken = new FavorToken(numberOfFavorToken);
        favorToken.removeCoins(cost);
        assertEquals(numberOfFavorToken - cost, favorToken.getCoins());
    }

    /**
     * Test when numberOfFavorToken less than cost
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeCoinsFailed() throws Exception {
        int numberOfFavorToken = 3;
        int cost = 4;
        favorToken = new FavorToken(numberOfFavorToken);
        favorToken.removeCoins(cost);
    }
}
