package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.IPlayerState;

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
    public TurnState(Game game, Player player, boolean isFirstTurn) {
        super(game);
        this.player = player;
    }

    /**
     *
     *
     */
    @Override
    public void nextTurn() {

    }


    /**
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
     * @param player
     * @param toolCard
     */
    @Override
    public void useCard(Player player, ToolCard toolCard) {
        playerState.useCard(player, toolCard);
    }

    /**
     * Pass the operation of placeCard to the playerState
     *
     * @param player
     * @param dice
     */
    @Override
    public void placeDice(Player player, Dice dice) {
        playerState.placeDice(player, dice);
    }
}
