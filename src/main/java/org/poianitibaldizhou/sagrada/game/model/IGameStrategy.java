package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.List;
import java.util.Map;

public interface IGameStrategy {

    /**
     * @return the number of tool cards needed for the game
     */
    int getNumberOfToolCardForGame();

    /**
     * @return the number of public objective cards needed for the game
     */
    int getNumberOfPublicObjectiveCardForGame();

    /**
     * @return the number of dices to draw every round
     */
    int getNumberOfDicesToDraw();

    /**
     * @return the number of private objective cards needed for each player
     */
    int getNumberOfPrivateObjectiveCardForGame();

    /**
     * Set the outcome of each player based on their scores
     *
     * @param scoreMap           the map of the score of each player
     * @param currentRoundPlayer the current round player who has the diceBag
     */
    void setPlayersOutcome(Map<Player, Integer> scoreMap, Player currentRoundPlayer);

    /**
     * @return true if the game is singlePlayerGame, otherwise false
     */
    boolean isSinglePlayer();

    /**
     * Add a new player to the map of players in the game
     *
     * @param user                  the user
     * @param schemaCard            the schemaCard of the user
     * @param privateObjectiveCards the privateObjectiveCards of the player
     */
    void addNewPlayer(User user, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards);

    /**
     * Based on the game handle the endGame differently
     *
     */
    void handleEndGame();

    /**
     * Get the list of pre-commands for the specified toolCard
     *
     * @param toolCard the specified toolCard
     * @return the list of pre-commands
     */
    Node<ICommand> getPreCommands(ToolCard toolCard);

}
