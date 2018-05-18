package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.SetupPlayerState;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Game {

    private final List<String> tokens;
    protected final List<Player> players;
    private final RoundTrack roundTrack;
    private final List<ToolCard> toolCards;
    private final List<PublicObjectiveCard> publicObjectiveCards;
    private final DrawableCollection<Dice> diceBag;
    protected final DraftPool draftPool;
    private final String name;

    protected IGameStrategy gameStrategy;
    private IStateGame state;

    /**
     * Constructor for Multi player.
     * Create the Game with all the attributes initialized, create also all the player from the given tokens and
     * set the state to SetupPlayerState
     *
     */
    public Game(String name, List<String> tokens) {
        this.tokens = new ArrayList<>(tokens);
        this.players = new ArrayList<>();
        this.diceBag = new DrawableCollection<>();
        this.toolCards = new ArrayList<>();
        this.publicObjectiveCards = new ArrayList<>();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();
        this.name = name;
        setState(new SetupPlayerState(this));
    }

    public Game(String name, String token){
        this.tokens = new ArrayList<>();
        this.tokens.add(token);
        this.players = new ArrayList<>();
        this.diceBag = new DrawableCollection<>();
        this.toolCards = new ArrayList<>();
        this.publicObjectiveCards = new ArrayList<>();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();
        this.name = name;
        setState(new SetupPlayerState(this));
    }


    /**
     * Deep-copy Constructor.
     */
    /*
    private Game(Game game) {
        this.players = new LinkedList<>();
        for (Player p : players)
            this.players.add(Player.newInstance(p));
        this.roundTrack = RoundTrack.newInstance(game.roundTrack);
        this.toolCards = new LinkedList<>();
        for (ToolCard t : game.getToolCards())
            this.toolCards.add(ToolCard.newInstance(t));
        this.publicObjectiveCards = game.publicObjectiveCards;
        this.draftPool = DraftPool.newInstance(game.draftPool);
        this.name = game.name;
        if (game.getGameStrategy() instanceof SinglePlayerGameStrategy)
            this.gameStrategy = SinglePlayerGameStrategy.newInstance((SinglePlayerGameStrategy) game.getGameStrategy());
        else
            this.gameStrategy = new MultiPlayerGameStrategy(((MultiPlayerGameStrategy)game.getGameStrategy()).getNumberOfPlayer());
        this.diceBag = DrawableCollection.newInstance(game.getDiceBag());
        this.state = IStateGame.newInstance(game.state);
    }*/

    //GETTER
    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    public boolean isSinglePlayer() {
        return gameStrategy.isSinglePlayer();
    }

    @Contract(pure = true)
    public List<Player> getPlayers() {
        List<Player> copyPlayers = new ArrayList<>();
        for (Player player : players) {
            copyPlayers.add(Player.newInstance(player));
        }
        return copyPlayers;
    }

    @Contract(pure = true)
    public RoundTrack getRoundTrack() {
        return RoundTrack.newInstance(roundTrack);
    }

    @Contract(pure = true)
    public List<ToolCard> getToolCards() {
        List<ToolCard> copyToolCards = new ArrayList<>();
        for (ToolCard toolCard: toolCards){
            copyToolCards.add(ToolCard.newInstance(toolCard));
        }
        return copyToolCards;
    }

    @Contract(pure = true)
    public List<PublicObjectiveCard> getPublicObjectiveCards() {
        List<PublicObjectiveCard> copyPublicObjectiveCards = new ArrayList<>();
        for (PublicObjectiveCard publicObjectiveCard: publicObjectiveCards){
            copyPublicObjectiveCards.add(publicObjectiveCard);
        }
        return copyPublicObjectiveCards;
    }

    @Contract(pure = true)
    public DraftPool getDraftPool() {
        return DraftPool.newInstance(draftPool);
    }

    @Contract(pure = true)
    public int getNumberOfPlayers() {
        return players.size();
    }

    @Contract(pure = true)
    public IStateGame getState() {
        // TODO deep copy
        return state;
    }

    @Contract(pure = true)
    public DrawableCollection<Dice> getDiceBag() {
        return diceBag;
    }

    @Contract(pure = true)
    public IGameStrategy getGameStrategy() {
        //TODO deep copy
        return gameStrategy;
    }

    @Contract(pure = true)
    public List<String> getToken(){
        return new ArrayList<>(tokens);
    }

    //MODIFIER

    public void setState(IStateGame state) {
        this.state = state;
        this.state.init();
    }

    public void setPlayerOutcome(Player player, Outcome outcome) {
        players.get(getIndexOfPlayer(player)).setOutcome(outcome);
    }

    public void setPlayerSchemaCard(String token, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        gameStrategy.addNewPlayer(token, schemaCard, privateObjectiveCards);
    }

    public void addRemainingDiceToRoundTrack(int currentRound) {
        roundTrack.addDicesToRound(draftPool.getDices(), currentRound);
    }

    public void clearDraftPool() {
        draftPool.clearPool();
    }


    public void swapRoundTrackDice(Dice oldDice, Dice newDice, int round) throws DiceNotFoundException {
        roundTrack.swapDice(oldDice, newDice, round);
    }

    public void swapDraftPoolDice(Dice oldDice, Dice newDice) throws DiceNotFoundException {
        draftPool.addDice(newDice);
        try {
            draftPool.useDice(oldDice);
        } catch (EmptyCollectionException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "ERROR: draftPool is empty!");
        }
    }

    public void addToolCard(ToolCard toolCard) {
        toolCards.add(toolCard);
    }

    public void addPublicObjectiveCard(PublicObjectiveCard publicObjectiveCard) {
        publicObjectiveCards.add(publicObjectiveCard);
    }

    public void addDiceToDraftPool(Dice dice) {
        draftPool.addDice(dice);
    }

    public void addDicesToDraftPoolFromDiceBag() {
        for (int i = 0; i < gameStrategy.getNumberOfDicesToDraw(); i++) {
            try {
                draftPool.addDice(diceBag.draw());
            } catch (EmptyCollectionException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "diceBag is empty", e);
            }
        }
    }

    public void addDiceToDiceBag(Dice dice) {
        diceBag.addElement(dice);
    }

    public void useDraftPoolDice(Dice dice) throws EmptyCollectionException, DiceNotFoundException {
        draftPool.useDice(dice);
    }

    public void reRollDraftPool() {
        draftPool.reRollDices();
    }

    public Dice getDiceFromDiceBag() {
        Dice dice = null;
        try {
            dice = diceBag.draw();
        } catch (EmptyCollectionException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "CRITICAL ERROR: diceBag empty");
        }
        return dice;
    }

    public Dice removeDiceFromSchemaCardPlayer(Player player, int row, int column) {
        return player.removeDiceFromSchemaCard(row, column);
    }

    public void setDiceOnSchemaCardPlayer(Player player, Dice dice, int row, int column,
                                          PlacementRestrictionType restriction, DiceRestrictionType diceRestriction) throws RuleViolationException {
        player.setDiceOnSchemaCard(dice, row, column, restriction, diceRestriction);
    }

    public void initDiceBag() {
        GameInjector.injectDiceBag(diceBag);
    }

    public void releaseToolCardExecution() {
        state.releaseToolCardExecution();
    }

    /**
     * Return the index of the player given based on the list of players
     *
     * @param player the player to find in the list of players
     * @return the index of the list of players about the given player
     */
    public int getIndexOfPlayer(Player player) {
        int indexOfPlayer = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player))
                indexOfPlayer = i;
        }
        if (indexOfPlayer == -1)
            throw new IllegalArgumentException("cannot find the current player in the list of players");
        return indexOfPlayer;
    }

    /**
     * Return the next index of the player for the list of players
     *
     * @param player    the current player
     * @param direction the direction of the next player (clockwise or counterclockwise)
     * @return the index of the next player
     */
    public int getNextIndexOfPlayer(Player player, Direction direction) {
        int indexOfPlayer = getIndexOfPlayer(player);
        return (indexOfPlayer + direction.getIncrement() + getNumberOfPlayers()) % getNumberOfPlayers();
    }

    public void selectPrivateObjectiveCard(Player player, PrivateObjectiveCard privateObjectiveCard) {
        player.setPrivateObjectiveCard(privateObjectiveCard);
    }

    public void calculateOutcome(){
        state.calculateVictoryPoints();
    }

    /*
    public static Game newInstance(Game game) {
        if (game == null)
            return  null;
        return new Game(game);
    }*/
}
