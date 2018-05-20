package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.*;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Player implements IVictoryPoints {

    protected final ICoin coin;
    private final String token;
    protected SchemaCard schemaCard;
    protected final List<PrivateObjectiveCard> privateObjectiveCards;
    protected int indexOfPrivateObjectiveCard;
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
    public Player(String token, ICoin coin, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        this.schemaCard = SchemaCard.newInstance(schemaCard);
        this.privateObjectiveCards = new ArrayList<>(privateObjectiveCards);
        this.coin = coin;
        this.token = token;
        this.outcome = Outcome.IN_GAME;
        this.indexOfPrivateObjectiveCard = 0;
    }

    public String getToken() {
        return token;
    }

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

    public int getCoins() {
        return coin.getCoins();
    }

    public void removeCoins(int cost) {
        coin.removeCoins(cost);
    }

    public boolean isCardUsable(ToolCard toolCard) {
        return coin.isCardUsable(toolCard);
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
     * Place if possible the dice chosen in the place chosen
     *
     * @param dice the dice which will be placed
     * @param row row position (number between 0 and 3 included)
     * @param column column position (number between 0 and 4 included)
     * @param tileConstraint the constraint of the tile chosen
     * @param diceConstraint the constrains of the dice
     * @throws RuleViolationException if the rule of the schema is violated
     */
    public void placeDice(Dice dice, int row, int column, PlacementRestrictionType tileConstraint,
                          DiceRestrictionType diceConstraint) throws RuleViolationException {
        schemaCard.setDice(dice, row, column, tileConstraint, diceConstraint);
    }

    public void placeDice(Dice dice, int row, int column) throws RuleViolationException {
        placeDice(dice, row, column, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
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
        if (!(obj instanceof MultiPlayer))
            return false;
        MultiPlayer other = (MultiPlayer) obj;
        return this.getToken().equals(other.getToken()) && getSchemaCard().equals(other.getSchemaCard())
                && getPrivateObjectiveCards().equals(other.getPrivateObjectiveCards()) && coin.equals(other.coin);
    }

    /**
     * Override of hashCode because equals changed
     *
     * @return hashCode of the token string
     */
    @Override
    public int hashCode() {
        return Objects.hash(MultiPlayer.class, schemaCard, privateObjectiveCards, coin);
    }

    public static Player newInstance(Player player) {
        if (player == null)
            return null;
        if(player instanceof SinglePlayer)
            return new SinglePlayer(player.token, player.coin, player.schemaCard,
                player.privateObjectiveCards);
        else
            return new MultiPlayer(player.token, player.coin, player.schemaCard,
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
