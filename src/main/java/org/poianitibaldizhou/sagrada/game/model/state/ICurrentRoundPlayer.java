package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.players.Player;

/**
 * OVERVIEW: Implemented by the states that regards a round (so the setup, reset and the termination are not included)
 */
public interface ICurrentRoundPlayer {

    /**
     * @return the current round player, the one who has the diceBag
     */
    Player getCurrentRoundPlayer();
}
