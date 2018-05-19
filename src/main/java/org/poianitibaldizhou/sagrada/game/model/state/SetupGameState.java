package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.WrongCardInJsonFileException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupGameState extends IStateGame{

    private static final Logger LOGGER = Logger.getLogger(SetupGameState.class.getName());

    /**
     * Constructor.
     * Create the state, distributes toolCards and PublicObjectiveCards on the table for everyone based on
     * the type of game (singlePlayer(and also the difficulty of the game) or multiPlayer)
     *
     * @param game the current game
     */

     SetupGameState(Game game) {
        super(game);
    }

    @Override
    public void init() {
        DrawableCollection<ToolCard> toolCards = new DrawableCollection<>();
        DrawableCollection<PublicObjectiveCard> publicObjectiveCards = new DrawableCollection<>();

        GameInjector.injectToolCards(toolCards, game.isSinglePlayer());
        try {
            GameInjector.injectPublicObjectiveCards(publicObjectiveCards);
        }catch (WrongCardInJsonFileException e){
            LOGGER.log(Level.SEVERE, "Error in injectPublicObjectiveCards", e);
        }

        game.initDiceBag();
        this.injectToolCards(toolCards);
        this.injectPublicObjectiveCards(publicObjectiveCards);
        readyGame();
    }

    /**
     * Method of the state pattern: changes the state of the game into RoundStartState
     */
    @Override
    public void readyGame() {
        game.setState(new RoundStartState(game, RoundTrack.FIRST_ROUND, getRandomStartPlayer(game.getPlayers())));
    }

    /**
     * Inject the param toolCards into the tool cards of the game
     *
     * @param toolCards the collection of every tool cards
     */
    private void injectToolCards(DrawableCollection<ToolCard> toolCards) {
        int numberOfToolCards = game.getNumberOfToolCardForGame();
        for (int i = 0; i < numberOfToolCards; i++) {
            try {
                game.addToolCard(toolCards.draw());
            } catch (EmptyCollectionException e) {
                LOGGER.log(Level.SEVERE, "Error in injectToolCards for empty collection", e);
            }
        }
    }

    /**
     * Inject the param publicObjectiveCards into the public objective cards of the game
     *
     * @param publicObjectiveCards the collection of every public objective cards
     */
    private void injectPublicObjectiveCards(DrawableCollection<PublicObjectiveCard> publicObjectiveCards) {
        int numberOfPublicObjectiveCards = game.getNumberOfPublicObjectiveCardForGame();
        for (int i = 0; i < numberOfPublicObjectiveCards; i++) {
            try {
                game.addPublicObjectiveCard(publicObjectiveCards.draw());
            } catch (EmptyCollectionException e) {
                LOGGER.log(Level.SEVERE, "Error in injectPublicObjectiveCards for empty collection", e);
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
    private Player getRandomStartPlayer(List<Player> players){
        DrawableCollection<Player> drawablePlayers = new DrawableCollection<>();
        drawablePlayers.addElements(players);
        Player player = null;
        try {
            player = drawablePlayers.draw();
        } catch (EmptyCollectionException e) {
            LOGGER.log(Level.SEVERE, "Error in getRandomStartPlayer for empty collection", e);
        }
        return player;
    }

}
