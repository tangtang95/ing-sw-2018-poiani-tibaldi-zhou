package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Game injector reads all cards from files json and inject them in game.
 */
public class GameInjector {

    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(GameInjector.class.getName());

    /**
     * GameInjector parameter.
     */
    public static final String CARD_NAME = "cardName";
    public static final String CARD_DESCRIPTION = "cardDescription";
    public static final String CARD_COLOUR = "cardColour";
    public static final String CARD_ACTION = "action";
    private static final String CARD_POINTS = "cardPoints";
    private static final String CONSTRAINT_TYPE = "constraintType";

    /**
     * File path.
     */
    public static final String FILE_PATH_1 = "resources/toolCards.json";
    private static final String FILE_PATH_2 = "resources/publicObjectiveCards.json";
    public static final String FILE_PATH_3 = "resources/privateObjectiveCards.json";
    private static final String FILE_PATH_4 = "resources/schemaCards.json";

    /**
     * Static constructor.
     */
    private GameInjector() {
        // DO NOTHING
    }

    /**
     * inject all ToolCard in a DrawableCollection of ToolCard, getting cards from
     * a json file resources/toolCards.json
     *
     * @param toolCardDrawableCollection DrawableCollection of ToolCard
     */
    public static void injectToolCards(@NotNull DrawableCollection<ToolCard> toolCardDrawableCollection) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        jsonArray = null;

        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader(FILE_PATH_1));
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.SEVERE, ServerMessage.PARSE_EXCEPTION, e);
        }

        for (Object object : Objects.requireNonNull(jsonArray)) {
            JSONObject toolCard = (JSONObject) object;
            toolCardDrawableCollection.addElement(new ToolCard(Color.valueOf((String) toolCard.get(CARD_COLOUR)),
                    (String) toolCard.get(CARD_NAME),
                    (String) toolCard.get(CARD_DESCRIPTION),
                    (String) toolCard.get(CARD_ACTION)));
        }
    }

    /**
     * inject dice in a diceBag: 90 dice of 5 color with random number
     * (18 red dices, 18 green dices, 18 blue dices, 18 yellow dices and 18 purple dices)
     *
     * @param diceBag DrawableCollection of Dice not null
     */
    public static void injectDiceBag(@NotNull DrawableCollection<Dice> diceBag) {
        Random random = new Random();
        for (int j = 0; j < 5; j++)
            for (int i = 0; i < 18; i++)
                diceBag.addElement(new Dice(new NumberConstraint(random.nextInt(6) + 1),
                        new ColorConstraint(Color.values()[j])
                ));
    }

    /**
     * inject all PublicObjectiveCard in the DrawableCollection of PublicObjectiveCard, getting
     * cards from a json file resources/publicObjectiveCards.json
     *
     * @param publicObjectiveCardDrawableCollection DrawableCollection of PublicObjectiveCard
     */
    public static void injectPublicObjectiveCards(
            @NotNull DrawableCollection<PublicObjectiveCard> publicObjectiveCardDrawableCollection) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader(FILE_PATH_2));
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.SEVERE, ServerMessage.PARSE_EXCEPTION, e);
        }

        for (Object object : Objects.requireNonNull(jsonArray)) {
            JSONObject publicObjectiveCard = (JSONObject) object;
            JSONObject value = (JSONObject) publicObjectiveCard.get("values");
            switch ((String) value.get("cardType")) {
                case "row":
                    publicObjectiveCardDrawableCollection.addElement(new RowPublicObjectiveCard(
                            (String) publicObjectiveCard.get(CARD_NAME),
                            (String) publicObjectiveCard.get(CARD_DESCRIPTION),
                            Integer.parseInt(publicObjectiveCard.get(CARD_POINTS).toString()),
                            ObjectiveCardType.valueOf((String) value.get(CONSTRAINT_TYPE))
                    ));
                    break;
                case "column":
                    publicObjectiveCardDrawableCollection.addElement(new ColumnPublicObjectiveCard(
                            (String) publicObjectiveCard.get(CARD_NAME),
                            (String) publicObjectiveCard.get(CARD_DESCRIPTION),
                            Integer.parseInt(publicObjectiveCard.get(CARD_POINTS).toString()),
                            ObjectiveCardType.valueOf((String) value.get(CONSTRAINT_TYPE))
                    ));
                    break;
                case "set":
                    Collection<IConstraint> constraints = new ArrayList<>();
                    JSONArray constrainCollection = (JSONArray) value.get("constrainCollection");
                    for (Object o : constrainCollection) {
                        if (value.get(CONSTRAINT_TYPE).equals("COLOR"))
                            constraints.add(new ColorConstraint(
                                    Color.valueOf((String) o)
                            ));
                        else
                            constraints.add(new NumberConstraint(
                                    Integer.parseInt(o.toString())
                            ));
                    }
                    publicObjectiveCardDrawableCollection.addElement(new SetPublicObjectiveCard(
                            (String) publicObjectiveCard.get(CARD_NAME),
                            (String) publicObjectiveCard.get(CARD_DESCRIPTION),
                            Integer.parseInt(publicObjectiveCard.get(CARD_POINTS).toString()),
                            constraints,
                            ObjectiveCardType.valueOf((String) value.get(CONSTRAINT_TYPE))
                    ));
                    break;
                case "diagonal":
                    publicObjectiveCardDrawableCollection.addElement(new DiagonalPublicObjectiveCard(
                            (String) publicObjectiveCard.get(CARD_NAME),
                            (String) publicObjectiveCard.get(CARD_DESCRIPTION),
                            Integer.parseInt(publicObjectiveCard.get(CARD_POINTS).toString()),
                            ObjectiveCardType.valueOf((String) value.get(CONSTRAINT_TYPE))
                    ));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    /**
     * inject all PrivateObjectiveCard in the DrawableCollection of PrivateObjectiveCard, getting
     * cards from a json file resources/privateObjectiveCards.json
     *
     * @param privateObjectiveCardDrawableCollection DrawableCollection of PrivateObjectiveCard
     */
    public static void injectPrivateObjectiveCard(
            @NotNull DrawableCollection<PrivateObjectiveCard> privateObjectiveCardDrawableCollection) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader(FILE_PATH_3));
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.SEVERE, ServerMessage.PARSE_EXCEPTION, e);
        }
        for (Object object : Objects.requireNonNull(jsonArray)) {
            JSONObject privateObjectiveCard = (JSONObject) object;
            privateObjectiveCardDrawableCollection.addElement(new PrivateObjectiveCard(
                    (String) privateObjectiveCard.get(CARD_NAME),
                    (String) privateObjectiveCard.get(CARD_DESCRIPTION),
                    Color.valueOf((String) privateObjectiveCard.get(CARD_COLOUR)
                    )));
        }
    }

    /**
     * inject all SchemaCard in the DrawableCollection of List of SchemaCard, getting
     * cards from a json file resources/schemaCards.json
     *
     * @param schemaCardDrawableCollection DrawableCollection of SchemaCard
     */
    public static void injectSchemaCards(@NotNull DrawableCollection<FrontBackSchemaCard> schemaCardDrawableCollection) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        jsonArray = null;

        List<FrontBackSchemaCard> frontBackSchemaCards = new ArrayList<>();

        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader(FILE_PATH_4));
            for (int i = 0; i < jsonArray.size() / 2; i++) {
                frontBackSchemaCards.add(i, new FrontBackSchemaCard());
            }
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.SEVERE, ServerMessage.PARSE_EXCEPTION, e);
        }

        for (Object object : Objects.requireNonNull(jsonArray)) {
            JSONObject schemaCard = (JSONObject) object;
            IConstraint[][] constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];

            JSONArray matrix = (JSONArray) schemaCard.get("cardMatrix");
            injectSchemaMatrix(matrix, constraints);

            int numberID = Integer.parseInt(schemaCard.get("id").toString());
            frontBackSchemaCards.get(numberID).setSchemaCard(new SchemaCard((String) schemaCard.get(CARD_NAME),
                    Integer.parseInt(schemaCard.get("difficulty").toString()),
                    constraints
            ));
        }
        schemaCardDrawableCollection.addElements(frontBackSchemaCards);
    }

    private static void injectSchemaMatrix(JSONArray matrix, IConstraint[][] constraints) {
        int i = 0;
        for (Object o : matrix) {
            JSONArray row = (JSONArray) o;
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) != null)
                    if (row.get(j) instanceof String)
                        constraints[i][j] = new ColorConstraint(Color.valueOf((String) row.get(j)));
                    else
                        constraints[i][j] = new NumberConstraint(Integer.parseInt(row.get(j).toString()));
            }
            i++;
        }
    }
}
