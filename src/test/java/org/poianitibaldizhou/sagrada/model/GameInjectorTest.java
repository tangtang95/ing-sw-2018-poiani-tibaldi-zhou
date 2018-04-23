package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.cards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import static org.junit.Assert.assertEquals;

public class GameInjectorTest {
    @DataPoint
    public static DrawableCollection<SchemaCard> schemaCardDrawableCollection;
    @DataPoint
    public static DrawableCollection<ToolCard> toolCardDrawableCollection;
    @DataPoint
    public static DrawableCollection<PrivateObjectiveCard> privateObjectiveCardDrawableCollection;
    @DataPoint
    public static DrawableCollection<Dice> diceDrawableCollection;
    @DataPoint
    private static GameInjector injector;

    @BeforeClass
    public static void setUpClass() {
        injector = new GameInjector();
    }

    @AfterClass
    public static void tearDownClass(){
        schemaCardDrawableCollection = null;
    }

    @Before
    public void setUp(){
        schemaCardDrawableCollection = null;
        toolCardDrawableCollection = null;
        privateObjectiveCardDrawableCollection = null;
        diceDrawableCollection = null;
    }

    @After
    public void tearDown() {

    }

    @Test
    public void toolCardInjectorTest(){
        toolCardDrawableCollection = injector.injectToolCards(toolCardDrawableCollection,true);
        assertEquals("Wrong size",12,toolCardDrawableCollection.size());
    }

    @Test
    public void privateObjectiveCardInjectorTest(){
        privateObjectiveCardDrawableCollection = injector.injectPrivateObjectiveCard(privateObjectiveCardDrawableCollection);
        assertEquals("Wrong size",5,privateObjectiveCardDrawableCollection.size());
    }

    @Test
    public void diceBagInjectorTest(){
        diceDrawableCollection = injector.injectDiceBag(diceDrawableCollection);
        assertEquals("Wrong size",90,diceDrawableCollection.size());
    }

    @Test
    public void schemaCardInjectorTest(){
        schemaCardDrawableCollection = injector.injectSchemaCards(schemaCardDrawableCollection);
        assertEquals("Wrong size",24,schemaCardDrawableCollection.size());
    }
}
