package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.card.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.card.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.card.toolcards.ToolCard;

public class Player {
    private ICoin coins;
    private final String token;
    private final SchemaCard schemaCard;
    private final PrivateObjectiveCard privateObjectiveCard;

    public Player(ICoin coins, SchemaCard schemaCard, PrivateObjectiveCard privateObjectiveCard, String token) {
        this.coins = coins;
        this.schemaCard = schemaCard;
        this.privateObjectiveCard = privateObjectiveCard;
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public SchemaCard getSchemaCard() {
        return schemaCard;
    }

    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    public int getCoins() {
        return coins.getCoins();
    }

    public void useCard(ToolCard toolCard) throws DiceNotFoundException, NoCoinsExpendableException, EmptyCollectionException {
        coins.use(toolCard);
        toolCard.invokeCommands(this);
    }

    public void endTurn() {

    }

    public void placeDice(Dice dice, int row, int column) throws TileFilledException, RuleViolationException {
        schemaCard.setDice(dice, row, column);
    }
}
