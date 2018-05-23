package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.*;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ICoin;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.game.model.observers.IPlayerObserver;


import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Player implements IVictoryPoints, Serializable {

    protected final ICoin coin;
    private final User user;
    protected SchemaCard schemaCard;
    protected final transient List<PrivateObjectiveCard> privateObjectiveCards;
    protected int indexOfPrivateObjectiveCard;
    private Outcome outcome;
    private List<IPlayerObserver> observerList;

    /**
     * Set to null the player parameters:
     * - coins are the player expendable coins for using the toolCard
     * - schemaCard
     * - privateObjectiveCard
     *
     * @param user                  the same user of the lobby
     * @param schemaCard
     * @param privateObjectiveCards
     */
    public Player(User user, ICoin coin, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        this.schemaCard = SchemaCard.newInstance(schemaCard);
        this.privateObjectiveCards = new ArrayList<>(privateObjectiveCards);
        this.coin = coin;
        this.user = user;
        this.outcome = Outcome.IN_GAME;
        this.indexOfPrivateObjectiveCard = 0;
        this.observerList = new ArrayList<>();
    }

    // GETTER
    public String getToken() {
        return user.getToken();
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

    @Contract(pure = true)
    public User getUser() {
        return user;
    }

    public int getCoins() {
        return coin.getCoins();
    }

    public void removeCoins(int cost) {
        coin.removeCoins(cost);
        observerList.forEach(observer -> observer.onFavorTokenChange(coin.getCoins()));
    }

    public boolean isCardUsable(ToolCard toolCard) throws RemoteException {
        return coin.isCardUsable(toolCard);
    }

    /**
     * It copies the list of observers, creating a new list, but not copying the single elements.
     *
     * @return list of observers
     */
    public List<IPlayerObserver> getObserverList() {
        return new ArrayList<>(observerList);
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
     * @param dice           the dice which will be placed
     * @param position       row position (number between 0 and 3 included)
     * @param tileConstraint the constraint of the tile chosen
     * @param diceConstraint the constrains of the dice
     * @throws RuleViolationException if the rule of the schema is violated
     */
    public void placeDice(Dice dice, Position position, PlacementRestrictionType tileConstraint,
                          DiceRestrictionType diceConstraint) throws RuleViolationException {
        schemaCard.setDice(dice, position, tileConstraint, diceConstraint);
    }

    public void placeDice(Dice dice, Position position) throws RuleViolationException {
        placeDice(dice, position, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        if (!containsPrivateObjectiveCard(privateObjectiveCard))
            throw new IllegalArgumentException("PrivateObjectiveCard doesn't exist in the player");
        for (int i = 0; i < privateObjectiveCards.size(); i++) {
            if (privateObjectiveCards.get(i).equals(privateObjectiveCard))
                indexOfPrivateObjectiveCard = i;
        }
    }

    public void attachObserver(IPlayerObserver observer) {
        observerList.add(observer);
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

    /**
     * Copy player in a new instance of player. Observers are copied by references.
     *
     * @param player player that needs to be copied
     * @return copied player
     */
    public static Player newInstance(@NotNull Player player) {
        Player newPlayer;
        if (player instanceof SinglePlayer)
            newPlayer = SinglePlayer.newInstance(player);
        else
            newPlayer = MultiPlayer.newInstance(player);

        return newPlayer;
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
