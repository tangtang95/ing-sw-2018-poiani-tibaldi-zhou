package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

public abstract class IStateGame {

    protected Game game;

    protected IStateGame(Game game) {
        this.game = game;
    }

    /**
     * Initialize the state
     *
     */
    public abstract void init();

    /**
     * Go to the next round of the game (it should go to the next roundStartState if implemented)
     *
     */
    public void nextRound() {
        throw new IllegalStateException("SEVERE ERROR: method not implemented in this state");
    }

    /**
     * Set the schemaCard given to the player bound to the token given
     *
     * @param token      the token of the player who choose the schemaCard
     * @param schemaCard the schemaCard chosen
     * @throws InvalidActionException if the state instance != SetupPlayerState || the set schemaCard is unsuccessful
     */
    public void ready(String token, SchemaCard schemaCard) throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * Join the game for the specific player with the token given when the state of the game is at ResetState
     *
     * @param token the token of the player who has joined the game
     * @throws InvalidActionException if the token is non-existent in the game ||
     *                                the player has already readied for the game
     */
    public void readyGame(String token) throws InvalidActionException {
        throw new IllegalStateException("SEVERE ERROR: method not implemented in this state");
    }

    /**
     * Throw the dices of the diceBag into the draftPool
     *
     * @param player the player who has the diceBag
     * @throws InvalidActionException if the state instance != RoundStartState || the throw of the dices is unsuccessful
     */
    public void throwDices(Player player) throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * Set the privateObjectiveCard given to the player (only in the singlePlayer)
     *
     * @param player               the player who wants to set the privateObjectiveCard
     * @param privateObjectiveCard the privateObjectiveCard chosen
     * @throws InvalidActionException if the state instance != EndGameState ||
     *                                the set of privateObjectiveCard is unsuccessful
     */
    public void choosePrivateObjectiveCard(Player player, PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * Calculate the victory points of every player in the game
     *
     */
    public void calculateVictoryPoints() {
        throw new IllegalStateException("SEVERE ERROR: method not implemented in this state");
    }

    /**
     * Change the player state inside the turnState based on the IActionCommand
     *
     * @param player the player who wants to change his state
     * @param action the action of the player
     * @throws InvalidActionException if the state instance != TurnState || it's not the "player" turn
     */
    public void chooseAction(Player player, IActionCommand action) throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * Place the dice on the schemaCard of the player given
     *
     * @param player   the player that wants to place the dice
     * @param dice     the dice to be placed on the schemaCard
     * @param position the position to place
     * @throws InvalidActionException if the state instance != TurnState ||
     *                                the placement of the dice is unsuccessful and inside the InvalidActionException
     *                                there is a RuleViolationException
     */
    public void placeDice(Player player, Dice dice, Position position) throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * Activate the commands of the toolCard and pass the observer to the ToolCardExecutor
     *
     * @param player   the player who wants to use the toolCard
     * @param toolCard the toolCard to use
     * @param observer the observer of the player given
     * @throws InvalidActionException if the state instance != TurnState ||
     *                                the player given is not the player turn ||
     *                                there are no coins to spend
     */
    public void useCard(Player player, ToolCard toolCard, IToolCardExecutorObserver observer) throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * Send the object inside the ExecutorEvent to the ToolCardExecutor
     *
     * @param event the event of message obtained from the player
     * @throws InvalidActionException if the state instance != TurnState
     */
    public void fireExecutorEvent(ExecutorEvent event) throws InvalidActionException {
        throw new InvalidActionException();
    }

    /**
     * Interrupt the toolCard Execution by the player action or by the timeout
     *
     * @throws InvalidActionException if the state instance != TurnState
     */
    public void interruptToolCardExecution(Player player) throws InvalidActionException {
        throw new InvalidActionException();
    }


}
