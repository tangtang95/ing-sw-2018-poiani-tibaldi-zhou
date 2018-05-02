package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Direction;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.IPlayerState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.SelectActionState;

public class TurnState extends IStateGame implements IPlayerState {

    private final Player player;
    private IPlayerState playerState;
    private boolean isFirstTurn;

    /**
     * Constructor.
     * Create the TurnState for the player
     *
     * @param game the current game
     * @param player the current player
     */
    TurnState(Game game, Player player, boolean isFirstTurn) {
        super(game);
        this.player = player;
        this.isFirstTurn = isFirstTurn;
        this.playerState = new SelectActionState(this);
    }

    /**
     * This method is called when the player end his turn: if it's the second turn and the player ending the turn
     * is the current player who has thrown the dices then the state goes to RoundEndState, otherwise if it's first
     * turn and the player ending the turn is the last player(the one before the current player of the round) then
     * it starts the second run. Otherwise the turn goes to the next player (if is the first turn than the direction is
     * clockwise otherwise it's counter clockwise)
     *
     */
    @Override
    public void nextTurn() {
        if(!isFirstTurn && player.equals(game.getCurrentPlayerRound()))
            game.setState(new RoundEndState(game));
        int indexLastPlayer = game.getNextIndexOfPlayer(player, Direction.COUNTER_CLOCKWISE);
        if(isFirstTurn && player.equals(game.getPlayers().get(indexLastPlayer)))
            game.setState(new TurnState(game, player, false));
        int indexNextPlayer = game.getNextIndexOfPlayer(player, (isFirstTurn) ? Direction.CLOCKWISE : Direction.COUNTER_CLOCKWISE);
        Player nextPlayer = game.getPlayers().get(indexNextPlayer);
        game.setState(new TurnState(game, nextPlayer, isFirstTurn));
    }


    /**
     * Pass the operation of chooseAction to the playerState
     *
     * @param action the operation of the player
     */
    @Override
    public void chooseAction(String action) {
        playerState.chooseAction(action);
    }

    /**
     * Pass the operation of useCard to the playerState
     *
     * @param player the player who choose to use the card
     * @param toolCard the toolCard to be used
     */
    @Override
    public void useCard(Player player, ToolCard toolCard) {
        playerState.useCard(player, toolCard);
    }

    /**
     * Pass the operation of placeCard to the playerState
     *
     * @param player the player who choose to place a dice
     * @param dice the dice to be placed
     */
    @Override
    public void placeDice(Player player, Dice dice, int row, int column, TileConstraintType tileConstraint,
                          DiceConstraintType diceConstraint) {
        playerState.placeDice(player, dice, row, column, tileConstraint, diceConstraint);
    }

    public void setPlayerState(IPlayerState playerState) {
        this.playerState = playerState;
    }

    public IPlayerState getPlayerState() {
        return playerState;
    }
}
