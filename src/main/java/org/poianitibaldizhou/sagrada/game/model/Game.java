package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Game implements IGame, IGameStrategy {

    protected final List<User> users;
    protected final Map<String, Player> players;
    private RoundTrack roundTrack;
    private final List<ToolCard> toolCards;
    private final List<PublicObjectiveCard> publicObjectiveCards;
    private DrawableCollection<Dice> diceBag;
    protected DraftPool draftPool;
    private final String name;
    private IStateGame state;

    private final Map<String, IGameObserver> gameObservers;
    private final Map<String, IStateObserver> stateObservers;

    protected Game(String name) {
        this.name = name;

        this.users = new ArrayList<>();
        this.players = new HashMap<>();
        this.diceBag = new DrawableCollection<>();
        this.toolCards = new ArrayList<>();
        this.publicObjectiveCards = new ArrayList<>();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();

        this.gameObservers = new HashMap<>();
        this.stateObservers = new HashMap<>();
    }

    //GETTER
    @Contract(pure = true)
    public String getName() {
        return name;
    }

    /**
     * DEEP-COPY
     *
     * @return the deep copy of the list of player
     */
    @Contract(pure = true)
    public List<Player> getPlayers() {
        List<Player> copyPlayers = new ArrayList<>();
        for (Player player : players.values()) {
            copyPlayers.add(Player.newInstance(player));
        }
        return copyPlayers;
    }

    /**
     * DEEP-COPY
     *
     * @return the deep copy of the roundTrack
     * @throws RemoteException network error
     */
    @Contract(pure = true)
    public RoundTrack getRoundTrack() {
        return RoundTrack.newInstance(roundTrack);
    }

    /**
     * DEEP-COPY
     *
     * @return the deep copy of the list of toolCards
     */
    @Contract(pure = true)
    public List<ToolCard> getToolCards() {
        List<ToolCard> copyToolCards = new ArrayList<>();
        for (ToolCard toolCard : toolCards) {
            copyToolCards.add(ToolCard.newInstance(toolCard));
        }
        return copyToolCards;
    }

    /**
     * SHALLOW-COPY (PublicObjectiveCards are immutable)
     *
     * @return the shallow-copy of the list of publicObjectiveCards
     */
    @Contract(pure = true)
    public List<PublicObjectiveCard> getPublicObjectiveCards() {
        return new ArrayList<>(publicObjectiveCards);
    }

    /**
     * DEEP-COPY
     *
     * @return the deep copy of the draftPool
     * @throws RemoteException network error
     */
    @Contract(pure = true)
    public DraftPool getDraftPool() {
        return DraftPool.newInstance(draftPool);
    }

    @Contract(pure = true)
    public int getNumberOfPlayers() {
        return users.size();
    }

    /**
     * @return the list of the state observers (reference)
     */
    public Map<String, IStateObserver> getStateObservers() {
        return stateObservers;
    }

    /**
     * @return the map of the game observers (reference)
     */
    public Map<String, IGameObserver> getGameObservers() {
        return gameObservers;
    }

    @Contract(pure = true)
    public IStateGame getState() {
        return state;
    }

    /**
     * DEEP-COPY
     *
     * @return the deep copy of the diceBag
     */
    @Contract(pure = true)
    public DrawableCollection<Dice> getDiceBag() {
        return new DrawableCollection<>(diceBag.getCollection());
    }

    @Contract(pure = true)
    public List<String> getUserToken() {
        return users.stream().map(User::getToken).collect(Collectors.toList());
    }

    /**
     * @param userToken the token of the user
     * @return the user by token
     * @throws IllegalArgumentException if the user is not founded
     */
    public User getUserByToken(final String userToken) {
        Optional<User> user = users.stream().filter(u -> u.getToken().equals(userToken)).findFirst();
        if (!user.isPresent())
            throw new IllegalArgumentException("SEVERE ERROR: Cannot find User");
        return user.get();
    }

    @Contract(pure = true)
    public int getPlayerScore(Player player) {
        return player.getVictoryPoints();
    }




    // INTERFACE METHODS
    @Override
    public void userFireExecutorEvent(String token, ExecutorEvent event) throws InvalidActionException {
        state.fireExecutorEvent(event);
    }

    // OBSERVER ATTACH (INTERFACE METHODS)
    @Override
    public void attachStateObserver(String token, IStateObserver stateObserver) {
        stateObservers.put(token, stateObserver);
    }

    @Override
    public void attachGameObserver(String userToken, IGameObserver gameObserver) {
        gameObservers.put(userToken, gameObserver);
    }

    @Override
    public void attachRoundTrackObserver(String token, IRoundTrackObserver roundTrackObserver) {
        roundTrack.attachObserver(token, roundTrackObserver);
    }

    @Override
    public void attachDraftPoolObserver(String token, IDraftPoolObserver draftPoolObserver) {
        draftPool.attachObserver(token, draftPoolObserver);
    }

    @Override
    public void attachToolCardObserver(String token, ToolCard toolCard, IToolCardObserver toolCardObserver) throws InvalidActionException {
        for(ToolCard card : toolCards){
            if(card.getName().equals(toolCard.getName())) {
                card.attachToolCardObserver(token, toolCardObserver);
                return;
            }
        }
        throw new InvalidActionException();
    }

    @Override
    public void attachDiceBagObserver(String token, IDrawableCollectionObserver<Dice> drawableCollectionObserver) {
        diceBag.attachObserver(token, drawableCollectionObserver);
    }

    @Override
    public void attachSchemaCardObserver(String token, SchemaCard schemaCard, ISchemaCardObserver schemaCardObserver) throws InvalidActionException {
        for (Player player: players.values()) {
            if(player.getSchemaCard().getName().equals(schemaCard.getName())) {
                player.attachSchemaCardObserver(token, schemaCardObserver);
                return;
            }
        }
        throw new InvalidActionException();
    }

    @Override
    public void attachPlayerObserver(String token, Player player, IPlayerObserver playerObserver) {
        for (Player p : players.values()){
            if(p.getUser().equals(player.getUser())){
                p.attachObserver(token, playerObserver);
            }
        }
    }

    @Override
    public void userJoin(String token) throws InvalidActionException {
        if(!containsToken(token))
            throw new InvalidActionException();
        state.readyGame(token);
    }

    @Override
    public void userSelectSchemaCard(String token, SchemaCard schemaCard) throws InvalidActionException {
        if(!containsToken(token))
            throw new InvalidActionException();
        state.ready(token, schemaCard);
    }

    @Override
    public void userPlaceDice(String token, Dice dice, Position position) throws InvalidActionException {
        if(!containsToken(token))
            throw new InvalidActionException();
        state.placeDice(players.get(token), dice, position);
    }

    @Override
    public void userUseToolCard(String token, ToolCard toolCard, IToolCardExecutorObserver executorObserver) throws InvalidActionException {
        if(!containsToken(token))
            throw new InvalidActionException();
        state.useCard(players.get(token), toolCard, executorObserver);
    }

    @Override
    public void userChooseAction(String token, IActionCommand action) throws InvalidActionException {
        if(!containsToken(token))
            throw new InvalidActionException();
        state.chooseAction(players.get(token), action);
    }

    @Override
    public void userChoosePrivateObjectiveCard(String token, PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException {
        if(!containsToken(token))
            throw new InvalidActionException();
        state.choosePrivateObjectiveCard(players.get(token), privateObjectiveCard);
    }

    @Override
    public boolean containsToken(String token) {
        Optional<String> optToken = users.stream().map(User::getToken).filter(tok -> tok.equals(token)).findFirst();
        return optToken.isPresent();
    }

    //MODIFIER
    public void setState(IStateGame state) {
        this.state = state;
        this.state.init();
    }

    public void setPlayerOutcome(Player player, Outcome outcome) {
        players.get(player.getToken()).setOutcome(outcome);
    }

    public void setPlayerSchemaCard(String userToken, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        User user = getUserByToken(userToken);
        addNewPlayer(user, schemaCard, privateObjectiveCards);
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
     * Return the index of the player given based on the list of tokens
     *
     * @param player the player to find in the list of tokens
     * @return the index of the list of tokens about the given player
     */
    private int getIndexOfPlayer(Player player) {
        int indexOfPlayer = -1;
        for (int i = 0; i < getPlayers().size(); i++) {
            if (getUserToken().get(i).equals(player.getToken()))
                indexOfPlayer = i;
        }
        if (indexOfPlayer == -1)
            throw new IllegalArgumentException("cannot find the current player in the list of players");
        return indexOfPlayer;
    }

    protected Player getPlayerByIndex(int currentIndexOfPlayer) {
        return players.get(users.get(currentIndexOfPlayer).getToken());
    }

    /**
     * Return the next index of the player for the list of players
     *
     * @param player    the current player
     * @param direction the direction of the next player (clockwise or counterclockwise)
     * @return the index of the next player
     */
    private int getNextIndexOfPlayer(Player player, Direction direction) {
        int indexOfPlayer = getIndexOfPlayer(player);
        return (indexOfPlayer + direction.getIncrement() + getNumberOfPlayers()) % getNumberOfPlayers();
    }

    public Player getNextPlayer(Player player, Direction direction) {
        int indexOfNextPlayer = getNextIndexOfPlayer(player, direction);
        return players.get(users.get(indexOfNextPlayer).getToken());
    }

    public void selectPrivateObjectiveCard(Player player, PrivateObjectiveCard privateObjectiveCard) {
        player.setPrivateObjectiveCard(privateObjectiveCard);
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
}
