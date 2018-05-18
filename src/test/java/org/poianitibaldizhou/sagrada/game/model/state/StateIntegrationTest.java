package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class StateIntegrationTest {

    @DataPoint
    public Game singleGame, multiPlayerGame;

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {
       /* List<String> tokens = new ArrayList<>();
        tokens.add("ABC");
        tokens.add("DEF");
        tokens.add("GHI");
        singleGame = new Game("ABC", 3);
        multiPlayerGame = new Game(tokens, "1Game");
        SchemaCard schemaCard = mock(SchemaCard.class);
        for (Player player : multiPlayerGame.getPlayers()) {
            assertTrue(player.getPrivateObjectiveCards() != null);
            multiPlayerGame.getState().ready(player, schemaCard);
        }
        for (Player player : singleGame.getPlayers()) {
            assertTrue(player.getPrivateObjectiveCards() != null);
            singleGame.getState().ready(player, schemaCard);
        }*/

    }

    @After
    public void tearDown() {
    }

    @Test
    public void setUpGameStateTest() {
        /*
        assertTrue(multiPlayerGame.getPublicObjectiveCards() != null);
        assertTrue(multiPlayerGame.getToolCards() != null);
        assertEquals("Wrong number of publicObjectiveCards", 3,
                multiPlayerGame.getPublicObjectiveCards().size());
        assertEquals("Wrong number of ToolCards", 3,
                multiPlayerGame.getToolCards().size());
        for (PublicObjectiveCard poc : multiPlayerGame.getPublicObjectiveCards()) {
            assertTrue(poc != null);
        }
        for (ToolCard toolCard : multiPlayerGame.getToolCards()) {
            assertTrue(toolCard != null);
        }
        assertTrue(singleGame.getPublicObjectiveCards() != null);
        assertTrue(singleGame.getToolCards() != null);
        assertEquals("Wrong number of publicObjectiveCards", 2,
                singleGame.getPublicObjectiveCards().size());
        assertEquals("Wrong number of ToolCards", singleGame.getDifficulty(),
                singleGame.getToolCards().size());
        for (PublicObjectiveCard poc : singleGame.getPublicObjectiveCards()) {
            assertTrue(poc != null);
        }
        for (ToolCard toolCard : singleGame.getToolCards()) {
            assertTrue(toolCard != null);
        }*/
    }

    @Test
    public void roundStartStateTest() {
        /*
        assertTrue(multiPlayerGame.getState() instanceof RoundStartState);
        assertTrue(singleGame.getState() instanceof RoundStartState);
        multiPlayerGame.getState().throwDices(multiPlayerGame.getCurrentPlayerRound());
        assertEquals("Wrong number of dices in draftPool", 7,
                multiPlayerGame.getDraftPool().size());
        singleGame.getState().throwDices(singleGame.getCurrentPlayerRound());
        assertEquals("Wrong number of dices in draftPool", 4,
                singleGame.getDraftPool().size());*/
    }

    @Test
    public void turnStateTest() {
        /*
        roundStartStateTest();
        TurnState turnState;
        System.out.println(multiPlayerGame.getCurrentPlayerRound().getToken() + " ");
        while (multiPlayerGame.getState() instanceof TurnState) {
            turnState = (TurnState) multiPlayerGame.getState();
            Objects.requireNonNull(turnState).chooseAction("endTurn");
            System.out.println(multiPlayerGame.getCurrentPlayerRound().getToken() + " ");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        multiPlayerGame.getState().nextRound();
        assertEquals("Wrong number of dices in roundTrack round 1", 7,
                multiPlayerGame.getRoundTrack().getDices(1).size());

        while (singleGame.getState() instanceof TurnState) {
            turnState = (TurnState) singleGame.getState();
            Objects.requireNonNull(turnState).chooseAction("endTurn");
        }
        singleGame.getState().nextRound();
        assertEquals("Wrong number of dices in roundTrack round 1", 7,
                singleGame.getRoundTrack().getDices(1).size());
        assertTrue(multiPlayerGame.getState() instanceof RoundStartState);
        assertTrue(singleGame.getState() instanceof RoundStartState);*/
    }
}
