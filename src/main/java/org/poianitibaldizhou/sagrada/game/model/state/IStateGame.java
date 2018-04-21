package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;

public abstract class IStateGame {

    protected Game game;

    protected IStateGame(Game game){
        this.game = game;
    }

    public void nextRound(){

    }
    public void ready(){

    }
    public void throwDices(){

    }
    public void nextTurn(){

    }
}
