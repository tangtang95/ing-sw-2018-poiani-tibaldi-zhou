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

    /**
     * Constructor.
     * Create the state, give one PrivateObjectiveCard and NUMBER_OF_SCHEMA_CARDS_PER_PLAYERS SchemaCards for
     * each players and notify this to the players
     *
     * @param game the current game
     */
    public SetupPlayerState(Game game) {
        super(game);
        playersReady = new TreeSet<>();
        playerSchemaCards = new HashMap<>();

        DrawableCollection<PrivateObjectiveCard> privateObjectiveCards = new DrawableCollection<>();
        DrawableCollection<SchemaCard> schemaCards = new DrawableCollection<>();

        GameInjector gameInjector = new GameInjector();
        gameInjector.injectPrivateObjectiveCard(privateObjectiveCards);
        gameInjector.injectSchemaCards(schemaCards);
        for (Player player : game.getPlayers()) {
            List<SchemaCard> schemaCardList = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_SCHEMA_CARDS_PER_PLAYERS; i++) {
                try {
                    schemaCardList.add(schemaCards.draw());
                } catch (EmptyCollectionException e) {
                    e.printStackTrace();
                }
            }
            try {
                playerSchemaCards.put(player, schemaCardList);
                player.setPrivateObjectiveCard(privateObjectiveCards.draw());
            } catch (EmptyCollectionException e) {
                e.printStackTrace();
            }
        }
        //TODO notify each player
    }

    /**
     * Method of the state pattern: When the player have finished to select the schemaCard,
     * this method is invoked to set the SchemaCard to the player and when every player has readied the state
     *
     * @param player     the player who have selected the schemaCard
     * @param schemaCard the schemaCard chosen by the player
     */
    @Override
    public void ready(Player player, SchemaCard schemaCard) {
        playersReady.add(player);
        if (player.getSchemaCard() == null && playerSchemaCards.get(player).contains(schemaCard))
            player.setSchemaCard(schemaCard);
        if (game.getNumberOfPlayers() == playersReady.size())
            game.setState(new SetupGameState(game));
    }

}
