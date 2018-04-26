package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

public class Player {
    private ICoin coins;
    private final String token;
    private SchemaCard schemaCard;
    private PrivateObjectiveCard privateObjectiveCard;

    /**
     * set null the player parameter:
     * coins are the player expendable coins for using the toolCard
     * schemaCard
     * privateObjectiveCard
     *
     * @param token string for locating the single player during the game
     */
    public Player(String token, ICoin coins) {
        this.coins = coins;
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

    /**
     * use the card and invoke the card commands
     *
     * @param toolCard the card which the player would use
     * @throws DiceNotFoundException if dice is not present in the DraftPool
     * @throws NoCoinsExpendableException if there aren't any expandable favor tokens or dices
     * @throws EmptyCollectionException if draftPull is empty
     * @throws IllegalNumberOfTokensOnToolCardException if on the ToolCard there is a number of tokens < 0 or 1
     */
    public void useCard(ToolCard toolCard) throws DiceNotFoundException, NoCoinsExpendableException,
            EmptyCollectionException, IllegalNumberOfTokensOnToolCardException {
        coins.use(toolCard);
        toolCard.invokeCommands(this);
    }

    public void endTurn() {
        //TODO
    }

    /**
     * place if possible the dice chosen in the place chosen
     *
     * @param dice the dice which will be placed
     * @param row row position (number between 0 and 3 included)
     * @param column column position (number between 0 and 4 included)
     * @param tileConstraint the constraint of the tile chosen
     * @param diceConstraint the constrains of dice
     * @throws RuleViolationException
     */
    public void placeDice(Dice dice, int row , int column, TileConstraintType tileConstraint,
                          DiceConstraintType diceConstraint) throws RuleViolationException {
        schemaCard.setDice(dice, row, column, tileConstraint, diceConstraint);
    }

    public void setSchemaCard(SchemaCard schemaCard) {
        this.schemaCard = schemaCard;
        if (coins instanceof FavorToken)
            coins = new FavorToken(schemaCard.getDifficulty());
    }

    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }
}
