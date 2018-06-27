package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

/**
 * OVERVIEW: Represents the state of forcing the end of the game. This may happen, for example,
 * when there is only one player left in the game and the others are disconnected.
 */
public class ForcedEndGameState extends IStateGame {

    private Player winner;

    /**
     * Constructor.
     * Creates a new state for forcing the end of the game.
     *
     * @param game game ended
     * @param winner winner of the game
     */
    public ForcedEndGameState(Game game, Player winner) {
        super(game);
        this.winner = winner;
    }

    /**
     * {@inheritDoc}
     * Notifies to the observers the winner of the game.
     */
    @Override
    public void init() {
        game.getStateObservers().forEach((key, value) -> value.onResultGame(winner.getUser()));
    }
}
