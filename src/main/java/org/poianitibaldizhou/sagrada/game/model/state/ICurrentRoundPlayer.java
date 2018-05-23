package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Player;

public interface ICurrentRoundPlayer {

    /**
     * @return the current round player, the one who has the diceBag
     */
    Player getCurrentRoundPlayer();
}
