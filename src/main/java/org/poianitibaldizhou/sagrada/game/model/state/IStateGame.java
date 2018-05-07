package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;

public abstract class IStateGame {

    protected Game game;

    protected IStateGame(Game game){
        this.game = game;
    }

    public void nextRound(){
        throw new IllegalStateException();
    }
    public void ready(Player player, SchemaCard schemaCard){
        throw new IllegalStateException();
    }
    public void readyGame(){
        throw new IllegalStateException();
    }
    public void throwDices(Player player){ throw new IllegalStateException(); }
    public void nextTurn(){
        throw new IllegalStateException();
    }
}
