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
    private SchemaCard schemaCard;
    private PrivateObjectiveCard privateObjectiveCard;

    public Player(String token) {
        this.coins = null;
        this.schemaCard = null;
        this.privateObjectiveCard = null;
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

    public void setCoins(ICoin coins) {
        this.coins = coins;
    }

    public void setSchemaCard(SchemaCard schemaCard) {
        //TODO deep copy
        this.schemaCard = schemaCard;
    }

    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }
}
