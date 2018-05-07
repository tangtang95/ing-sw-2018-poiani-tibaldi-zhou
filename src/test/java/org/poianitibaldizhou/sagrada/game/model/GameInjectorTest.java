package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.WrongCardInJsonFileException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.cards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GameInjectorTest {
    @DataPoint
    public static DrawableCollection<SchemaCard> schemaCardDrawableCollection;
    @DataPoint
    public static DrawableCollection<ToolCard> toolCardDrawableCollection, toolCardDrawableCollection1;
    @DataPoint
    public static DrawableCollection<PrivateObjectiveCard> privateObjectiveCardDrawableCollection;
    @DataPoint
    public static DrawableCollection<Dice> diceDrawableCollection;
    @DataPoint
    public static DrawableCollection<PublicObjectiveCard> publicObjectiveCardDrawableCollection;
    @DataPoint
    private static GameInjector injector;

    @BeforeClass
    public static void setUpClass() {
        injector = new GameInjector();
        schemaCardDrawableCollection = new DrawableCollection<>();
        toolCardDrawableCollection = new DrawableCollection<>();
        toolCardDrawableCollection1 = new DrawableCollection<>();
        privateObjectiveCardDrawableCollection = new DrawableCollection<>();
        diceDrawableCollection = new DrawableCollection<>();
        publicObjectiveCardDrawableCollection = new DrawableCollection<>();

        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1." +
                        " Non puoi cambiare un 6 in 1 o un 1 in 6","",true
                ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Pennello per Eglomise",
                "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore." +
                        " Devi rispettare tutte le altre restrizioni di piazzamento"
                ,"",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.RED, "Alesatore per lamina di rame",
                "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore." +
                        " Devi rispettare tutte le altre restrizioni di piazzamento"
                ,"",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.YELLOW, "Lathekin",
                "Muovi esattamente due dadi, rispettando tutte le restrizioni di piazzamento",
                "",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.GREEN, "Taglierina circolare",
                "Dopo aver scelto un dado, scambia quel dado con un dado sul Tracciato dei round",
                "",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Pennello per Pasta Salda",
                "Dopo aver scelto un dado, tira nuovamente quel dado. Se non puoi piazzarlo," +
                        " riponilo nella riserva"
                ,"",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Martelletto",
                "Tira nuovamente tutti i dadi della riserva. Questa carta può essere usata solo durante" +
                        " il tuo secondo turno, prima di scegliere il secondo dado"
                ,"",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.RED, "Tenaglia a Rotelle",
                "Dopo il tuo primo turno scegli immediatamente un altro dado. Salta il tuo secondo " +
                        "turno in questo round"
                ,"",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.YELLOW, "Riga in Sughero",
                "Dopo aver scelto un dado, piazzalo in una casella che non sia adiacente " +
                        "a un altro dado. Devi rispettare tutte le restrizioni di piazzamento",
                "",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.GREEN, "Tampone Diamantato",
                "Dopo aver scelto un dado, giralo sulla faccia opposta. 6 diventa 1, 5 diventa 2," +
                        " 4 diventa 3, ecc.",
                "",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Diluente per Pasta Salda",
                "Dopo aver scelto un dado riponilo nel Sacchetto, poi pescane uno dal Sacchetto." +
                        " Scegli il valore del nuovo dado e piazzalo, rispettando tutte le restrizioni di piazzamento",
                "",true
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Taglierina Manuale",
                "Muovi fino a due dadi dello stesso colore di un solo dado sul Tracciato dei Round. " +
                        "Devi rispettare tutte le restrizioni di piazzamento",
                "",true
        ));
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
        injector.injectToolCards(toolCardDrawableCollection1, true);
        for (int i = 0; i < toolCardDrawableCollection.size(); i++) {
            assertEquals("Wrong drawableCollection of toolCards",
                    toolCardDrawableCollection.getCollection().get(i),
                    toolCardDrawableCollection1.getCollection().get(i));
        }
        assertEquals("Wrong size", 12, toolCardDrawableCollection.size());
    }

    @Test
    public void privateObjectiveCardInjectorTest() {
        injector.injectPrivateObjectiveCard(privateObjectiveCardDrawableCollection);
        assertEquals("Wrong size", 5, privateObjectiveCardDrawableCollection.size());
    }

    @Test
    public void diceBagInjectorTest() {
        injector.injectDiceBag(diceDrawableCollection);
        assertEquals("Wrong size", 90, diceDrawableCollection.size());
    }

    @Test
    public void PublicObjectiveCardInjectorTest() throws WrongCardInJsonFileException {
        injector.injectPublicObjectiveCards(publicObjectiveCardDrawableCollection);
        assertEquals("Wrong size", 10, publicObjectiveCardDrawableCollection.size());
    }

    @Test
    public void schemaCardInjectorTest() {
        injector.injectSchemaCards(schemaCardDrawableCollection);
        assertEquals("Wrong size", 24, schemaCardDrawableCollection.size());
    }
}
