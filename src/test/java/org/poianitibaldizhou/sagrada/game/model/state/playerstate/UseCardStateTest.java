package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;


import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UseCardStateTest {

    @Mock
    private Player player;

    @Mock
    private ToolCard toolCard;

    @Mock
    private TurnState turnState;

    @DataPoint
    public static UseCardState useCardState;

    @Test
    public void useCard() {
        useCardState.useCard(player,toolCard);
        verify(player).isCardUsable(toolCard);
        assertEquals(true, useCardState.useCard(player,toolCard));
    }

    @Test
    public void releaseToolCardExecution() {
        useCardState.releaseToolCardExecution();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(player.isCardUsable(toolCard)).thenReturn(true);

        useCardState = new UseCardState(turnState);
    }

}