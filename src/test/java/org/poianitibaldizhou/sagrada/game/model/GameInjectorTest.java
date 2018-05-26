package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.WrongCardInJsonFileException;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;

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
    public void toolCardInjectorTest() throws Exception {
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1." +
                        " Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][8-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Pennello per Eglomise",
                "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore." +
                        " Devi rispettare tutte le altre restrizioni di piazzamento"
                ,"[1-Remove dice][2-Place old dice ignoring color constraints][4-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.RED, "Alesatore per lamina di rame",
                "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore." +
                        " Devi rispettare tutte le altre restrizioni di piazzamento"
                ,"[1-Remove dice][2-Place old dice ignoring number constraints][4-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.YELLOW, "Lathekin",
                "Muovi esattamente due dadi, rispettando tutte le restrizioni di piazzamento",
                "[1-Remove dice][2-Place old dice][4-CA][8-Remove dice][16-Place old dice][32-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.GREEN, "Taglierina circolare",
                "Dopo aver scelto un dado, scambia quel dado con un dado sul Tracciato dei round",
                "[1-Choose dice][2-Swap dice with RoundTrack][4-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Pennello per Pasta Salda",
                "Dopo aver scelto un dado, tira nuovamente quel dado. Se non puoi piazzarlo," +
                        " riponilo nella riserva"
                ,"[1-Choose dice][2-Reroll dice][4-If Dice placeable][8-Place new dice][9-Add dice to DraftPool][16-CA][18-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Martelletto",
                "Tira nuovamente tutti i dadi della riserva. Questa carta può essere usata solo durante" +
                        " il tuo secondo turno, prima di scegliere il secondo dado"
                ,"[1-Check second turn][2-Check before choose dice][4-Reroll DraftPool][8-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.RED, "Tenaglia a Rotelle",
                "Dopo il tuo primo turno scegli immediatamente un altro dado. Salta il tuo secondo " +
                        "turno in questo round"
                ,"[1-Check first turn][2-Wait turn end][4-Choose dice][8-Place old dice][16-Skip second turn][32-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.YELLOW, "Riga in Sughero",
                "Dopo aver scelto un dado, piazzalo in una casella che non sia adiacente " +
                        "a un altro dado. Devi rispettare tutte le restrizioni di piazzamento",
                "[1-Choose dice][2-Place isolated new dice][4-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.GREEN, "Tampone Diamantato",
                "Dopo aver scelto un dado, giralo sulla faccia opposta. 6 diventa 1, 5 diventa 2," +
                        " 4 diventa 3, ecc.",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Pour over dice][8-Place new dice][16-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Diluente per Pasta Salda",
                "Dopo aver scelto un dado riponilo nel Sacchetto, poi pescane uno dal Sacchetto." +
                        " Scegli il valore del nuovo dado e piazzalo, rispettando tutte le restrizioni di piazzamento",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Add dice to Dicebag][8-Draw dice from Dicebag][16-Modify dice value][32-Place new dice][64-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Taglierina Manuale",
                "Muovi fino a due dadi dello stesso colore di un solo dado sul Tracciato dei Round. " +
                        "Devi rispettare tutte le restrizioni di piazzamento",
                "[1-Choose color from RoundTrack][2-Remove dice of a certain color][4-Place old dice][8-If continue][9-CA][16-Remove dice of a certain color][32-Place old dice][64-CA]"
        ));

        GameInjector.injectToolCards(toolCardDrawableCollection1);
        for (int i = 0; i < toolCardDrawableCollection.size(); i++) {
            assertEquals("Wrong drawableCollection of toolCard: "
                            + toolCardDrawableCollection.getCollection().get(i).getName(),
                    toolCardDrawableCollection.getCollection().get(i),
                    toolCardDrawableCollection1.getCollection().get(i));
        }
        assertEquals("Wrong size", 12, toolCardDrawableCollection.size());
    }

    @Test
    public void privateObjectiveCardInjectorTest() throws Exception {
        GameInjector.injectPrivateObjectiveCard(privateObjectiveCardDrawableCollection);
        assertEquals("Wrong size", 5, privateObjectiveCardDrawableCollection.size());
    }

    @Test
    public void diceBagInjectorTest() throws Exception {
        GameInjector.injectDiceBag(diceDrawableCollection);
        assertEquals("Wrong size", 90, diceDrawableCollection.size());
    }

    @Test
    public void PublicObjectiveCardInjectorTest() throws Exception {
        GameInjector.injectPublicObjectiveCards(publicObjectiveCardDrawableCollection);
        assertEquals("Wrong size", 10, publicObjectiveCardDrawableCollection.size());
    }

    @Test
    public void schemaCardInjectorTest() {
        // TODO
    }

    @Test
    public void NotNullAnnotationParameterTest() throws Exception{
        DrawableCollection<Dice> diceBag = null;
        try {
            GameInjector.injectDiceBag(diceBag);
            fail("exception expected");
        } catch (IllegalArgumentException e){
            assertNotEquals(null, e);
            //e.printStackTrace();
        }
    }
}
