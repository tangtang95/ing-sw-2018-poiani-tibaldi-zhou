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
    public static ToolCard toolCard1;
    @DataPoint
    public static ExpendableDice expendableDice;

    @BeforeClass
    public static void setUpClass() throws EmptyCollectionException {
        GameInjector gameInjector = new GameInjector();
        DrawableCollection<ToolCard> toolCardDrawableCollection = new DrawableCollection<>();
        DrawableCollection<Dice> diceBag = new DrawableCollection<>();
        draftPool = new DraftPool();

        gameInjector.injectToolCards(toolCardDrawableCollection, false);
        toolCard1 = toolCardDrawableCollection.draw();

        gameInjector.injectDiceBag(diceBag);
        List<Dice> dices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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
        try {
            expendableDice.use(toolCard1);
        } catch (NoCoinsExpendableException e) {
            assertEquals("DraftPool size error", 10, draftPool.getDices().size());
        }

        assertEquals("DraftPool size error", 9, draftPool.getDices().size());
    }
}
