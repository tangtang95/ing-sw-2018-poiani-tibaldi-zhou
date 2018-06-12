package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.EndTurnAction;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class SelectActionStateTest {

    @Mock
    private TurnState turnState;

    @Mock
    private IActionCommand actionCommand;

    @DataPoint
    public static SelectActionState selectActionState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        selectActionState = new SelectActionState(turnState);
    }

    @Test
    public void chooseAction() {
        selectActionState.chooseAction(actionCommand);
        verify(actionCommand).executeAction(turnState);

    }
}