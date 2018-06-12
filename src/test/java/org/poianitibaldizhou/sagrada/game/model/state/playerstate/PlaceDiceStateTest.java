package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class PlaceDiceStateTest {

    @Mock
    private TurnState turnState;

    @Mock
    private Player player;

    @DataPoint
    public static PlaceDiceState placeDiceState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        doNothing().when(turnState).notifyOnPlaceDiceState();
        placeDiceState = new PlaceDiceState(turnState);
    }

    @Test
    public void placeDice() {
        MockitoAnnotations.initMocks(this);

        Dice dice = new Dice(3, Color.GREEN);
        Position position = new Position(1,1);

        try {
            placeDiceState.placeDice(player,dice ,position );
            verify(player).placeDice(dice,position);
        } catch (RuleViolationException e) {
            e.printStackTrace();
        }
    }
}