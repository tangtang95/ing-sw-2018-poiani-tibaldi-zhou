package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.ResetState;
import org.poianitibaldizhou.sagrada.game.model.state.SetupPlayerState;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Game implements IGameStrategy{

    private final List<String> playerTokens;
    protected final List<Player> players;
    private RoundTrack roundTrack;
    private final List<ToolCard> toolCards;
    private final List<PublicObjectiveCard> publicObjectiveCards;
    private DrawableCollection<Dice> diceBag;
    protected DraftPool draftPool;
    private final String name;
    private IStateGame state;

    /**
     * Constructor for Multi player.
     * Create the Game with all the attributes initialized, create also all the player from the given playerTokens and
     * set the state to SetupPlayerState
     *
     */
    public Game(String name, List<String> playerTokens) {
        this.playerTokens = new ArrayList<>(playerTokens);
        this.players = new ArrayList<>();
        this.diceBag = new DrawableCollection<>();
        this.toolCards = new ArrayList<>();
        this.publicObjectiveCards = new ArrayList<>();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();
        this.name = name;
        setState(new ResetState(this));
    }

    public Game(String name, String playerToken){
        this.playerTokens = new ArrayList<>();
        this.playerTokens.add(playerToken);
        this.players = new ArrayList<>();
        this.diceBag = new DrawableCollection<>();
        this.toolCards = new ArrayList<>();
        this.publicObjectiveCards = new ArrayList<>();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();
        this.name = name;
        setState(new ResetState(this));
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
        this.diceBag = DrawableCollection.newInstance(game.getDiceBag());
        this.state = IStateGame.newInstance(game.state);
    }*/

    //GETTER
    @Contract(pure = true)
    public String getName() {
        return name;
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
        return playerTokens.size();
    }

    @Contract(pure = true)
    public IStateGame getState() {
        return state;
    }

    @Contract(pure = true)
    public DrawableCollection<Dice> getDiceBag() {
        // TODO deep copy
        return diceBag;
    }

    @Contract(pure = true)
    public List<String> getToken(){
        return new ArrayList<>(playerTokens);
    }

    public int getPlayerScore(Player player) {
        return player.getVictoryPoints();
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
        addNewPlayer(token, schemaCard, privateObjectiveCards);
    }

    public void addRemainingDiceToRoundTrack(int currentRound) {
        roundTrack.addDicesToRound(draftPool.getDices(), currentRound);
    }

    public void addToolCard(ToolCard toolCard) {
        toolCards.add(toolCard);
    }

    public void addPublicObjectiveCard(PublicObjectiveCard publicObjectiveCard) {
        publicObjectiveCards.add(publicObjectiveCard);
    }

    public void initDiceBag() {
        GameInjector.injectDiceBag(diceBag);
    }
    
    /**
     * Return the index of the player given based on the list of players
     *
     * @param player the player to find in the list of players
     * @return the index of the list of players about the given player
     */
    public int getIndexOfPlayer(Player player) {
        int indexOfPlayer = -1;
        for (int i = 0; i < getPlayers().size(); i++) {
            if (getPlayers().get(i).equals(player))
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

    public void setDraftPool(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    public void setDiceBag(DrawableCollection<Dice> diceBag) {
        this.diceBag = diceBag;
    }

    public void setRoundTrack(RoundTrack roundTrack) {
        this.roundTrack = roundTrack;
    }

    public void clearDraftPool() {
        draftPool.clearPool();
    }

    public void addDicesToDraftPoolFromDiceBag() {
        for (int i = 0; i < getNumberOfDicesToDraw(); i++) {
            try {
                draftPool.addDice(diceBag.draw());
            } catch (EmptyCollectionException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "diceBag is empty", e);
            }
        }
    }

    public void setExecutor(ExecutorEvent event) {
        event.setNeededValue(((TurnState) state).getToolCardExecutor());
    }


    /*
    public static Game newInstance(Game game) {
        if (game == null)
            return  null;
        return new Game(game);
    }*/
}
