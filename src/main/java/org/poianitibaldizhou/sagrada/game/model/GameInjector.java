package org.poianitibaldizhou.sagrada.game.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.card.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.card.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.card.toolcards.ToolCard;

import java.io.FileNotFoundException;
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

    public void toolCardInjector(List<ToolCard> toolCards, boolean isSinglePlayer, int difficulty) throws IOException, ParseException, EmptyCollectionException {
        List<ToolCard> allToolcards = new LinkedList<>();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader("resources/toolCards.json"));

        for (Object object : jsonArray) {
            JSONObject toolCard = (JSONObject) object;
            allToolcards.add(new ToolCard(  Color.valueOf((String)toolCard.get("cardColour")),
                                            (String)toolCard.get("cardName"),
                                            (String)toolCard.get("cardDescription"),
                                            (String)toolCard.get("action"),
                                            isSinglePlayer
                                         ));

        }
        toolCardDrawableCollection = new DrawableCollection<>(allToolcards);
        if (isSinglePlayer)
            for(int i = 0; i < difficulty; i++)
                toolCards.add(toolCardDrawableCollection.draw());
        else
            for(int i = 0; i < 3; i++)
                toolCards.add(toolCardDrawableCollection.draw());
    }

    public void diceBagInjector(DrawableCollection<Dice> diceBag){
        Random random = new Random();
        for (int j = 0; j < 5; j++)
            for (int i = 0; i < 18; i++)
                diceBag.addElement(new Dice(new NumberConstraint(random.nextInt(6) + 1),
                                            new ColorConstraint(Color.values()[j])
                                            ));

    }

    public void publicObjectiveCardInjector(List<PublicObjectiveCard> publicObjectiveCards) throws IOException, ParseException {
        List<PublicObjectiveCard> allPublicObjectiveCards = new LinkedList<>();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader("resources/publicObjectiveCards.json"));

        for (Object object : jsonArray) {
            JSONObject toolCard = (JSONObject) object;
            allPublicObjectiveCards.add(new setPublicObjectiveCard( (String) toolCard.get("cardName"),
                                                                    (String) toolCard.get("cardDescription"),
                                                                    (String) toolCard.get("action")
                                                                ));
            }

        }
    }
}
