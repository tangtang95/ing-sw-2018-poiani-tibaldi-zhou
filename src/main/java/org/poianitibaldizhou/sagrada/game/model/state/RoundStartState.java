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
        this.currentRoundPlayer = Player.newInstance(currentRoundPlayer);
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
            game.addDicesToDraftPoolFromDiceBag();
            game.setState(new TurnState(game, currentRound, currentRoundPlayer, player,true));
            return true;
        }
        return false;
    }

    @Contract(pure = true)
    public Player getCurrentRoundPlayer(){
        return currentRoundPlayer;
    }

    public static IStateGame newInstance(IStateGame rss) {
        if (rss == null)
            return null;
        return new RoundStartState(rss.game, ((RoundStartState)(rss)).currentRound,
                ((RoundStartState)(rss)).currentRoundPlayer);
    }
}
