package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;

import java.util.*;

public class SetupPlayerState extends IStateGame {

    private Set<Player> playersReady;
    private Map<Player, List<SchemaCard>> playerSchemaCards;

    public final static int NUMBER_OF_SCHEMA_CARDS_PER_PLAYERS = 2;

    public SetupPlayerState(Game game) {
        super(game);
        playersReady = new TreeSet<>();
        playerSchemaCards = new HashMap<>();
        GameInjector gameInjector = new GameInjector();

        DrawableCollection<PrivateObjectiveCard> privateObjectiveCards = new DrawableCollection<>();
        DrawableCollection<SchemaCard> schemaCards = new DrawableCollection<>();
        gameInjector.injectPrivateObjectiveCard(privateObjectiveCards);
        gameInjector.injectSchemaCards(schemaCards);
        for (Player player: game.getPlayers()) {
            try {
                playerSchemaCards.put(player, new LinkedList<SchemaCard>());
                player.setPrivateObjectiveCard(privateObjectiveCards.draw());
            } catch (EmptyCollectionException e) {
                e.printStackTrace();
            }
        }
        //TODO notify each player
    }

    @Override
    public void ready(Player player, SchemaCard schemaCard) {
        playersReady.add(player);
        if(player.getSchemaCard() == null && playerSchemaCards.get(player).contains(schemaCard))
            player.setSchemaCard(schemaCard);
        if (game.getNumberOfPlayers() == playersReady.size())
            game.setState(new SetupGameState(game));
    }

}
