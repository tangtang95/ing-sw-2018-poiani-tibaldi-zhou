package org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.UseCardState;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class UseCardActionTest {

    @DataPoint
    public static UseCardAction useCardAction;

    @Mock
    private TurnState turnState;

    @Test
    public void executeAction() {
        useCardAction.executeAction(turnState);
        verify(turnState).setPlayerState(any(UseCardState.class));
    }

    @Test
    public void toJSON() {
        assertNull(useCardAction.toJSON());
    }

    @Test
    public void toObject() {
        UseCardAction.toObject();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        useCardAction = new UseCardAction();
    }
}