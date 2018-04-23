package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.SetupPlayerState;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private final boolean isSinglePlayer;
    private List<Player> players;
    private RoundTrack roundTrack;
    private List<ToolCard> toolCards;
    private List<PublicObjectiveCard> publicObjectiveCards;
    private DrawableCollection<Dice> diceBag;
    private DraftPool draftPool;
    private IStateGame state;
    private int actualRound;


    public Game(List<String> tokens, boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
        this.players = new LinkedList<>();
        this.toolCards= new LinkedList<>();
        this.publicObjectiveCards = new LinkedList<>();
        this.diceBag = new DrawableCollection<>();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();
        this.actualRound = 0;

        for (String token: tokens) {
            players.add(new Player(token));
        }
        setState(new SetupPlayerState(this));
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

    public int getNumberOfActualRound() {
        return actualRound;
    }

    public int getNumberOfRounds() {
        return RoundTrack.NUMBER_OF_TRACK;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public int getDifficulty() {
        //TODO
        return 0;
    }

    public DrawableCollection<Dice> getDiceBag() {
        return diceBag;
    }
}
