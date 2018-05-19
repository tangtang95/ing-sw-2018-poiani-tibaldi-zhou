package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.givenToolCard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Node;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration test without Game and Player
 */
public class MartellettoTest {

    @Mock
    private Game game;
    @Mock
    private Player player1, player2;

    private ToolCard toolCard;

    private TurnState turnState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        toolCard = new ToolCard(Color.BLUE, "Martelletto",
                "Tira nuovamente tutti i dadi della riserva. Questa carta può essere usata solo durante" +
                        " il tuo secondo turno, prima di scegliere il secondo dado"
                ,"[1-Check second turn][2-Check before choose dice][4-Reroll DraftPool][8-CA]",false);

    }

    @After
    public void tearDown() throws Exception {
        toolCard = null;
        game = null;
        player1 = null;
        player2 = null;
        turnState = null;
    }

    @Test
    public void checkMainFlowTest() throws Exception {
        // TODO fix with new executor
        turnState = new TurnState(game, 0, player1, player1, false);
        turnState.init();

        when(game.getState()).thenReturn(turnState);
        Node<ICommand> rootCommand = toolCard.useCard();
        ToolCardExecutor executor = new ToolCardExecutor(rootCommand, player1, game);
        executor.start();
        executor.join();
        //verify(game).reRollDraftPool();
    }

    @Test
    public void checkSubFlowTest() throws Exception {
        // TODO
    }
}
