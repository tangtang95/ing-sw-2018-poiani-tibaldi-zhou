package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
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
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.*;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.ResetState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * OVERVIEW: Game represents a game, so it has all the references to the board game stuff of Sagrada. 
 */
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

    private final Map<String, IGameFakeObserver> gameObservers;
    private final Map<String, IStateFakeObserver> stateObservers;

    private final TerminationGameManager terminationGameManager;

    /**
     * Constructor.
     * Creates a game with a certain name and his termination manager
     *
     * @param name                   game's name
     * @param terminationGameManager game's termination manager
     */
    protected Game(String name, TerminationGameManager terminationGameManager) {
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

        this.terminationGameManager = terminationGameManager;
    }

    //GETTER
    @Contract(pure = true)
    public String getName() {
        return name;
    }

    /**
     * Get the list of players in the game by reference
     * @return list of game's player by references
     */
    public List<Player> getPlayerListReferences() {
        ArrayList<Player> playerArrayList = new ArrayList<>();
        players.forEach((key, value) -> playerArrayList.add(value));
        return playerArrayList;
    }

    /**
     * SHALLOW-COPY (PublicObjectiveCards are immutable)
     *
     * @return the shallow-copy of the list of publicObjectiveCards
     */
    @Override
    @Contract(pure = true)
    public List<PublicObjectiveCard> getPublicObjectiveCards() {
        return new ArrayList<>(publicObjectiveCards);
    }

    @Contract(pure = true)
    public int getNumberOfPlayers() {
        return users.size();
    }

    /**
     * @return the list of the state observers (reference)
     */
    public Map<String, IStateFakeObserver> getStateObservers() {
        return stateObservers;
    }

    /**
     * @return the map of the game observers (reference)
     */
    public Map<String, IGameFakeObserver> getGameObservers() {
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void initGame() {
        setState(new ResetState(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceSkipTurn() throws InvalidActionException {
        state.forceSkipTurn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceStateChange() throws InvalidActionException {
        state.forceStateChange();
    }

    @Override
    public void forceGameTerminationBeforeStarting() throws InvalidActionException {
        state.forceGameTerminationBeforeStarting();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getTimedOutUsers() {
        return getUsers().stream().filter(user -> !players.containsKey(user.getToken())).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userFireExecutorEvent(String token, ExecutorEvent event) throws InvalidActionException {
        state.fireExecutorEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoundTrack getRoundTrack() {
        return RoundTrack.newInstance(roundTrack);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DraftPool getDraftPool() {
        return DraftPool.newInstance(draftPool);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ToolCard> getToolCards() {
        List<ToolCard> copyToolCards = new ArrayList<>();
        for (ToolCard toolCard : toolCards) {
            copyToolCards.add(ToolCard.newInstance(toolCard));
        }
        return copyToolCards;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> getPlayers() {
        List<Player> copyPlayers = new ArrayList<>();
        for (Player player : players.values()) {
            copyPlayers.add(Player.newInstance(player));
        }
        return copyPlayers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detachObservers(String token) {
        roundTrack.detachObserver(token);
        diceBag.detachObserver(token);
        detachGameObserver(token);
        detachStateObserver(token);
        draftPool.detachObserver(token);
        players.forEach((key, value) -> {
            value.detachObserver(token);
            value.detachSchemaCardObserver(token);
        });

        toolCards.forEach(toolcard -> toolcard.detachToolCardObserver(token));
    }

    // OBSERVER ATTACH (INTERFACE METHODS)

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachStateObserver(String token, IStateFakeObserver stateObserver) {
        stateObservers.put(token, stateObserver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachGameObserver(String userToken, IGameFakeObserver gameObserver) {
        gameObservers.put(userToken, gameObserver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachRoundTrackObserver(String token, IRoundTrackFakeObserver roundTrackObserver) {
        roundTrack.attachObserver(token, roundTrackObserver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachDraftPoolObserver(String token, IDraftPoolFakeObserver draftPoolObserver) {
        draftPool.attachObserver(token, draftPoolObserver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachToolCardObserver(String token, ToolCard toolCard, IToolCardFakeObserver toolCardObserver) {
        for (ToolCard card : toolCards) {
            if (card.getName().equals(toolCard.getName())) {
                card.attachToolCardObserver(token, toolCardObserver);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachDiceBagObserver(String token, IDrawableCollectionFakeObserver<Dice> drawableCollectionObserver) {
        diceBag.attachObserver(token, drawableCollectionObserver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachSchemaCardObserver(String token, SchemaCard schemaCard, ISchemaCardFakeObserver schemaCardObserver) {
        for (Player player : players.values()) {
            if (player.getSchemaCard().getName().equals(schemaCard.getName())) {
                player.attachSchemaCardObserver(token, schemaCardObserver);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachPlayerObserver(String token, Player player, IPlayerFakeObserver playerObserver) {
        for (Player p : players.values()) {
            if (p.getUser().equals(player.getUser())) {
                p.attachObserver(token, playerObserver);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userJoin(String token) throws InvalidActionException {
        if (!containsToken(token))
            throw new InvalidActionException();
        state.readyGame(token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userSelectSchemaCard(String token, SchemaCard schemaCard) throws InvalidActionException {
        if (!containsToken(token))
            throw new InvalidActionException();
        state.ready(token, schemaCard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userPlaceDice(String token, Dice dice, Position position) throws InvalidActionException {
        if (!containsToken(token) || !draftPool.getDices().contains(dice))
            throw new InvalidActionException();

        state.placeDice(players.get(token), dice, position);
        try {
            draftPool.useDice(dice);
        } catch (DiceNotFoundException | EmptyCollectionException e) {
            throw new IllegalStateException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userUseToolCard(String token, ToolCard toolCard, IToolCardExecutorFakeObserver executorObserver) throws InvalidActionException {
        if (!containsToken(token))
            throw new IllegalArgumentException();
        Optional<ToolCard> toolCardRef = toolCards.stream().filter(card -> card.equals(toolCard)).findFirst();
        if (!toolCardRef.isPresent())
            throw new InvalidActionException();
        state.useCard(players.get(token), toolCardRef.get(), executorObserver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PrivateObjectiveCard> getPrivateObjectiveCardsByToken(String token) {
        return new ArrayList<>(players.get(token).getPrivateObjectiveCards());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userChooseAction(String token, IActionCommand action) throws  InvalidActionException {
        if (!containsToken(token))
            throw new IllegalArgumentException();
        state.chooseAction(players.get(token), action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userChoosePrivateObjectiveCard(String token, PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException {
        if (!containsToken(token))
            throw new IllegalArgumentException();
        state.choosePrivateObjectiveCard(players.get(token), privateObjectiveCard);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsToken(String token) {
        Optional<String> optToken = users.stream().map(User::getToken).filter(tok -> tok.equals(token)).findFirst();
        return optToken.isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCurrentPlayer() throws InvalidActionException {
        return state.getCurrentPlayer();
    }

    //MODIFIER

    /**
     * Detach the game observers with a certain token
     *
     * @param token token of the observers that needs to be removed
     */
    public void detachGameObserver(String token) {
        gameObservers.remove(token);
    }

    /**
     * Detach the game observers with a certain token
     *
     * @param token token of the observers that needs to be removed
     */
    public void detachStateObserver(String token) {
        stateObservers.remove(token);
    }

    /**
     * Set and initialize a new state for the game
     *
     * @param state state that need to be set and initialized
     */
    public void setState(IStateGame state) {
        this.state = state;
        this.state.init();
    }

    /**
     * Terminate the game
     */
    public void terminateGame() {
        terminationGameManager.terminateGame();
    }

    /**
     * Set the outcome of the game for a certain player
     *
     * @param player  player that need to be related with outcome
     * @param outcome player's outcome
     */
    public void setPlayerOutcome(Player player, Outcome outcome) {
        players.get(player.getToken()).setOutcome(outcome);
    }

    /**
     * Set a player with his schema card and the list of his private objective cards
     *
     * @param userToken             player's token
     * @param schemaCard            player's schema card
     * @param privateObjectiveCards player's private objective cards
     */
    public void setPlayer(String userToken, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        User user = getUserByToken(userToken);
        addNewPlayer(user, schemaCard, privateObjectiveCards);
    }

    /**
     * Add the remaining dice of the draft pool the the round track
     *
     * @param currentRound round of the round track on which the dices are added
     */
    public void addRemainingDiceToRoundTrack(int currentRound) {
        roundTrack.addDicesToRound(draftPool.getDices(), currentRound);
    }

    /**
     * Add a tool card to the game
     *
     * @param toolCard tool card added
     */
    public void addToolCard(ToolCard toolCard) {
        toolCards.add(toolCard);
    }

    /**
     * Add a public objective card to the game
     *
     * @param publicObjectiveCard public objective card added
     */
    public void addPublicObjectiveCard(PublicObjectiveCard publicObjectiveCard) {
        publicObjectiveCards.add(publicObjectiveCard);
    }

    /**
     * Init the dice bag of the game
     */
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

    /**
     * Returns the next player of the game
     *
     * @param player    previous player
     * @param direction direction of the turn (clock wise or anti clock wise)
     * @return next player
     */
    public Player getNextPlayer(Player player, Direction direction) {
        int indexOfNextPlayer = getNextIndexOfPlayer(player, direction);
        return players.get(users.get(indexOfNextPlayer).getToken());
    }

    /**
     * Set the private objective card for a certain player
     *
     * @param player               player related with privateObjectiveCard
     * @param privateObjectiveCard player's private objective card
     */
    public void selectPrivateObjectiveCard(Player player, PrivateObjectiveCard privateObjectiveCard) {
        player.setPrivateObjectiveCard(privateObjectiveCard);
    }

    /**
     * Set a draft pool for the game
     *
     * @param draftPool new draft pool
     */
    public void setDraftPool(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    /**
     * Set a dice bag for the game
     *
     * @param diceBag new dice bag
     */
    public void setDiceBag(DrawableCollection<Dice> diceBag) {
        this.diceBag = diceBag;
    }

    /**
     * Set a round track for the game
     *
     * @param roundTrack new round track
     */
    public void setRoundTrack(RoundTrack roundTrack) {
        this.roundTrack = roundTrack;
    }

    /**
     * Clear the draft pool. After this call the draft pool will be empty
     */
    public void clearDraftPool() {
        draftPool.clearPool();
    }

    /**
     * The number of dices that need to be thrown are removed from the dice bag and added to the draft pool
     */
    public void addDicesToDraftPoolFromDiceBag() {
        for (int i = 0; i < getNumberOfDicesToDraw(); i++) {
            try {
                draftPool.addDice(diceBag.draw());
            } catch (EmptyCollectionException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "diceBag is empty", e);
                throw new IllegalStateException();
            }
        }
    }
}
