package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.SetupPlayerState;

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

    private Player currentPlayerRound;
    private IStateGame state;
    private int difficulty;


    /**
     * Constructor for Multi player.
     * Create the Game with all the attributes initialized, create also all the player from the given tokens and
     * set the state to SetupPlayerState
     *
     * @param tokens the list of String token of the players
     */
    public Game(List<String> tokens) {
        this.isSinglePlayer = false;
        this.players = new LinkedList<>();
        this.diceBag = new DrawableCollection<>();
        this.toolCards = new LinkedList<>();
        this.publicObjectiveCards = new LinkedList<>();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();

        for (String token : tokens) {
            players.add(new Player(token, isSinglePlayer ? new ExpendableDice(draftPool) : new FavorToken()));
            setState(new SetupPlayerState(this));
        }
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
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

    public int getCurrentRound() {
        return roundTrack.getCurrentRound();
    }

    public int getNumberOfRounds() {
        return RoundTrack.NUMBER_OF_TRACK;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public int getDifficulty() {
        if(!isSinglePlayer)
            throw new IllegalArgumentException("shouldn't call this method if it is a multi player game");
        return difficulty;
    }

    public DrawableCollection<Dice> getDiceBag() {
        return diceBag;
    }

    /**
     * Set the turn of the player to the passed playerTurn as parameter
     *
     * @param player the player
     */
    public void setCurrentPlayerRound(Player player) {
        if(getIndexOfPlayer(player) != -1)
            currentPlayerRound = player;
    }

    /**
     * Return the Player who has the control of the round game (who has the diceBag)
     *
     * @return the player who has the control of the round game
     */
    public Player getCurrentPlayerRound() {
        return currentPlayerRound;
    }

    /**
     * Rotate the DiceBag to another player in clockwise direction
     */
    public void rotateCurrentPlayerRound() {
        Player newCurrentPlayer = players.get((getIndexOfPlayer(getCurrentPlayerRound()) + 1) % players.size());
        setCurrentPlayerRound(newCurrentPlayer);
    }

    /**
     * Return the index of the list of players about the given player
     *
     * @param player the player to find in the list of players
     * @return the index of the list of players about the given player
     */
    public int getIndexOfPlayer(Player player){
        int indexOfCurrentPlayer = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player))
                indexOfCurrentPlayer = i;
        }
        if (indexOfCurrentPlayer == -1)
            throw new IllegalArgumentException("cannot find the current player in the list of players");
        return indexOfCurrentPlayer;
    }
}
