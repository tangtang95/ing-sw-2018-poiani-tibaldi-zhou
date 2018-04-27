package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;

public class RoundStartState extends IStateGame{

    public static final int NUMBER_OF_DICES_TO_DRAW_FOR_SINGLE_PLAYER = 4;

    /**
     * Constructor.
     * Create the state of RoundStartGame
     *
     * @param game
     */
    public RoundStartState(Game game) {
        super(game);
    }

    /**
     * Inject the number of dices based on the numberOfPlayers or the type of game (single player or multi player) and
     * set the state of the game to the TurnState
     *
     * @param player the player who has called the throw dices (in the view)
     */
    @Override
    public void throwDices(Player player) {
        if(game.getCurrentPlayerRound().equals(player)) {
            int numberOfDicesToDraw = (game.isSinglePlayer()) ?
                    NUMBER_OF_DICES_TO_DRAW_FOR_SINGLE_PLAYER : game.getNumberOfPlayers()*2 + 1;
            for (int i = 0; i < numberOfDicesToDraw; i++) {
                try {
                    game.getDraftPool().addDice(game.getDiceBag().draw());
                } catch (EmptyCollectionException e) {
                    e.printStackTrace();
                }
            }
            game.setState(new TurnState(game, player,true));
        }
    }

}
