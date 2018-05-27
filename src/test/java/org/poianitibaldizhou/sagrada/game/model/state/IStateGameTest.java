package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class IStateGameTest {

    @Mock
    private Game game;

    private IStateGame stateGame;

    @Before
    public void setUp() throws Exception {
        stateGame = new IStateGame(game) {
            @Override
            public void init() {
                // do nothing
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        stateGame = null;
        game = null;
    }

    @Test(expected = Exception.class)
    public void nextRoundTest() throws Exception {
        stateGame.nextRound();
    }

    @Test(expected = Exception.class)
    public void ready() throws Exception {
        stateGame.ready("player1", mock(SchemaCard.class));
    }

    @Test(expected = Exception.class)
    public void readyGame() throws Exception {
        stateGame.readyGame("player1");
    }

    @Test(expected = Exception.class)
    public void throwDices() throws Exception {
        stateGame.throwDices(mock(Player.class));
    }

    @Test(expected = Exception.class)
    public void choosePrivateObjectiveCard() throws Exception {
        stateGame.choosePrivateObjectiveCard(mock(Player.class), mock(PrivateObjectiveCard.class));
    }

    @Test(expected = Exception.class)
    public void calculateVictoryPoints() throws Exception {
        stateGame.calculateVictoryPoints();
    }

    @Test(expected = Exception.class)
    public void chooseAction() throws Exception {
        stateGame.chooseAction(mock(Player.class), mock(IActionCommand.class));
    }

    @Test(expected = Exception.class)
    public void placeDice() throws Exception {
        stateGame.placeDice(mock(Player.class), mock(Dice.class), mock(Position.class));
    }

    @Test(expected = Exception.class)
    public void useCard() throws Exception {
        stateGame.useCard(mock(Player.class), mock(ToolCard.class), mock(IToolCardExecutorObserver.class));
    }

    @Test(expected = Exception.class)
    public void fireExecutorEvent() throws Exception {
        stateGame.fireExecutorEvent(mock(ExecutorEvent.class));
    }

    @Test(expected = Exception.class)
    public void interruptToolCardExecution() throws Exception {
        stateGame.interruptToolCardExecution(mock(Player.class));
    }



}