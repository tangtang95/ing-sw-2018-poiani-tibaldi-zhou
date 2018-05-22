package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;

import java.util.HashSet;
import java.util.Set;

public class ResetState extends IStateGame {

    private Set<String> playersReady;

    public ResetState(Game game) {
        super(game);
        playersReady = new HashSet<>();
    }

    @Override
    public void init() {

    }

    @Override
    public void readyGame(String token) {
        playersReady.add(token);
        if(playersReady.size() == game.getNumberOfPlayers())
            game.setState(new SetupPlayerState(game));
    }


}
