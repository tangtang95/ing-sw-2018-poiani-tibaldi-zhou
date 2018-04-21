package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;

public class SetupGameState extends IStateGame {

    protected SetupGameState(Game game) {
        super(game);

        GameInjector gameInjector = new GameInjector();
        gameInjector.injectToolCards(game.getToolCards(), game.isSinglePlayer(), game.getDifficulty());
        gameInjector.injectPublicObjectiveCards(game.getPublicObjectiveCards());
        gameInjector.injectDiceBag(game.getDiceBag());
    }

    @Override
    public void readyGame() {
        game.setState(new RoundStartState(game));
    }

}
