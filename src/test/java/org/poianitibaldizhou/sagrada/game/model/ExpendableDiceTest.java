package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExpendableDiceTest {
    @DataPoint
    public static DraftPool draftPool;
    @DataPoint
    public static DrawableCollection<ToolCard> toolCards;
    @DataPoint
    public static ExpendableDice expendableDice;

    @BeforeClass
    public static void setUpClass() throws EmptyCollectionException {
        GameInjector gameInjector = new GameInjector();
        toolCards = new DrawableCollection<>();
        DrawableCollection<Dice> diceBag = new DrawableCollection<>();
        draftPool = new DraftPool();

        gameInjector.injectToolCards(toolCards, false);

        gameInjector.injectDiceBag(diceBag);
        List<Dice> dices = new ArrayList<>();
        for (int i = 0; i < 90; i++) {
            dices.add(diceBag.draw());
        }
        draftPool.addDices(dices);

        expendableDice = new ExpendableDice(draftPool);
    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void useTest() {
        int i = 90;
        for (ToolCard t : toolCards.getCollection()) {
            try {
                expendableDice.use(t);
            } catch (NoCoinsExpendableException e) {
                assertEquals("DraftPool size error", i, draftPool.getDices().size());
            }
            i--;
            assertEquals("DraftPool size error", i, draftPool.getDices().size());
        }
    }
}
