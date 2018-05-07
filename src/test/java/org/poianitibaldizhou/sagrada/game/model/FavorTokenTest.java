package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.IllegalNumberOfTokensOnToolCardException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.FavorToken;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import static org.junit.Assert.assertEquals;

public class FavorTokenTest {
    @DataPoint
    public static FavorToken favorToken;
    @DataPoint
    public static ToolCard toolCard;

    @BeforeClass
    public static void setUpClass() throws EmptyCollectionException {
        DrawableCollection<ToolCard> toolCardDrawableCollection = new DrawableCollection<>();
        GameInjector injector = new GameInjector();

        injector.injectToolCards(toolCardDrawableCollection, true);

        toolCard = toolCardDrawableCollection.draw();
        favorToken = new FavorToken(6);

    }

    @AfterClass
    public static void tearDownClass(){

    }

    @Before
    public void setUp(){
    }

    @After
    public void tearDown() {
    }

    @Test
    public void useTest() throws IllegalNumberOfTokensOnToolCardException, NoCoinsExpendableException {
        int temp = favorToken.getCoins();
        favorToken.use(toolCard);
        assertEquals("Wrong number of coins", temp - toolCard.getCost(), favorToken.getCoins());
        for (int i = 0; i < 6; i++) {
            try {
                favorToken.use(toolCard);
            } catch (NoCoinsExpendableException e) {
                assertEquals("Wrong number of coins", 0, favorToken.getCoins());
            }
        }
    }
}
