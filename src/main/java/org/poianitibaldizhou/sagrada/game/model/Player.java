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
     * Constructor.
     * Create a new player based on user, strategy coin, his schemaCard and his privataObjectiveCards
     *
     * @param user the user of the player
     * @param coin the strategy coin for the player
     * @param schemaCard the schemaCard of the player
     * @param privateObjectiveCards the list of privateObjectiveCards of the player
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
     * Return true if the toolCard is usable, otherwise false
     *
     * @param toolCard the toolCard to check if usable
     * @return true if the toolCard is usable, otherwise false
     * @throws RemoteException network error
     */
    public boolean isCardUsable(ToolCard toolCard) throws RemoteException {
        return coin.isCardUsable(toolCard);
    }

    // SETTER
    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public void attachObserver(IPlayerObserver observer) {
        observerList.add(observer);
    }

    public void setSchemaCard(SchemaCard schemaCard) {
        this.schemaCard = schemaCard;
    }

    /**
     * Remove the coins by a certain cost
     *
     * @param cost the value to decrement the coins
     * @throws RemoteException network error
     */
    public void removeCoins(int cost) throws RemoteException {
        coin.removeCoins(cost);
        for (IPlayerObserver playerObserver : observerList) {
            playerObserver.onFavorTokenChange(coin.getCoins());
        }
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
                          DiceRestrictionType diceConstraint) throws RuleViolationException, RemoteException {
        schemaCard.setDice(dice, position, tileConstraint, diceConstraint);
    }

    public void placeDice(Dice dice, Position position) throws RuleViolationException, RemoteException {
        placeDice(dice, position, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
    }

    /**
     * Set private objective card to the player (used only in singlePlayer)
     *
     * @param privateObjectiveCard the private objective card chosen
     * @throws IllegalArgumentException if the list of privateObjectiveCards of the player doesn't contain
     *                                  privateObjectiveCard given
     */
    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        if (!containsPrivateObjectiveCard(privateObjectiveCard))
            throw new IllegalArgumentException("PrivateObjectiveCard doesn't exist in the player");
        for (int i = 0; i < privateObjectiveCards.size(); i++) {
            if (privateObjectiveCards.get(i).equals(privateObjectiveCard))
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

}
