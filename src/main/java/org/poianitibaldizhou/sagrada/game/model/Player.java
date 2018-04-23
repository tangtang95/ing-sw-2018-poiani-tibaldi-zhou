package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.*;
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

    public void placeDice(Dice dice, int row , int column, TileConstraintType tileConstraint,
                          DiceConstraintType diceConstraint) throws RuleViolationException {
        schemaCard.setDice(dice, row, column, tileConstraint, diceConstraint);
    }

    public void setCoins(ICoin coins) {
        this.coins = coins;
    }

    public void setSchemaCard(SchemaCard schemaCard) {
        this.schemaCard = schemaCard;
    }

    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }
}
