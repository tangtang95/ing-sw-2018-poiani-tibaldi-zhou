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

    }
    public void ready(Player player, SchemaCard schemaCard){

    }
    public void readyGame(){

    }
    public void throwDices(){

    }
    public void nextTurn(){

    }
}
