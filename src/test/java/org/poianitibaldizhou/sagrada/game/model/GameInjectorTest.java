package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.WrongCardInJsonFileException;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

/**
 * Dependency class with:
 * - SchemaCard -> Tile -> Constraints
 * - ToolCard
 * - PrivateObjectiveCard -> Card
 * - Dice -> Constraints
 * - PublicObjectiveCard -> Card
 * - DrawableCollection
 */
public class GameInjectorTest {
    @DataPoint
    private static DrawableCollection<SchemaCard> schemaCardDrawableCollection;
    @DataPoint
    private static DrawableCollection<ToolCard> toolCardDrawableCollection, toolCardDrawableCollection1;
    @DataPoint
    private static DrawableCollection<PrivateObjectiveCard> privateObjectiveCardDrawableCollection;
    @DataPoint
    private static DrawableCollection<Dice> diceDrawableCollection;
    @DataPoint
    private static DrawableCollection<PublicObjectiveCard> publicObjectiveCardDrawableCollection;

    @BeforeClass
    public static void setUpClass() {
        schemaCardDrawableCollection = new DrawableCollection<>();
        toolCardDrawableCollection = new DrawableCollection<>();
        toolCardDrawableCollection1 = new DrawableCollection<>();
        privateObjectiveCardDrawableCollection = new DrawableCollection<>();
        diceDrawableCollection = new DrawableCollection<>();
        publicObjectiveCardDrawableCollection = new DrawableCollection<>();
    }

    @AfterClass
    public static void tearDownClass() {
        schemaCardDrawableCollection = null;
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void toolCardInjectorTest() {
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1." +
                        " Non puoi cambiare un 6 in 1 o un 1 in 6",
                "Choose dice;Modify dice value by 1;Add dice to DraftPool;CA",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Pennello per Eglomise",
                "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore." +
                        " Devi rispettare tutte le altre restrizioni di piazzamento"
                ,"Remove dice;Place dice ignoring color constraints;CA",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.RED, "Alesatore per lamina di rame",
                "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore." +
                        " Devi rispettare tutte le altre restrizioni di piazzamento"
                ,"Choose dice;Remove dice;Place dice ignoring number constraints",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.YELLOW, "Lathekin",
                "Muovi esattamente due dadi, rispettando tutte le restrizioni di piazzamento",
                "Choose dice;Remove dice;Place dice;Choose dice;Remove dice;Place dice",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.GREEN, "Taglierina circolare",
                "Dopo aver scelto un dado, scambia quel dado con un dado sul Tracciato dei round",
                "Choose dice;Swap dice with RoundTrack",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Pennello per Pasta Salda",
                "Dopo aver scelto un dado, tira nuovamente quel dado. Se non puoi piazzarlo," +
                        " riponilo nella riserva"
                ,"Choose dice;Reroll dice;Check Dice placeble;Add dice to DraftPool",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Martelletto",
                "Tira nuovamente tutti i dadi della riserva. Questa carta può essere usata solo durante" +
                        " il tuo secondo turno, prima di scegliere il secondo dado"
                ,"Check second turn;Check before choose dice;Reroll DraftPool",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.RED, "Tenaglia a Rotelle",
                "Dopo il tuo primo turno scegli immediatamente un altro dado. Salta il tuo secondo " +
                        "turno in questo round"
                ,"Check first turn;Wait turn end;Choose dice;Place dice;Skip second turn",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.YELLOW, "Riga in Sughero",
                "Dopo aver scelto un dado, piazzalo in una casella che non sia adiacente " +
                        "a un altro dado. Devi rispettare tutte le restrizioni di piazzamento",
                "Choose dice;Place isolated dice",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.GREEN, "Tampone Diamantato",
                "Dopo aver scelto un dado, giralo sulla faccia opposta. 6 diventa 1, 5 diventa 2," +
                        " 4 diventa 3, ecc.",
                "Choose dice;Pour over dice",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Diluente per Pasta Salda",
                "Dopo aver scelto un dado riponilo nel Sacchetto, poi pescane uno dal Sacchetto." +
                        " Scegli il valore del nuovo dado e piazzalo, rispettando tutte le restrizioni di piazzamento",
                "Choose dice;Add dice to Dicebag;Draw dice from Dicebag;Choose dice value;Place dice",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Taglierina Manuale",
                "Muovi fino a due dadi dello stesso colore di un solo dado sul Tracciato dei Round. " +
                        "Devi rispettare tutte le restrizioni di piazzamento",
                "Choose color from RoundTrack;Remove dice of a certain color;Place dice;Remove dice of a certain color;Place dice",true
        ));

        GameInjector.injectToolCards(toolCardDrawableCollection1, true);
        for (int i = 0; i < toolCardDrawableCollection.size(); i++) {
            assertEquals("Wrong drawableCollection of toolCards",
                    toolCardDrawableCollection.getCollection().get(i),
                    toolCardDrawableCollection1.getCollection().get(i));
        }
        assertEquals("Wrong size", 12, toolCardDrawableCollection.size());
    }

    @Test
    public void privateObjectiveCardInjectorTest() {
        GameInjector.injectPrivateObjectiveCard(privateObjectiveCardDrawableCollection);
        assertEquals("Wrong size", 5, privateObjectiveCardDrawableCollection.size());
    }

    @Test
    public void diceBagInjectorTest() {
        GameInjector.injectDiceBag(diceDrawableCollection);
        assertEquals("Wrong size", 90, diceDrawableCollection.size());
    }

    @Test
    public void PublicObjectiveCardInjectorTest() throws WrongCardInJsonFileException {
        GameInjector.injectPublicObjectiveCards(publicObjectiveCardDrawableCollection);
        assertEquals("Wrong size", 10, publicObjectiveCardDrawableCollection.size());
    }

    @Test
    public void schemaCardInjectorTest() {
        GameInjector.injectSchemaCards(schemaCardDrawableCollection);
        assertEquals("Wrong size", 24, schemaCardDrawableCollection.size());
    }

    @Test
    public void NotNullAnnotationParameterTest(){
        DrawableCollection<Dice> diceBag = null;
        try {
            GameInjector.injectDiceBag(diceBag);
            fail("exception expected");
        } catch (IllegalArgumentException e){
            assertNotEquals(null, e);
        }
    }
}
