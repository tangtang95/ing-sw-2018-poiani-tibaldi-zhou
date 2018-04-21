package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;

public class RoundStartState extends IStateGame{

    protected RoundStartState(Game game) {
        super(game);
    }

    @Override
    public void throwDices() {
        game.setState(new TurnState(game));
    }

}
