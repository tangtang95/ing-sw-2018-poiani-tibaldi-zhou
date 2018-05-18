package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;

import java.util.List;

public class MultiPlayerGame extends Game {

    public MultiPlayerGame(String name, List<String> tokens) {
        super(name, tokens);
        this.gameStrategy = new MultiPlayerGameStrategy(this);
    }

    public void addNewPlayer(String token, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        players.add(new Player(token, new FavorToken(schemaCard.getDifficulty()), schemaCard, privateObjectiveCards));
    }

}
