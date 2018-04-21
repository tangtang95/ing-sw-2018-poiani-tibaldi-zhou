package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

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

    public Game(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
        this.players = new LinkedList<>();
        this.toolCards= new LinkedList<>();
        this.publicObjectiveCards = new LinkedList<>();
        this.diceBag = new DrawableCollection();
        this.roundTrack = new RoundTrack();
        this.draftPool = new DraftPool();
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

}
