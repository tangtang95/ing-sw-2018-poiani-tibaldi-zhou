package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: Represents the setup state of the game.
 */
public class SetupGameState extends IStateGame {

    private static final Logger LOGGER = Logger.getLogger(SetupGameState.class.getName());

    /**
     * Constructor.
     * Create the state, distributes toolCards and PublicObjectiveCards on the table for everyone based on
     * the type of game (singlePlayer(and also the difficulty of the game) or multiPlayer)
     *
     * @param game the current game
     */

    public SetupGameState(Game game) {
        super(game);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        game.getStateObservers().forEach((key, value) -> value.onSetupGame());

        DrawableCollection<ToolCard> toolCards = new DrawableCollection<>();
        DrawableCollection<PublicObjectiveCard> publicObjectiveCards = new DrawableCollection<>();

        GameInjector.injectToolCards(toolCards);
        GameInjector.injectPublicObjectiveCards(publicObjectiveCards);

        game.initDiceBag();
        this.injectToolCards(toolCards);
        this.injectPublicObjectiveCards(publicObjectiveCards);

        game.getGameObservers().forEach((key, value) -> {
            value.onToolCardsDraw(game.getToolCards());
            value.onPublicObjectiveCardsDraw(game.getPublicObjectiveCards());
        });

        game.setState(new RoundStartState(game, RoundTrack.FIRST_ROUND, getRandomStartPlayer(game.getPlayerListReferences())));
    }

    /**
     * Inject the param toolCards into the tool cards of the game
     *
     * @param toolCards the collection of every tool cards
     */
    private void injectToolCards(DrawableCollection<ToolCard> toolCards) {
        for (int i = 0; i < game.getNumberOfToolCardForGame(); i++) {
            try {
                game.addToolCard(toolCards.draw());
            } catch (EmptyCollectionException e) {
                LOGGER.log(Level.SEVERE, ServerMessage.EMPTY_COLLECTION_ERROR, e);
            }
        }
    }

    /**
     * Inject the param publicObjectiveCards into the public objective cards of the game
     *
     * @param publicObjectiveCards the collection of every public objective cards
     */
    private void injectPublicObjectiveCards(DrawableCollection<PublicObjectiveCard> publicObjectiveCards) {
        for (int i = 0; i < game.getNumberOfPublicObjectiveCardForGame(); i++) {
            try {
                game.addPublicObjectiveCard(publicObjectiveCards.draw());
            } catch (EmptyCollectionException e) {
                LOGGER.log(Level.SEVERE, ServerMessage.EMPTY_COLLECTION_ERROR, e);
            }
        }
    }

    /**
     * When the game starts, it should choose a random player from where to start the game so this method
     * return
     *
     * @param players all the players of the game
     * @return return a random player from the list of players given by parameter
     */
    private Player getRandomStartPlayer(List<Player> players) {
        DrawableCollection<Player> drawablePlayers = new DrawableCollection<>();
        drawablePlayers.addElements(players);
        Player player = null;
        try {
            player = drawablePlayers.draw();
        } catch (EmptyCollectionException e) {
            LOGGER.log(Level.SEVERE, ServerMessage.EMPTY_COLLECTION_ERROR, e);
        }
        return player;
    }

}
