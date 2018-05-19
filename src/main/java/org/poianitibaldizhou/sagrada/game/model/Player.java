package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.*;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

import java.util.ArrayList;
import java.util.List;

public class Player{
    private ICoin coins;
    private final String token;
    private SchemaCard schemaCard;
    private final List<PrivateObjectiveCard> privateObjectiveCards;
    private int indexOfPrivateObjectiveCard;
    private Outcome outcome;

    /**
     * Set to null the player parameters:
     * - coins are the player expendable coins for using the toolCard
     * - schemaCard
     * - privateObjectiveCard
     *
     * @param token string for locating the single player during the game
     * @param schemaCard
     * @param privateObjectiveCards
     */
    public Player(String token, ICoin coins, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        this.coins = coins;
        this.schemaCard = SchemaCard.newInstance(schemaCard);
        this.privateObjectiveCards = new ArrayList<>(privateObjectiveCards);
        this.token = token;
        this.outcome = Outcome.IN_GAME;
        this.indexOfPrivateObjectiveCard = 0;
    }

    public String getToken() {
        return token;
    }

    public int getCoins() {
        return coins.getCoins();
    }

    public ICoin getICoins() {return this.coins;}

    public SchemaCard getSchemaCard() {
        return SchemaCard.newInstance(schemaCard);
    }

    public List<PrivateObjectiveCard> getPrivateObjectiveCards() {
        return new ArrayList<>(privateObjectiveCards);
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public boolean isDicePositionableOnSchemaCard(Dice dice, int row, int column) {
        return schemaCard.isDicePositionable(dice, row, column);
    }

    /**
     * Use the card and invoke the card commands
     *
     * @param toolCard the card which the player would use
     * @throws NoCoinsExpendableException if there aren't any expandable favor tokens or dices
     */
    public Node<ICommand> useCard(ToolCard toolCard) throws NoCoinsExpendableException {
        coins.use(toolCard);
        return toolCard.useCard();
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }


    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        if(!containsPrivateObjectiveCard(privateObjectiveCard))
            throw new IllegalArgumentException("PrivateObjectiveCard doesn't exist in the player");
        for (int i = 0; i < privateObjectiveCards.size(); i++) {
            if(privateObjectiveCards.get(i).equals(privateObjectiveCard))
                indexOfPrivateObjectiveCard = i;
        }
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
     * Override of hashCode because equals changed
     *
     * @return hashCode of the token string
     */
    @Override
    public int hashCode() {
        return this.getToken().hashCode();
    }

    /**
     * Return the score of the player based on the PrivateObjectiveCard
     *
     * @return the score of the player only by PrivateObjectiveCard
     */
    public int getScoreFromPrivateCard() {
        return privateObjectiveCards.get(indexOfPrivateObjectiveCard).getScore(schemaCard);
    }

    /**
     * Return the score of the player based on the PrivateObjectiveCard, the remaining favor tokens and the empty spaces
     * of the schemaCard
     *
     * @return the score of the player for multiPlayerGame
     */
    public int getMultiPlayerScore() {
        //getCoins should be fixed when the game is single player
        return privateObjectiveCards.get(indexOfPrivateObjectiveCard)
                .getScore(schemaCard) + getFavorTokens() - schemaCard.getNumberOfEmptySpaces();
    }

    /**
     *
     * @return the score of the player for singlePlayerGame
     */
    public int getSinglePlayerScore(){
        return privateObjectiveCards.get(indexOfPrivateObjectiveCard)
                .getScore(schemaCard) - schemaCard.getNumberOfEmptySpaces()*3;
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

    public static Player newInstance(Player player) {
        if (player == null)
            return null;
        return new Player(player.token,player.coins, player.schemaCard,
                player.privateObjectiveCards);
    }

    private boolean containsPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        for (int i = 0; i < privateObjectiveCards.size(); i++) {
            if (privateObjectiveCards.get(i).equals(privateObjectiveCard))
                return true;
        }
        return false;
    }

    public void setSchemaCard(SchemaCard schemaCard) {
        this.schemaCard = schemaCard;
    }
}
