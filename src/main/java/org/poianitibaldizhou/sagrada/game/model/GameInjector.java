package org.poianitibaldizhou.sagrada.game.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.exception.DiceInvalidNumberException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.cards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SetPublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class GameInjector {
    private DrawableCollection<ToolCard> toolCardDrawableCollection;
    private DrawableCollection<Dice> diceDrawableCollection;
    private JSONParser jsonParser;

    public GameInjector(){
        jsonParser = new JSONParser();
    }

    public void injectToolCards(List<ToolCard> toolCards, boolean isSinglePlayer, int difficulty) {
        List<ToolCard> allToolCards = new LinkedList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader("resources/toolCards.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        for (Object object : jsonArray) {
            JSONObject toolCard = (JSONObject) object;
            allToolCards.add(new ToolCard(  Color.valueOf((String)toolCard.get("cardColour")),
                                            (String)toolCard.get("cardName"),
                                            (String)toolCard.get("cardDescription"),
                                            (String)toolCard.get("action"),
                                            isSinglePlayer
                                         ));

        }
        toolCardDrawableCollection = new DrawableCollection<>(allToolCards);
        if (isSinglePlayer)
            for(int i = 0; i < difficulty; i++)
                try {
                    toolCards.add(toolCardDrawableCollection.draw());
                } catch (EmptyCollectionException e) {
                    e.printStackTrace();
                }
        else
            for(int i = 0; i < 3; i++)
                try {
                    toolCards.add(toolCardDrawableCollection.draw());
                } catch (EmptyCollectionException e) {
                    e.printStackTrace();
                }
    }

    public void injectDiceBag(DrawableCollection<Dice> diceBag){
        Random random = new Random();
        for (int j = 0; j < 5; j++)
            for (int i = 0; i < 18; i++)
                try {
                    diceBag.addElement(new Dice(new NumberConstraint(random.nextInt(6) + 1),
                                                new ColorConstraint(Color.values()[j])
                                                ));
                } catch (DiceInvalidNumberException e) {
                    e.printStackTrace();
                }

    }

    public void injectPublicObjectiveCards(List<PublicObjectiveCard> publicObjectiveCards)  {
        List<PublicObjectiveCard> allPublicObjectiveCards = new LinkedList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader("resources/publicObjectiveCards.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        for (Object object : jsonArray) {
            JSONObject toolCard = (JSONObject) object;

        }
    }

    public void injectPrivateObjectiveCard(DrawableCollection<PrivateObjectiveCard> privateObjectiveCards) {
    }

    public void injectSchemaCards(DrawableCollection<SchemaCard> schemaCards) {
    }
}
