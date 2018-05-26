package org.poianitibaldizhou.sagrada.game.model.state;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.observers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.IStateObserver;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupPlayerState extends IStateGame {

    private Set<String> playersReady;
    private Map<String, List<List<SchemaCard>>> playerSchemaCards;
    private Map<String, List<PrivateObjectiveCard>> privateObjectiveCardMap;

    private static final int NUMBER_OF_SCHEMA_CARDS_PER_PLAYERS = 2;

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
        privateObjectiveCardMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        game.getStateObservers().forEach((key, value) -> {
            try {
                value.onSetupPlayer();
            } catch (RemoteException e) {
                game.getStateObservers().remove(key);
            }
        });

        DrawableCollection<PrivateObjectiveCard> privateObjectiveCards = new DrawableCollection<>();
        DrawableCollection<List<SchemaCard>> schemaCards = new DrawableCollection<>();

        GameInjector.injectPrivateObjectiveCard(privateObjectiveCards);
        GameInjector.injectSchemaCards(schemaCards);

        for (String token : game.getUserToken()) {
            List<List<SchemaCard>> schemaCardList = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_SCHEMA_CARDS_PER_PLAYERS; i++) {
                try {
                    schemaCardList.add(schemaCards.draw());
                } catch (EmptyCollectionException e) {
                    LOGGER.log(Level.SEVERE, "Error for empty collection", e);
                }
            }
            playerSchemaCards.put(token, schemaCardList);
            try {
                game.getGameObservers().get(token).onSchemaCardsDraw(schemaCardList);
            } catch (RemoteException e) {
                game.getGameObservers().remove(token);
            }

            int numberOfPrivateObjectiveCard = game.getNumberOfPrivateObjectiveCardForGame();
            List<PrivateObjectiveCard> privateObjectiveCardList = new ArrayList<>();
            for (int i = 0; i < numberOfPrivateObjectiveCard; i++) {
                try {
                    privateObjectiveCardList.add(privateObjectiveCards.draw());
                } catch (EmptyCollectionException e) {
                    LOGGER.log(Level.SEVERE, "Error for empty collection", e);
                }
            }
            privateObjectiveCardMap.put(token, privateObjectiveCardList);
            if (!game.getGameObservers().containsKey(token))
                throw new IllegalStateException("SEVERE ERROR: cannot find token");
            try {
                game.getGameObservers().get(token).onPrivateObjectiveCardDraw(privateObjectiveCardList);
            } catch (RemoteException e) {
                game.getGameObservers().remove(token);
            }
        }

    }


    /**
     * {@inheritDoc}
     * <p>
     * When the player have finished to select the schemaCard, this method is invoked to set the SchemaCard
     * to the player and when every player has readied the state
     *
     * @param token      the token of the player who have selected the schemaCard
     * @param schemaCard the schemaCard chosen by the player
     * @throws InvalidActionException if if the player has already readied before ||
     *                                the schemaCard given is the wrong one
     */
    @Override
    public void ready(String token, SchemaCard schemaCard) throws InvalidActionException {
        if (!isPlayerReady(token) && containsSchemaCard(token, schemaCard)) {
            playersReady.add(token);
            game.setPlayerSchemaCard(token, schemaCard, privateObjectiveCardMap.get(token));
            if (game.getNumberOfPlayers() == playersReady.size()) {
                game.getGameObservers().forEach((key, value) -> {
                    try {
                        value.onPlayersCreate(game.getPlayers());
                    } catch (RemoteException e) {
                        game.getGameObservers().remove(key);
                    }
                });
                game.setState(new SetupGameState(game));
            }
            return;
        }
        throw new InvalidActionException();
    }

    @Contract(pure = true)
    public boolean isPlayerReady(String token) {
        return playersReady.contains(token);
    }

    @Contract(pure = true)
    public boolean containsSchemaCard(String token, SchemaCard schemaCard) {
        for (List<SchemaCard> schema : playerSchemaCards.get(token))
            if (schema.contains(schemaCard))
                return true;
        return false;
    }

    @Contract(pure = true)
    public List<List<SchemaCard>> getSchemaCardsOfPlayer(String token) {
        List<List<SchemaCard>> schemaCards = new ArrayList<>();
        for (List<SchemaCard> schema : playerSchemaCards.get(token)) {
            List<SchemaCard> copySchema = new ArrayList<>();
            for (SchemaCard schemaCard : schema)
                copySchema.add(SchemaCard.newInstance(schemaCard));
            schemaCards.add(copySchema);
        }
        return schemaCards;
    }

}
