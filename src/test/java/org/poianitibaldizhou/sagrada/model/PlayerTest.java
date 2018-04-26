package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlayerTest {
    @DataPoint
    public static final String token1 = "1234567890";

    @DataPoint
    public static final String token2= "0987654321";

    @DataPoint
    public static ToolCard toolCard1,toolCard2;

    @DataPoint
    public static Player player1, player2;

    @DataPoint
    public static SchemaCard schemaCard;

    @DataPoint
    public static DraftPool draftPool;

    @BeforeClass
    public static void setUpClass() throws EmptyCollectionException {
        GameInjector gameInjector = new GameInjector();
        DrawableCollection<ToolCard> toolCardDrawableCollection = new DrawableCollection<>();
        DrawableCollection<SchemaCard> schemaCardDrawableCollection = new DrawableCollection<>();
        DrawableCollection<Dice> diceBag = new DrawableCollection<>();
        DraftPool draftPool = new DraftPool();

        gameInjector.injectToolCards(toolCardDrawableCollection,false);
        toolCard1 = toolCardDrawableCollection.draw();
        player1 = new Player(token1, new FavorToken());

        gameInjector.injectToolCards(toolCardDrawableCollection,true);
        toolCard2 = toolCardDrawableCollection.draw();
        player2 = new Player(token2, new ExpendableDice(draftPool));

        gameInjector.injectSchemaCards(schemaCardDrawableCollection);
        schemaCard = schemaCardDrawableCollection.draw();

        gameInjector.injectDiceBag(diceBag);
        List<Dice> dices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dices.add(diceBag.draw());
        }
        draftPool.addDices(dices);
    }

    @AfterClass
    public static void tearDownClass(){

    }

    @Before
    public void setUp(){
        player1.setSchemaCard(schemaCard);
        player2.setSchemaCard(schemaCard);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void getCoinTest() {
        assertEquals("Wrong number of Favor Tokens", schemaCard.getDifficulty(),player1.getCoins());
        assertEquals("Wrong number of dice",10,player2.getCoins());
    }
}
