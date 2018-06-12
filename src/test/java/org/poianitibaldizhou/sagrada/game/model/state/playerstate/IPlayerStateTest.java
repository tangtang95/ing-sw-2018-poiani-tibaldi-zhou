package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.UseCardAction;

public class IPlayerStateTest {

    @Mock
    private TurnState turnState;

    @Mock
    private Player player;

    @Mock
    private ToolCard toolCard;

    @DataPoint
    public static IPlayerState playerState1, playerState2;

    @Before
    public void setUp() throws Exception {
        playerState1 = new EndTurnState(turnState);
        playerState2 = new SelectActionState(turnState);
    }

    @Test(expected = Exception.class)
    public void chooseAction() throws InvalidActionException {
        playerState1.chooseAction(new UseCardAction());
    }

    @Test(expected = Exception.class)
    public void useCard() throws InvalidActionException {
        playerState1.useCard(player,toolCard);
    }

    @Test(expected = Exception.class)
    public void placeDice() throws InvalidActionException, RuleViolationException {
        playerState1.placeDice(player, new Dice(2, Color.GREEN), new Position(1,1));
    }

    @Test(expected = Exception.class)
    public void endTurn() throws InvalidActionException {
        playerState2.endTurn();
    }
}