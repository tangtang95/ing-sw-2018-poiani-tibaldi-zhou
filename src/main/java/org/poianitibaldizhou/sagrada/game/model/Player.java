package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

public class Player {
    private ICoin coins;
    private final String token;
    private SchemaCard schemaCard;
    private PrivateObjectiveCard privateObjectiveCard;
    private Outcome outcome;

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
        this.outcome = Outcome.INGAME;
    }

    //GETTER
    public String getToken() {
        return token;
    }

    public int getCoins() {
        return coins.getCoins();
    }

    public SchemaCard getSchemaCard() {
        return schemaCard;
    }

    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    public Outcome getOutcome() {
        return outcome;
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

    /**
     * place if possible the dice chosen in the place chosen
     *
     * @param dice the dice which will be placed
     * @param row row position (number between 0 and 3 included)
     * @param column column position (number between 0 and 4 included)
     * @param tileConstraint the constraint of the tile chosen
     * @param diceConstraint the constrains of the dice
     * @throws RuleViolationException if the rule of the schema is violated
     */
    public void placeDice(Dice dice, int row, int column, TileConstraintType tileConstraint,
                          DiceConstraintType diceConstraint) throws RuleViolationException {
        schemaCard.setDice(dice, row, column, tileConstraint, diceConstraint);
    }

    public void endTurn() {
        //TODO
    }

    //SETTER
    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public void setSchemaCard(SchemaCard schemaCard) {
        this.schemaCard = schemaCard;
        if (coins instanceof FavorToken)
            coins = new FavorToken(schemaCard.getDifficulty());
    }

    /**
     * Return true if the player has the same token
     *
     * @param obj the other object to compare
     * @return true if the player has the same token
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player))
            return false;
        Player other = (Player) obj;
        return this.getToken().equals(other.getToken());
    }

    /**
     * Return the score of the player based on the PrivateObjectiveCard
     *
     * @return the score of the player only by PrivateObjectiveCard
     */
    public int getScoreFromPrivateCard() {
        return privateObjectiveCard.getScore(schemaCard);
    }

    /**
     * Return the score of the player based on the PrivateObjectiveCard, the remained favor tokens and the empty spaces
     * of the schemaCard
     *
     * @return the score of the player
     */
    public int getScore() {
        //getCoins should be fixed when the game is single player
        return privateObjectiveCard.getScore(schemaCard) + getFavorTokens() - schemaCard.getNumberOfEmptySpaces();
    }


    /**
     * Return favor tokens of the player
     *
     * @return favor tokens of the player
     */
    public int getFavorTokens() {
        if (coins instanceof FavorToken)
            return getCoins();
        return 0;
    }
}
