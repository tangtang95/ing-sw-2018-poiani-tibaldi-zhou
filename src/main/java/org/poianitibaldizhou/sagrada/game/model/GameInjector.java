package org.poianitibaldizhou.sagrada.game.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.cards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class GameInjector {
    private JSONParser jsonParser;

    /**
     * Constructor.
     * Creates a JSONParser for analyzing json file
     */
    public GameInjector() {
        jsonParser = new JSONParser();
    }

    /**
     * Create and inject all ToolCard in a DrawableCollection of ToolCard, getting cards from
     * a json file resources/toolCards.json
     *
     * @param toolCardDrawableCollection DrawableCollection of ToolCard
     * @param isSinglePlayer Game parameter for choosing the game mode (single player or multilayer)
     * @return a DrawableCollection of ToolCard
     */
    public DrawableCollection<ToolCard> injectToolCards(DrawableCollection<ToolCard> toolCardDrawableCollection,
                                                        boolean isSinglePlayer) {
        List<ToolCard> allToolCards = new LinkedList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader("resources/toolCards.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        for (Object object : jsonArray) {
            JSONObject toolCard = (JSONObject) object;
            allToolCards.add(new ToolCard(Color.valueOf((String) toolCard.get("cardColour")),
                    (String) toolCard.get("cardName"),
                    (String) toolCard.get("cardDescription"),
                    (String) toolCard.get("action"),
                    isSinglePlayer
            ));
        }
        return new DrawableCollection<>(allToolCards);
    }

    /**
     * Create and inject dice in a diceBag: 90 dice of 5 color with random number
     * (18 red dices, 18 green dices, 18 blue dices, 18 yellow dices and 18 purple dices)
     *
     * @param diceBag DrawableCollection of Dice with null value
     * @return a DrawableCollection of Dice, the diceBag
     */
    public DrawableCollection<Dice> injectDiceBag(DrawableCollection<Dice> diceBag) {
        diceBag = new DrawableCollection<>();
        Random random = new Random();
        for (int j = 0; j < 5; j++)
            for (int i = 0; i < 18; i++)
                diceBag.addElement(new Dice(new NumberConstraint(random.nextInt(6) + 1),
                        new ColorConstraint(Color.values()[j])
                ));
        return diceBag;

    }

    public DrawableCollection<PublicObjectiveCard> injectPublicObjectiveCards(
            DrawableCollection<PublicObjectiveCard> publicObjectiveCardDrawableCollection) {
        List<PublicObjectiveCard> allPublicObjectiveCards = new LinkedList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader("resources/publicObjectiveCards.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        for (Object object : jsonArray) {
            JSONObject publicObjectiveCard = (JSONObject) object;
            JSONObject value = (JSONObject) publicObjectiveCard.get("values");
            switch ((String) value.get("type")) {
                case "row":
                    break;
                case "column":
                    break;
                case "set":
                    break;
                case "diagonal":
                    break;
            }
        }
        //TODO
        return publicObjectiveCardDrawableCollection;
    }

    /**
     * create and inject all PrivateObjectiveCard in the DrawableCollection of PrivateObjectiveCard, getting
     * cards from a json file resources/privateObjectiveCards.json
     *
     * @param privateObjectiveCardDrawableCollection DrawableCollection of PrivateObjectiveCard with null value
     * @return DrawableCollection of PrivateObjectiveCard
     */
    public DrawableCollection<PrivateObjectiveCard> injectPrivateObjectiveCard(
            DrawableCollection<PrivateObjectiveCard> privateObjectiveCardDrawableCollection) {
        List<PrivateObjectiveCard> allPrivateObjectiveCards = new LinkedList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader("resources/privateObjectiveCards.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        for (Object object : jsonArray) {
            JSONObject privateObjectiveCard = (JSONObject) object;
            allPrivateObjectiveCards.add(new PrivateObjectiveCard((String) privateObjectiveCard.get("cardName"),
                    (String) privateObjectiveCard.get("cardDescription"),
                    Color.valueOf((String) privateObjectiveCard.get("cardColor")
                    )));
        }
        return new DrawableCollection<>(allPrivateObjectiveCards);
    }

    /**
     * create and inject all SchemaCard in the DrawableCollection of SchemaCard, getting
     * cards from a json file resources/schemaCards.json
     *
     * @param schemaCardDrawableCollection DrawableCollection of SchemaCard with null value
     * @return DrawableCollection of SchemaCard
     */
    public DrawableCollection<SchemaCard> injectSchemaCards(
            DrawableCollection<SchemaCard> schemaCardDrawableCollection) {
        List<SchemaCard> allSchemaCards = new LinkedList<>();
        JSONArray jsonArray = null;

        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader("resources/schemaCards.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        for (Object object : jsonArray) {
            JSONObject schemaCard = (JSONObject) object;
            IConstraint[][] constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
            JSONArray matrix = (JSONArray) schemaCard.get("cardMatrix");

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
            allSchemaCards.add(new SchemaCard((String)schemaCard.get("cardName"),
                    Integer.parseInt(schemaCard.get("difficulty").toString()),
                    constraints
            ));
        }
        return new DrawableCollection<>(allSchemaCards);
    }
}
