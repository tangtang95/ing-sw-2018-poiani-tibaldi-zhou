package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;

public class SetupGameState extends IStateGame {

    protected SetupGameState(Game game) {
        super(game);


    }

    @Override
    public void readyGame() {
        game.setState(new RoundStartState(game));
    }

}
