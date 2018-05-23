package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.*;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Game implements IGameStrategy {

    protected final List<User> users;
    protected final HashMap<String, Player> players;
    private RoundTrack roundTrack;
    private final List<ToolCard> toolCards;
    private final List<PublicObjectiveCard> publicObjectiveCards;
    private DrawableCollection<Dice> diceBag;
    protected DraftPool draftPool;
    private final String name;
    private IStateGame state;

    private final Map<String, IGameObserver> gameObservers;
    private final List<IStateObserver> stateObservers;

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
        this.stateObservers = new ArrayList<>();
    }

    //GETTER
    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    public List<Player> getPlayers() {
        List<Player> copyPlayers = new ArrayList<>();
        for (Player player : players.values()) {
            copyPlayers.add(Player.newInstance(player));
        }
        return copyPlayers;
    }

    @Contract(pure = true)
    public RoundTrack getRoundTrack() throws RemoteException {
        return RoundTrack.newInstance(roundTrack);
    }

    @Contract(pure = true)
    public List<ToolCard> getToolCards() {
        List<ToolCard> copyToolCards = new ArrayList<>();
        for (ToolCard toolCard : toolCards) {
            copyToolCards.add(ToolCard.newInstance(toolCard));
        }
        return copyToolCards;
    }

    @Contract(pure = true)
    public List<PublicObjectiveCard> getPublicObjectiveCards() {
        return new ArrayList<>(publicObjectiveCards);
    }

    @Contract(pure = true)
    public DraftPool getDraftPool() throws RemoteException {
        return DraftPool.newInstance(draftPool);
    }

    @Contract(pure = true)
    public int getNumberOfPlayers() {
        return users.size();
    }

    /**
     * @return the list of the state observers (note that the state observers are references)
     */
    public List<IStateObserver> getStateObservers() {
        return new ArrayList<>(stateObservers);
    }

    public Map<String, IGameObserver> getGameObservers() {
        return new HashMap<>(gameObservers);
    }

    @Contract(pure = true)
    public IStateGame getState() {
        return state;
    }

    @Contract(pure = true)
    public DrawableCollection<Dice> getDiceBag() throws RemoteException {
        DrawableCollection<Dice> newDiceBag = new DrawableCollection<>();
        newDiceBag.addElements(diceBag.getCollection());
        return newDiceBag;
    }

    @Contract(pure = true)
    public List<String> getUserToken() {
        return users.stream().map(User::getToken).collect(Collectors.toList());
    }

    public User getUserByToken(final String userToken) {
        Optional<User> user = users.stream().filter(u -> u.getToken().equals(userToken)).findFirst();
        if (!user.isPresent())
            throw new IllegalArgumentException("Cannot find User");
        return user.get();
    }

    @Contract(pure = true)
    public int getPlayerScore(Player player) {
        return player.getVictoryPoints();
    }


    // OBSERVER ATTACH
    public void attachStateObserver(IStateObserver stateObserver) {
        stateObservers.add(stateObserver);
    }

    public void attachGameObserver(String userToken, IGameObserver gameObserver) {
        gameObservers.put(userToken, gameObserver);
    }

    public void attachRoundTrackObserver(IRoundTrackObserver roundTrackObserver) {
        // TODO
    }

    public void attachDraftPoolObserver(IDraftPoolObserver draftPoolObserver) {
        draftPool.attachObserver(draftPoolObserver);
    }

    public void attachToolCardObserver(ToolCard toolCard, IToolCardObserver toolCardObserver) throws InvalidActionException {
        if (!toolCards.contains(toolCard)) {
            throw new InvalidActionException();
        }
        toolCard.attachToolCardObserver(toolCardObserver);
    }

    public void attachDiceBagObserver(IDrawableCollectionObserver<Dice> drawableCollectionObserver) {
        diceBag.attachObserver(drawableCollectionObserver);
    }

    //MODIFIER

    public void setState(IStateGame state) throws RemoteException {
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

    public void addRemainingDiceToRoundTrack(int currentRound) throws RemoteException {
        roundTrack.addDicesToRound(draftPool.getDices(), currentRound);
    }

    public void addToolCard(ToolCard toolCard) {
        toolCards.add(toolCard);
    }

    public void addPublicObjectiveCard(PublicObjectiveCard publicObjectiveCard) {
        publicObjectiveCards.add(publicObjectiveCard);
    }

    public void initDiceBag() throws RemoteException {
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

    public void clearDraftPool() throws RemoteException {
        draftPool.clearPool();
    }

    public void addDicesToDraftPoolFromDiceBag() throws RemoteException {
        for (int i = 0; i < getNumberOfDicesToDraw(); i++) {
            try {
                draftPool.addDice(diceBag.draw());
            } catch (EmptyCollectionException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "diceBag is empty", e);
            }
        }
    }

    public void setExecutor(ExecutorEvent event) throws InvalidActionException {
        state.fireExecutorEvent(event);
    }

}
