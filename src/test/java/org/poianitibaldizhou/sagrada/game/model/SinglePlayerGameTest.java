package org.poianitibaldizhou.sagrada.game.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ClearAll;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.PayDice;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.coin.ExpendableDice;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.players.MultiPlayer;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.players.SinglePlayer;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IGameFakeObserver;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SinglePlayerGameTest {

    @Mock
    private TerminationGameManager terminationGameManager;

    @Mock
    private IStateGame stateGame;

    private SinglePlayerGame singlePlayerGame;

    private User user;
    private SchemaCard schemaCard;
    private List<PrivateObjectiveCard> privateObjectiveCards;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User("user", NetworkUtility.encryptUsername("user"));

        privateObjectiveCards = new ArrayList<>();
        privateObjectiveCards.add(new PrivateObjectiveCard("private1", "descr1", Color.BLUE));
        privateObjectiveCards.add(new PrivateObjectiveCard("private2", "descr2", Color.YELLOW));

        IConstraint[][] matrix = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                matrix[i][j] = new NoConstraint();
            }
        }
        schemaCard = new SchemaCard("schemaCardName", 5, matrix);

        singlePlayerGame = new SinglePlayerGame("gameName", user, 3, terminationGameManager);
    }

    @Test
    public void testBasicMethods() {
        assertEquals(3, singlePlayerGame.getDifficulty());
        assertEquals(3, singlePlayerGame.getNumberOfToolCardForGame());
        assertEquals(SinglePlayerGame.NUMBER_OF_PRIVATE_OBJECTIVE_CARDS, singlePlayerGame.getNumberOfPrivateObjectiveCardForGame());
        assertEquals(SinglePlayerGame.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS, singlePlayerGame.getNumberOfPublicObjectiveCardForGame());
        assertEquals(SinglePlayerGame.NUMBER_OF_DICES_TO_DRAW, singlePlayerGame.getNumberOfDicesToDraw());
        assertTrue(singlePlayerGame.isSinglePlayer());
    }

    @Test
    public void testSetPlayer() {
        singlePlayerGame.addNewPlayer(user, schemaCard, privateObjectiveCards);
        SinglePlayer singlePlayer = new SinglePlayer(user, new ExpendableDice(singlePlayerGame), schemaCard, privateObjectiveCards);
        List<Player> playerList = new ArrayList<>();
        playerList.add(singlePlayer);

        assertEquals(playerList, singlePlayerGame.getPlayers());
    }

    @Test
    public void testGetTargetScore() {
        RoundTrack roundTrack = new RoundTrack();
        roundTrack.addDiceToRound(new Dice(5, Color.BLUE), 0);
        roundTrack.addDiceToRound(new Dice(5, Color.RED), 2);
        roundTrack.addDiceToRound(new Dice(1, Color.YELLOW), 0);
        roundTrack.addDiceToRound(new Dice(3, Color.YELLOW), 4);
        roundTrack.addDiceToRound(new Dice(6, Color.GREEN), 1);
        roundTrack.addDiceToRound(new Dice(2, Color.PURPLE), 1);

        singlePlayerGame.setRoundTrack(roundTrack);

        assertEquals(22, singlePlayerGame.getTargetScore());
    }

    @Test
    public void testPlayerOutComeWin() {
        SinglePlayer singlePlayer = new SinglePlayer(user, new ExpendableDice(singlePlayerGame), schemaCard, privateObjectiveCards);
        singlePlayerGame.addNewPlayer(user, schemaCard, privateObjectiveCards);

        Map<Player, Integer> scoreMap = new HashMap<>();
        scoreMap.putIfAbsent(singlePlayer, 24);

        RoundTrack roundTrack = new RoundTrack();
        roundTrack.addDiceToRound(new Dice(5, Color.BLUE), 0);
        roundTrack.addDiceToRound(new Dice(5, Color.RED), 2);
        roundTrack.addDiceToRound(new Dice(1, Color.YELLOW), 0);
        roundTrack.addDiceToRound(new Dice(3, Color.YELLOW), 4);
        roundTrack.addDiceToRound(new Dice(6, Color.GREEN), 1);
        roundTrack.addDiceToRound(new Dice(2, Color.PURPLE), 1);

        singlePlayerGame.setRoundTrack(roundTrack);

        singlePlayerGame.setPlayersOutcome(scoreMap, singlePlayer);

        assertEquals(Outcome.WIN.toString(), singlePlayerGame.getPlayers().get(0).getOutcome().toString());
    }

    @Test
    public void testPlayerOutComeLose() {
        SinglePlayer singlePlayer = new SinglePlayer(user, new ExpendableDice(singlePlayerGame), schemaCard, privateObjectiveCards);
        singlePlayerGame.addNewPlayer(user, schemaCard, privateObjectiveCards);

        Map<Player, Integer> scoreMap = new HashMap<>();
        scoreMap.putIfAbsent(singlePlayer, 10);

        RoundTrack roundTrack = new RoundTrack();
        roundTrack.addDiceToRound(new Dice(5, Color.BLUE), 0);
        roundTrack.addDiceToRound(new Dice(5, Color.RED), 2);
        roundTrack.addDiceToRound(new Dice(1, Color.YELLOW), 0);
        roundTrack.addDiceToRound(new Dice(3, Color.YELLOW), 4);
        roundTrack.addDiceToRound(new Dice(6, Color.GREEN), 1);
        roundTrack.addDiceToRound(new Dice(2, Color.PURPLE), 1);

        singlePlayerGame.setRoundTrack(roundTrack);

        singlePlayerGame.setPlayersOutcome(scoreMap, singlePlayer);

        assertEquals(Outcome.LOSE.toString(), singlePlayerGame.getPlayers().get(0).getOutcome().toString());
    }

    @Test(expected = Exception.class)
    public void testForceGameTerminationTest() {
        singlePlayerGame.setState(stateGame);
        singlePlayerGame.forceGameTermination(new SinglePlayer(user, new ExpendableDice(singlePlayerGame),
                schemaCard, privateObjectiveCards));
    }

    @Test
    public void testHandleGame() {
        IGameFakeObserver gameFakeObserver = mock(IGameFakeObserver.class);

        singlePlayerGame.addNewPlayer(user, schemaCard, privateObjectiveCards);
        singlePlayerGame.attachGameObserver(user.getToken(), gameFakeObserver);

        singlePlayerGame.handleEndGame();

        verify(gameFakeObserver, times(1)).onChoosePrivateObjectiveCards(privateObjectiveCards);
    }

    @Test
    public void testGetPreCommands() {
        ToolCard toolCard = new ToolCard(Color.BLUE, "name", "descr", "[1-Add dice to DraftPool]");
        Node<ICommand> expected = new Node<>(new PayDice(Color.BLUE));
        ICommand temp = (player, toolCardExecutor, turnState) -> null;

        expected.setLeftChild(new Node<>(temp));
        expected.getLeftChild().setLeftChild(new Node<>(new ClearAll()));

        assertEquals(expected.getData(), singlePlayerGame.getPreCommands(toolCard).getData());
        assertEquals(expected.getLeftChild().getLeftChild().getData(), singlePlayerGame.getPreCommands(toolCard).getLeftChild()
                .getLeftChild().getData());
    }

    @Test
    public void testGetPreCommandsExecution() throws Exception {
        ToolCard toolCard = mock(ToolCard.class);
        ToolCardExecutor toolCardExecutor = mock(ToolCardExecutor.class);
        SinglePlayer singlePlayer = mock(SinglePlayer.class);
        TurnState turnState = mock(TurnState.class);
        when(toolCard.getColor()).thenReturn(Color.BLUE);

        Node<ICommand> preCommands = singlePlayerGame.getPreCommands(toolCard);
        ICommand destroy = preCommands.getLeftChild().getData();

        assertEquals(CommandFlow.MAIN, destroy.executeCommand(singlePlayer, toolCardExecutor, turnState));
        verify(toolCard, times(1)).destroyToolCard();
    }

}
