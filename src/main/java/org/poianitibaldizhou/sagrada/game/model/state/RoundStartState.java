package org.poianitibaldizhou.sagrada.game.model.state;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RoundStartState extends IStateGame implements ICurrentRoundPlayer {

    private Player currentRoundPlayer;
    private int currentRound;

    private static final Logger LOGGER = Logger.getLogger(RoundStartState.class.getName());

    /**
     * Constructor.
     * Create the state of RoundStartGame
     *
     * @param game the game to consider
     * @param currentRound the current round of the game
     * @param currentRoundPlayer the player who is the first player of the round
     */
    RoundStartState(Game game, int currentRound, Player currentRoundPlayer) {
        super(game);
        this.currentRoundPlayer = currentRoundPlayer;
        this.currentRound = currentRound;
    }

    /**
     * Inject the number of dices based on the numberOfPlayers or the type of game (single player or multi player) and
     * set the state of the game to the TurnState
     *
     * @param player the player who has called the throw dices (in the view)
     */
    @Override
    public boolean throwDices(Player player) {
        if(currentRoundPlayer.equals(player)) {
            int numberOfDicesToDraw = game.getGameStrategy().getNumberOfDicesToDraw();
            for (int i = 0; i < numberOfDicesToDraw; i++) {
                try {
                    game.getDraftPool().addDice(game.getDiceBag().draw());
                } catch (EmptyCollectionException e) {
                    LOGGER.log(Level.SEVERE, "Error in throwDices for empty collection", e);
                }
            }
            game.setState(new TurnState(game, currentRound, currentRoundPlayer, player,true));
            return true;
        }
        return false;
    }

    @Contract(pure = true)
    public Player getCurrentRoundPlayer(){
        return currentRoundPlayer;
    }

}
