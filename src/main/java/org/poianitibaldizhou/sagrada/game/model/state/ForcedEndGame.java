package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

public class ForcedEndGame extends IStateGame {

    private Player winner;

    public ForcedEndGame(Game game, Player winner) {
        super(game);
        this.winner = winner;
    }

    @Override
    public void init() {
        game.getStateObservers().forEach((key, value) -> value.onResultGame(winner.getUser()));
    }
}
