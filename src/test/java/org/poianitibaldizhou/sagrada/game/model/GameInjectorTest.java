package org.poianitibaldizhou.sagrada.game.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

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
    private static DrawableCollection<FrontBackSchemaCard> schemaCardDrawableCollection;
    @DataPoint
    private static DrawableCollection<ToolCard> toolCardDrawableCollection, toolCardDrawableCollection1;
    @DataPoint
    private static DrawableCollection<PrivateObjectiveCard> privateObjectiveCardDrawableCollection,
            privateObjectiveCardDrawableCollection1;
    @DataPoint
    private static DrawableCollection<Dice> diceDrawableCollection;
    @DataPoint
    private static DrawableCollection<PublicObjectiveCard> publicObjectiveCardDrawableCollection,
            publicObjectiveCardDrawableCollection1;

    @BeforeClass
    public static void setUpClass() {
        schemaCardDrawableCollection = new DrawableCollection<>();

        toolCardDrawableCollection = new DrawableCollection<>();
        toolCardDrawableCollection1 = new DrawableCollection<>();

        privateObjectiveCardDrawableCollection = new DrawableCollection<>();
        privateObjectiveCardDrawableCollection1 = new DrawableCollection<>();

        diceDrawableCollection = new DrawableCollection<>();

        publicObjectiveCardDrawableCollection = new DrawableCollection<>();
        publicObjectiveCardDrawableCollection1 = new DrawableCollection<>();
    }

    @AfterClass
    public static void tearDownClass() {
        schemaCardDrawableCollection = null;
    }

    @Test
    public void toolCardInjectorTest() {
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1." +
                        " Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Pennello per Eglomise",
                "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore." +
                        " Devi rispettare tutte le altre restrizioni di piazzamento"
                ,"[1-Remove dice][2-CP][4-Place old dice ignoring color constraints][8-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.RED, "Alesatore per lamina di rame",
                "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore." +
                        " Devi rispettare tutte le altre restrizioni di piazzamento"
                ,"[1-Remove dice][2-CP][4-Place old dice ignoring number constraints][8-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.YELLOW, "Lathekin",
                "Muovi esattamente due dadi, rispettando tutte le restrizioni di piazzamento",
                "[1-Remove dice][2-CP][4-Place old dice][8-CA][16-Remove dice][32-CP][64-Place old dice][128-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.GREEN, "Taglierina circolare",
                "Dopo aver scelto un dado, scambia quel dado con un dado sul Tracciato dei round",
                "[1-Choose dice][2-Swap dice with RoundTrack][4-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.PURPLE, "Pennello per Pasta Salda",
                "Dopo aver scelto un dado, tira nuovamente quel dado. Se non puoi piazzarlo," +
                        " riponilo nella riserva"
                ,"[1-Choose dice][2-Remove dice from DraftPool][4-Reroll dice][8-If Dice placeable][16-Place new dice][17-Add dice to DraftPool][32-CA][34-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.BLUE, "Martelletto",
                "Tira nuovamente tutti i dadi della riserva. Questa carta può essere usata solo durante" +
                        " il tuo secondo turno, prima di scegliere il secondo dado"
                ,"[1-Check second turn][2-Check before choose dice][4-Reroll DraftPool][8-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.RED, "Tenaglia a Rotelle",
                "Dopo il tuo primo turno scegli immediatamente un altro dado. Salta il tuo secondo " +
                        "turno in questo round"
                ,"[1-Check first turn][2-Wait turn end][4-Choose dice][8-Place old dice][16-Remove dice from DraftPool][32-Skip second turn][64-CA]"
        ));
        toolCardDrawableCollection.addElement(new ToolCard(Color.YELLOW, "Riga in Sughero",
                "Dopo aver scelto un dado, piazzalo in una casella che non sia adiacente " +
                        "a un altro dado. Devi rispettare tutte le restrizioni di piazzamento",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Place isolated new dice][8-CA]"
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
                "[1-Choose color from RoundTrack][2-Remove dice of a certain color][4-CP][8-Place old dice][16-If continue][32-CP][64-CD][33-CA][128-Remove dice of a certain color][256-CP][512-Place old dice][1024-CA]"
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
    public void privateObjectiveCardInjectorTest() {
        privateObjectiveCardDrawableCollection.addElement(new PrivateObjectiveCard("Sfumature Rosse - Privata",
                "Somma dei valori su tutti i dadi rossi",
                Color.RED));
        privateObjectiveCardDrawableCollection.addElement(new PrivateObjectiveCard("Sfumature Gialle - Privata",
                "Somma dei valori su tutti i dadi gialli",
                Color.YELLOW));
        privateObjectiveCardDrawableCollection.addElement(new PrivateObjectiveCard("Sfumature Verdi - Privata",
                "Somma dei valori su tutti i dadi verdi",
                Color.GREEN));
        privateObjectiveCardDrawableCollection.addElement(new PrivateObjectiveCard("Sfumature Blu - Privata",
                "Somma dei valori su tutti i dadi blu",
                Color.BLUE));
        privateObjectiveCardDrawableCollection.addElement(new PrivateObjectiveCard("Sfumature Viola - Privata",
                "Somma dei valori su tutti i dadi viola",
                Color.PURPLE));

        GameInjector.injectPrivateObjectiveCard(privateObjectiveCardDrawableCollection1);
        for (int i = 0; i < privateObjectiveCardDrawableCollection.size(); i++) {
            assertTrue(privateObjectiveCardDrawableCollection1.getCollection().
                    containsAll(privateObjectiveCardDrawableCollection.getCollection()));
        }
        assertEquals("Wrong size", 5, privateObjectiveCardDrawableCollection.size());
    }

    @Test
    public void diceBagInjectorTest() {
        GameInjector.injectDiceBag(diceDrawableCollection);
        assertEquals("Wrong size", 90, diceDrawableCollection.size());
    }

    @Test
    public void PublicObjectiveCardInjectorTest() {

        Collection<IConstraint> numberConstraints = new ArrayList<>();
        numberConstraints.add(new NumberConstraint(1));
        numberConstraints.add(new NumberConstraint(2));
        numberConstraints.add(new NumberConstraint(3));
        numberConstraints.add(new NumberConstraint(4));
        numberConstraints.add(new NumberConstraint(5));
        numberConstraints.add(new NumberConstraint(6));

        Collection<IConstraint> colorConstraints = new ArrayList<>();
        colorConstraints.add(new ColorConstraint(Color.GREEN));
        colorConstraints.add(new ColorConstraint(Color.RED));
        colorConstraints.add(new ColorConstraint(Color.YELLOW));
        colorConstraints.add(new ColorConstraint(Color.BLUE));
        colorConstraints.add(new ColorConstraint(Color.PURPLE));

        Collection<IConstraint> sfumatureChiare = new ArrayList<>();
        sfumatureChiare.add(new NumberConstraint(1));
        sfumatureChiare.add(new NumberConstraint(2));

        Collection<IConstraint> sfumatureMedie = new ArrayList<>();
        sfumatureMedie.add(new NumberConstraint(3));
        sfumatureMedie.add(new NumberConstraint(4));

        Collection<IConstraint> sfumatureScure = new ArrayList<>();
        sfumatureScure.add(new NumberConstraint(5));
        sfumatureScure.add(new NumberConstraint(6));

        publicObjectiveCardDrawableCollection.addElement(new RowPublicObjectiveCard("Colori diversi - Riga",
                "Righe senza colori ripetuti",
                6,
                ObjectiveCardType.COLOR));
        publicObjectiveCardDrawableCollection.addElement(new SetPublicObjectiveCard("Sfumature Diverse",
                "Set di dadi di ogni valore ovunque",
                5,numberConstraints,
                ObjectiveCardType.NUMBER));
        publicObjectiveCardDrawableCollection.addElement(new DiagonalPublicObjectiveCard("Diagonali Colorate",
                "Numero di dadi dello stesso colore diagonalmente adiacenti",
                1,
                ObjectiveCardType.COLOR));
        publicObjectiveCardDrawableCollection.addElement(new ColumnPublicObjectiveCard("Colori diversi - Colonna",
                "Colonne senza colori ripetuti",
                5,
                ObjectiveCardType.COLOR));
        publicObjectiveCardDrawableCollection.addElement(new RowPublicObjectiveCard("Sfumature diverse - Riga",
                "Righe senza sfumature ripetute",
                5,
                ObjectiveCardType.NUMBER));
        publicObjectiveCardDrawableCollection.addElement(new ColumnPublicObjectiveCard("Sfumature diverse - Colonna",
                "Colonne senza sfumature ripetute",
                4,
                ObjectiveCardType.NUMBER));
        publicObjectiveCardDrawableCollection.addElement(new SetPublicObjectiveCard("Sfumature Chiare",
                "Set di 1 & 2 ovunque",
                2, sfumatureChiare,
                ObjectiveCardType.NUMBER));
        publicObjectiveCardDrawableCollection.addElement(new SetPublicObjectiveCard("Sfumature Medie",
                "Set di 3 & 4 ovunque",
                2, sfumatureMedie,
                ObjectiveCardType.NUMBER));
        publicObjectiveCardDrawableCollection.addElement(new SetPublicObjectiveCard("Sfumature Scure",
                "Set di 5 & 6 ovunque",
                2, sfumatureScure,
                ObjectiveCardType.NUMBER));
        publicObjectiveCardDrawableCollection.addElement(new SetPublicObjectiveCard("Varietà di Colore",
                "Set di dadi di ogni colore ovunque",
                4, colorConstraints,
                ObjectiveCardType.COLOR));
        GameInjector.injectPublicObjectiveCards(publicObjectiveCardDrawableCollection1);
        for (int i = 0; i < publicObjectiveCardDrawableCollection1.size(); i++) {
            assertTrue(publicObjectiveCardDrawableCollection1.getCollection().
                    containsAll(publicObjectiveCardDrawableCollection.getCollection()));
        }
        assertEquals("Wrong size", 10, publicObjectiveCardDrawableCollection.size());
    }

    @Test
    public void schemaCardInjectorTest() {
        FrontBackSchemaCard frontBackSchemaCard = new FrontBackSchemaCard();
        Tile[][] constraints = new Tile[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new Tile(null);
            }
        }
        constraints[0][0] = new Tile(new ColorConstraint(Color.YELLOW));
        constraints[0][1] = new Tile(new ColorConstraint(Color.BLUE));
        constraints[0][4] = new Tile(new NumberConstraint(1));
        constraints[1][0] = new Tile(new ColorConstraint(Color.GREEN));
        constraints[1][2] = new Tile(new NumberConstraint(5));
        constraints[1][4] = new Tile(new NumberConstraint(4));
        constraints[2][0] = new Tile(new NumberConstraint(3));
        constraints[2][2] = new Tile(new ColorConstraint(Color.RED));
        constraints[2][4] = new Tile(new ColorConstraint(Color.GREEN));
        constraints[3][0] = new Tile(new NumberConstraint(2));
        constraints[3][3] = new Tile(new ColorConstraint(Color.BLUE));
        constraints[3][4] = new Tile(new ColorConstraint(Color.YELLOW));
        SchemaCard schemaCardFront = new SchemaCard("Kaleidoscopic Dream",
                4, constraints);

        frontBackSchemaCard.setSchemaCard(schemaCardFront);

        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new Tile(null);
            }
        }
        constraints[0][0] = new Tile(new NumberConstraint(4));
        constraints[0][2] = new Tile(new NumberConstraint(2));
        constraints[0][3] = new Tile(new NumberConstraint(5));
        constraints[0][4] = new Tile(new ColorConstraint(Color.GREEN));
        constraints[1][2] = new Tile(new NumberConstraint(6));
        constraints[1][3] = new Tile(new ColorConstraint(Color.GREEN));
        constraints[1][4] = new Tile(new NumberConstraint(2));
        constraints[2][1] = new Tile(new NumberConstraint(3));
        constraints[2][2] = new Tile(new ColorConstraint(Color.GREEN));
        constraints[2][3] = new Tile(new NumberConstraint(4));
        constraints[3][0] = new Tile(new NumberConstraint(5));
        constraints[3][1] = new Tile(new ColorConstraint(Color.GREEN));
        constraints[3][2] = new Tile(new NumberConstraint(1));
        SchemaCard schemaCardBack = new SchemaCard("Virtus",
                5, constraints);

        frontBackSchemaCard.setSchemaCard(schemaCardBack);
        GameInjector.injectSchemaCards(schemaCardDrawableCollection);
        assertTrue(schemaCardDrawableCollection.getCollection().contains(frontBackSchemaCard));
    }
}
