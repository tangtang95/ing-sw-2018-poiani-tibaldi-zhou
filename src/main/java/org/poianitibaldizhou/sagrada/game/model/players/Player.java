package org.poianitibaldizhou.sagrada.game.model.players;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.*;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ICoin;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.PlayerFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.SchemaCardFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;


import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public abstract class Player implements IVictoryPoints, Serializable, JSONable {

    protected final ICoin coin;
    private final User user;
    protected SchemaCard schemaCard;
    protected final transient List<PrivateObjectiveCard> privateObjectiveCards;
    protected int indexOfPrivateObjectiveCard;
    private Outcome outcome;

    private transient Map<String, PlayerFakeObserver> observerMap;

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
        this.observerMap = new HashMap<>();
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

    public PrivateObjectiveCard getPrivateObjectiveCard() { return privateObjectiveCards.get(indexOfPrivateObjectiveCard); }

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
    public Map<String, PlayerFakeObserver> getObserverMap() {
        return new HashMap<>(observerMap);
    }

    public Map<String, SchemaCardFakeObserver> getSchemaCardObserverMap() { return schemaCard.getObserverMap(); }

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
    public boolean isCardUsable(ToolCard toolCard) {
        return coin.isCardUsable(toolCard);
    }

    // SETTER
    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public void attachObserver(String token, PlayerFakeObserver observer) {
        observerMap.put(token, observer);
    }

    public void detachObserver(String token) {
        observerMap.remove(token);
    }

    public void attachSchemaCardObserver(String token, SchemaCardFakeObserver schemaCardObserver) {
        schemaCard.attachObserver(token, schemaCardObserver);
    }

    public void detachSchemaCardObserver(String token) {
        schemaCard.detachObserver(token);
    }

    public void setSchemaCard(SchemaCard schemaCard) {
        this.schemaCard = SchemaCard.newInstance(schemaCard);
    }

    /**
     * Remove the coins by a certain cost
     *
     * @param cost the value to decrement the coins
     */
    public void removeCoins(int cost) {
        coin.removeCoins(cost);
        observerMap.forEach((key, value) -> value.onFavorTokenChange(cost));
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
        if (!(obj instanceof Player))
            return false;
        Player other = (Player) obj;
        return this.getUser().equals(other.getUser()) && getSchemaCard().equals(other.getSchemaCard())
                && getPrivateObjectiveCards().containsAll(other.getPrivateObjectiveCards())
                && coin.equals(other.coin) && indexOfPrivateObjectiveCard == other.indexOfPrivateObjectiveCard
                && outcome == other.outcome;
    }

    /**
     * Override of hashCode because equals changed
     *
     * @return hashCode of the token string
     */
    @Override
    public int hashCode() {
        return Objects.hash(Player.class, user, schemaCard,
                privateObjectiveCards, coin, outcome, indexOfPrivateObjectiveCard);
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

    public JSONObject toJSON() {
        JSONObject playerJSON = new JSONObject();

        playerJSON.put("token", this.getToken());
        playerJSON.put("username", this.getUser().getName());
        playerJSON.put("coins", this.getCoins());

        return playerJSON;
    }
}
