package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.ConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCardPoint;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

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

    public void useCard(ToolCard toolCard) throws DiceNotFoundException, NoCoinsExpendableException, EmptyCollectionException, IllegalNumberOfTokensOnToolCardException {
        coins.use(toolCard);
        toolCard.invokeCommands(this);
    }

    public void endTurn() {

    }

    public void placeDice(Dice dice, SchemaCardPoint point, ConstraintType type) throws RuleViolationException, ConstraintTypeException {
        schemaCard.setDice(dice, point, type);
    }
}
