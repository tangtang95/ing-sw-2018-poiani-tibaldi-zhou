package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.SetupPlayerState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {

    private final boolean isSinglePlayer;
    private final List<Player> players;
    private final RoundTrack roundTrack;
    private final List<ToolCard> toolCards;
    private final List<PublicObjectiveCard> publicObjectiveCards;
    private final DrawableCollection<Dice> diceBag;
    private final DraftPool draftPool;
    private final String name;

    private IGameStrategy gameStrategy;
    private IStateGame state;


    /**
     * Constructor for Multi player.
     * Create the Game with all the attributes initialized, create also all the player from the given tokens and
     * set the state to SetupPlayerState
     *
     * @param tokens the list of String token of the players
     */
    public Game(List<String> tokens, String name) {
        this.isSinglePlayer = false;
        this.players = new LinkedList<>();
        this.diceBag = new DrawableCollection<>();
        this.toolCards = new LinkedList<>();
        this.publicObjectiveCards = new LinkedList<>();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();
        this.name = name;
        for (String token : tokens) {
            players.add(new Player(token, new FavorToken()));
        }

        this.gameStrategy = new MultiPlayerGameStrategy(players.size());
        setState(new SetupPlayerState(this));
    }

    private Game(List<Player> players, RoundTrack roundTrack, List<ToolCard> toolCards,
                 List<PublicObjectiveCard> publicObjectiveCards, DrawableCollection<Dice> diceBag,
                 DraftPool draftPool, String name, IStateGame state,
                 Boolean isSinglePlayer, int difficulty) {
        this.players = new LinkedList<>();
        for (Player p : players)
            this.players.add(Player.newInstance(p));
        this.roundTrack = RoundTrack.newInstance(roundTrack);
        this.toolCards = new LinkedList<>();

        this.publicObjectiveCards = new LinkedList<>();
        for (PublicObjectiveCard poc : publicObjectiveCards)
            this.publicObjectiveCards.add(PublicObjectiveCard.newInstance(poc));
        this.draftPool = DraftPool.newInstance(draftPool);
        this.name = name;
        this.isSinglePlayer = isSinglePlayer;
        //need a refactor
        this.diceBag = diceBag;
        this.state = state;
    }

    /**
     * Constructor of Single Player.
     * Create the Game for the single player with all the attributes intialized
     *
     * @param singlePlayerToken the token of the single player
     * @param difficulty        the difficulty of the game chosen by the user
     */
    public Game(String singlePlayerToken, int difficulty) {
        this.isSinglePlayer = true;
        this.players = new LinkedList<>();
        this.diceBag = new DrawableCollection<>();
        this.toolCards = new LinkedList<>();
        this.publicObjectiveCards = new LinkedList<>();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();
        this.gameStrategy = new SinglePlayerGameStrategy(difficulty);

        players.add(new Player(singlePlayerToken, new ExpendableDice(draftPool)));
        setState(new SetupPlayerState(this));

        name = "Single player";
    }

    public String getName() {
        return name;
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public List<Player> getPlayers() {
        List<Player> copyPlayers = new ArrayList<>();
        for (Player player : players) {
            copyPlayers.add(player.newInstance(player));
        }
        return copyPlayers;
    }

    public RoundTrack getRoundTrack() {
        return RoundTrack.newInstance(roundTrack);
    }

    public List<ToolCard> getToolCards() {
        return toolCards;
    }

    public List<PublicObjectiveCard> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    public void setState(IStateGame state) {
        this.state = state;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public DrawableCollection<Dice> getDiceBag() {
        return diceBag;
    }

    public IGameStrategy getGameStrategy() {
        return gameStrategy;
    }

    public void setPrivateObjectiveCards(Player player, DrawableCollection<PrivateObjectiveCard> privateObjectiveCards) {
        gameStrategy.setPrivateObjectiveCard(player, privateObjectiveCards);
    }

    public void readyGame() {
        state.readyGame();
    }

    public void setPlayerOutcome(Player player, Outcome outcome) {
        players.get(getIndexOfPlayer(player)).setOutcome(outcome);
    }

    public void setPlayerSchemaCard(Player player, SchemaCard schemaCard) {
        players.get(getIndexOfPlayer(player)).setSchemaCard(schemaCard);
    }

    public void addRemainingDiceToRoundTrack(int currentRound) {
        roundTrack.addDicesToRound(draftPool.getDices(), currentRound);
    }

    public void clearDraftPool() {
        draftPool.clearPool();
    }

    public void removeDiceFromRoundTrack(int round, Dice roundTrackDice) {
        roundTrack.removeDiceFromRoundTrack(round, roundTrackDice);
    }

    public void addDiceToRoundTrack(Dice dice, int round) {
        roundTrack.addDiceToRound(dice, round);
    }

    public int getTargetScore() {
        int targetScore = 0;
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            List<Dice> dices = roundTrack.getDices(i);
            for (Dice dice: dices) {
                targetScore += dice.getNumber();
            }
        }
        return targetScore;
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

    public IStateGame getState() {
        return state;
    }

    public static Game newInstance(Game game) {
        return new Game(game.getPlayers(), game.getRoundTrack(), game.getToolCards(), game.getPublicObjectiveCards(),
                game.getDiceBag(), game.getDraftPool(), game.getName(), game.getState(),
                game.isSinglePlayer(), 0);
        //TODO FIX difficulty
    }


}
