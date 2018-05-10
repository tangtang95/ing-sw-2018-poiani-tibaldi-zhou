package org.poianitibaldizhou.sagrada.game.model;

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
        DrawableCollection<ToolCard> toolCardDrawableCollection = new DrawableCollection<>();
        DrawableCollection<SchemaCard> schemaCardDrawableCollection = new DrawableCollection<>();
        DrawableCollection<Dice> diceBag = new DrawableCollection<>();
        DraftPool draftPool = new DraftPool();
        // TODO
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
    public void getCoinTest() {

    }
}
