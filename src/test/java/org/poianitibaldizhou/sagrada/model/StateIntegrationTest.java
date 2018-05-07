package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.RoundStartState;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

public class StateIntegrationTest {

    @DataPoint
    public static Game singleGame, multiPlayerGame;

    @BeforeClass
    public static void setUpClass() {
        List<String> tokens = new ArrayList<>();
        tokens.add("ABC");
        tokens.add("DEF");
        tokens.add("GHI");
        singleGame = new Game("ABC",3);
        multiPlayerGame = new Game(tokens, "1Game");
        SchemaCard schemaCard = mock(SchemaCard.class);
        for (Player player : multiPlayerGame.getPlayers()) {
            assert (player.getPrivateObjectiveCard() != null);
            multiPlayerGame.getState().ready(player,schemaCard);
        }
        for (Player player : singleGame.getPlayers()) {
            assert (player.getPrivateObjectiveCard() != null);
            singleGame.getState().ready(player,schemaCard);
        }
        singleGame.getState().readyGame();
        multiPlayerGame.getState().readyGame();
    }

    @AfterClass
    public static void tearDownClass(){

    }

    @Before
    public void setUp(){

    }

    @After
    public void tearDown() {
    }

    @Test
    public void setUpGameStateTest(){
        assert (multiPlayerGame.getPublicObjectiveCards() != null);
        assert (multiPlayerGame.getToolCards() != null);
        assertEquals("Wrong number of publicObjectiveCards", 3,
                multiPlayerGame.getPublicObjectiveCards().size());
        assertEquals("Wrong number of ToolCards", 3,
                multiPlayerGame.getToolCards().size());
        for (PublicObjectiveCard poc : multiPlayerGame.getPublicObjectiveCards()) {
            assert (poc != null);
        }
        for (ToolCard toolCard : multiPlayerGame.getToolCards()) {
            assert (toolCard != null);
        }
        assert (singleGame.getPublicObjectiveCards() != null);
        assert (singleGame.getToolCards() != null);
        assertEquals("Wrong number of publicObjectiveCards", 2,
                singleGame.getPublicObjectiveCards().size());
        assertEquals("Wrong number of ToolCards", singleGame.getDifficulty(),
                singleGame.getToolCards().size());
        for (PublicObjectiveCard poc : singleGame.getPublicObjectiveCards()) {
            assert (poc != null);
        }
        for (ToolCard toolCard : singleGame.getToolCards()) {
            assert (toolCard != null);
        }
    }

    @Test
    public void roundStartStateTest(){
        assert (multiPlayerGame.getState() instanceof RoundStartState);
        assert (singleGame.getState() instanceof RoundStartState);
        multiPlayerGame.getState().throwDices(multiPlayerGame.getCurrentPlayerRound());
        assertEquals("Wrong number of dices in draftPool",7,
                multiPlayerGame.getDraftPool().size());
        singleGame.getState().throwDices(singleGame.getCurrentPlayerRound());
        assertEquals("Wrong number of dices in draftPool",4,
                singleGame.getDraftPool().size());
    }

    @Test
    public void turnStateTest(){
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
        assertEquals("Wrong number of dices in roundTrack round 1",7,
                multiPlayerGame.getRoundTrack().getDices(1).size());

        while (singleGame.getState() instanceof TurnState) {
            turnState = (TurnState) singleGame.getState();
            Objects.requireNonNull(turnState).chooseAction("endTurn");
        }
        singleGame.getState().nextRound();
        assertEquals("Wrong number of dices in roundTrack round 1",7,
                singleGame.getRoundTrack().getDices(1).size());
        assert (multiPlayerGame.getState() instanceof RoundStartState);
        assert (singleGame.getState() instanceof RoundStartState);
    }
}
