package org.poianitibaldizhou.sagrada.game.model.state;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupPlayerState extends IStateGame {

    private Set<Player> playersReady;
    private Map<Player, List<SchemaCard>> playerSchemaCards;

    public static final int NUMBER_OF_SCHEMA_CARDS_PER_PLAYERS = 2;

    private static final Logger LOGGER = Logger.getLogger(SetupPlayerState.class.getName());

    /**
     * Constructor.
     * Create the state, give one PrivateObjectiveCard and NUMBER_OF_SCHEMA_CARDS_PER_PLAYERS SchemaCards for
     * each players and notify this to the players
     *
     * @param game the current game
     */
    public SetupPlayerState(Game game) {
        super(game);
        playersReady = new HashSet<>();
        playerSchemaCards = new HashMap<>();

        DrawableCollection<PrivateObjectiveCard> privateObjectiveCards = new DrawableCollection<>();
        DrawableCollection<SchemaCard> schemaCards = new DrawableCollection<>();

        GameInjector.injectPrivateObjectiveCard(privateObjectiveCards);
        GameInjector.injectSchemaCards(schemaCards);
        for (Player player : game.getPlayers()) {
            List<SchemaCard> schemaCardList = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_SCHEMA_CARDS_PER_PLAYERS; i++) {
                try {
                    schemaCardList.add(schemaCards.draw());
                } catch (EmptyCollectionException e) {
                    LOGGER.log(Level.SEVERE, "Error in SetupPlayerState for empty collection", e);
                }
            }
            playerSchemaCards.put(player, schemaCardList);
            game.setPrivateObjectiveCards(player, privateObjectiveCards);
        }
        //TODO notify each player for the schemaCard
    }

    /**
     * Copy_constructor
     *
     * @param playerState playerState to copy
     */
    private SetupPlayerState(SetupPlayerState playerState) {
        super(playerState.game);
        playersReady = new HashSet<>();
        playerSchemaCards = new HashMap<>();
        List<SchemaCard> schemaCardList = new ArrayList<>();

        for (Player player : playerState.playerSchemaCards.keySet()) {
            for (SchemaCard schemaCard : playerState.playerSchemaCards.get(player))
                schemaCardList.add(SchemaCard.newInstance(schemaCard));
            this.playerSchemaCards.put(Player.newInstance(player), schemaCardList);
        }

        for (Player player : playerState.playersReady)
            this.playersReady.add(Player.newInstance(player));

    }

    /**
     * Method of the state pattern: When the player have finished to select the schemaCard,
     * this method is invoked to set the SchemaCard to the player and when every player has readied the state
     *
     * @param player     the player who have selected the schemaCard
     * @param schemaCard the schemaCard chosen by the player
     */
    @Override
    public boolean ready(Player player, SchemaCard schemaCard) {
        if (!isPlayerReady(player) && player.getSchemaCard() == null && containsSchemaCard(player, schemaCard)) {
            playersReady.add(player);
            game.setPlayerSchemaCard(player, schemaCard);
            if (game.getNumberOfPlayers() == playersReady.size()) {
                game.setState(new SetupGameState(game));
                game.readyGame();
            }
            return true;
        }
        return false;
    }

    @Contract(pure = true)
    public boolean isPlayerReady(Player player) {
        for (Player playerReady : playersReady) {
            if (playerReady.equals(player))
                return true;
        }
        return false;
    }

    @Contract(pure = true)
    public boolean containsSchemaCard(Player player, SchemaCard schemaCard) {
        return playerSchemaCards.get(player).contains(schemaCard);
    }

    @Contract(pure = true)
    public List<SchemaCard> getSchemaCardsOfPlayer(Player player) {
        List<SchemaCard> schemaCards = new ArrayList<>();
        for (SchemaCard schema : playerSchemaCards.get(player)) {
            schemaCards.add(SchemaCard.newInstance(schema));
        }
        return schemaCards;
    }

    public static IStateGame newInstance(IStateGame sps) {
        if (sps == null)
            return null;
        return new SetupPlayerState((SetupPlayerState) sps);
    }
}
