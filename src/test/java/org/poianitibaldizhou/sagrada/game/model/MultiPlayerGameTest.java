package org.poianitibaldizhou.sagrada.game.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ClearAll;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.PayDice;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.RemoveFavorToken;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.AnswerExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.coin.FavorToken;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.players.SinglePlayer;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.DrawableCollectionFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.*;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.game.model.players.MultiPlayer;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MultiPlayerGameTest {

    private MultiPlayerGame multiPlayerGame;

    private List<User> userList;
    private List<IGameFakeObserver> gameFakeObserverList;
    private List<IStateFakeObserver> stateFakeObserverList;

    @Mock
    private IStateGame stateGame;

    @Mock
    private IGameFakeObserver gameObserver1, gameObserver2, gameObserver3;

    @Mock
    private IStateFakeObserver stateFakeObserver1, stateFakeObserver2, stateFakeObserver3;

    @Mock
    private TerminationGameManager terminationGameManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userList = new ArrayList<>();
        userList.add(new User("user1", String.valueOf("user1".hashCode())));
        userList.add(new User("user2", String.valueOf("user2".hashCode())));
        userList.add(new User("user3", String.valueOf("user3".hashCode())));

        stateFakeObserverList = new ArrayList<>();
        stateFakeObserverList.add(stateFakeObserver1);
        stateFakeObserverList.add(stateFakeObserver2);
        stateFakeObserverList.add(stateFakeObserver3);

        gameFakeObserverList = new ArrayList<>();
        gameFakeObserverList.add(gameObserver1);
        gameFakeObserverList.add(gameObserver2);
        gameFakeObserverList.add(gameObserver3);

        multiPlayerGame = new MultiPlayerGame("Multi player game", userList, terminationGameManager);
    }

    @After
    public void tearDown() {
        userList = null;

    }

    @Test
    public void testNumberOfDicesToThrow() throws Exception {
        userList.add(new User("user4", String.valueOf("user4".hashCode())));
        multiPlayerGame = new MultiPlayerGame("Multi player game", userList, terminationGameManager);
        assertEquals(9, multiPlayerGame.getNumberOfDicesToDraw());

        userList.remove(0);
        multiPlayerGame = new MultiPlayerGame("Multi player game", userList, terminationGameManager);
        assertEquals(7, multiPlayerGame.getNumberOfDicesToDraw());

        userList.remove(0);
        multiPlayerGame = new MultiPlayerGame("Multi player game", userList, terminationGameManager);
        assertEquals(5, multiPlayerGame.getNumberOfDicesToDraw());
    }

    @Test
    public void testSimpleMethods() {
        assertFalse(multiPlayerGame.isSinglePlayer());
        assertEquals(MultiPlayerGame.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS, multiPlayerGame.getNumberOfPublicObjectiveCardForGame());
        assertEquals(MultiPlayerGame.NUMBER_OF_TOOL_CARDS, multiPlayerGame.getNumberOfToolCardForGame());
        assertEquals(MultiPlayerGame.NUMBER_OF_PRIVATE_OBJECTIVE_CARDS, multiPlayerGame.getNumberOfPrivateObjectiveCardForGame());
    }

    @Test
    public void testForceGameTermination() {
        Player player = mock(Player.class);
        doNothing().when(stateGame).init();
        multiPlayerGame.setState(stateGame);
        multiPlayerGame.forceGameTermination(player);
        verify(stateGame, times(1)).forceTermination(player);
    }

    @Test
    public void testHandleEndGame() {
        doNothing().when(stateGame).init();
        doNothing().when(stateGame).calculateVictoryPoints();
        multiPlayerGame.setState(stateGame);
        multiPlayerGame.handleEndGame();

        verify(stateGame, times(1)).calculateVictoryPoints();
    }

    @Test
    public void testAddNewPlayer() {
        multiPlayerGame.setState(stateGame);

        IConstraint[][] matrix = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                matrix[i][j] = new NoConstraint();
            }
        }
        SchemaCard schemaCard = new SchemaCard("schemaCardName", 5, matrix);

        List<PrivateObjectiveCard> privateObjectiveCards = new ArrayList<>();
        PrivateObjectiveCard privateObjectiveCard = mock(PrivateObjectiveCard.class);
        privateObjectiveCards.add(privateObjectiveCard);

        multiPlayerGame.addNewPlayer(userList.get(0), schemaCard, privateObjectiveCards);

        MultiPlayer multiPlayer = new MultiPlayer(userList.get(0), new FavorToken(schemaCard.getDifficulty()), schemaCard, privateObjectiveCards);
        List<MultiPlayer> multiPlayers = new ArrayList<>();
        multiPlayers.add(multiPlayer);

        assertEquals(multiPlayers, multiPlayerGame.getPlayers());
    }

    @Test
    public void testGetWinnersByVictoryPointsEveryPlayerSamePoints() {
        List<Player> playerList = new ArrayList<>();
        userList.forEach(user -> {
            playerList.add(new MultiPlayer(user, (new FavorToken(createEmptySchemaCard().getDifficulty())),
                    createEmptySchemaCard(), createMockListOfPrivateObjectiveCards()));
            multiPlayerGame.addNewPlayer(user, createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        });

        Map<Player, Integer> scoreMap = new HashMap<>();
        playerList.forEach(player -> scoreMap.putIfAbsent(player, 2));

        multiPlayerGame.setPlayersOutcome(scoreMap, playerList.get(0));

        for (Player player : multiPlayerGame.getPlayers()) {
            if (player.getUser().equals(userList.get(2)))
                assertEquals(Outcome.WIN.toString(), player.getOutcome().toString());
            else
                assertEquals(Outcome.LOSE.toString(), player.getOutcome().toString());
        }
    }

    @Test
    public void testGetWinnersDifferentScores() {
        List<Player> playerList = new ArrayList<>();
        userList.forEach(user -> {
            playerList.add(new MultiPlayer(user, (new FavorToken(createEmptySchemaCard().getDifficulty())),
                    createEmptySchemaCard(), createMockListOfPrivateObjectiveCards()));
            multiPlayerGame.addNewPlayer(user, createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        });

        Map<Player, Integer> scoreMap = new HashMap<>();

        scoreMap.putIfAbsent(playerList.get(0), 5);
        scoreMap.putIfAbsent(playerList.get(1), 4);
        scoreMap.putIfAbsent(playerList.get(2), 2);

        multiPlayerGame.setPlayersOutcome(scoreMap, playerList.get(0));

        for (Player player : multiPlayerGame.getPlayers()) {
            if (player.getUser().equals(userList.get(0)))
                assertEquals(Outcome.WIN.toString(), player.getOutcome().toString());
            else
                assertEquals(Outcome.LOSE.toString(), player.getOutcome().toString());
        }
    }

    @Test
    public void testGetWinnnersTwoPlayersSameScore() {
        List<Player> playerList = new ArrayList<>();
        userList.forEach(user -> {
            playerList.add(new MultiPlayer(user, (new FavorToken(createEmptySchemaCard().getDifficulty())),
                    createEmptySchemaCard(), createMockListOfPrivateObjectiveCards()));
            multiPlayerGame.addNewPlayer(user, createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        });

        Map<Player, Integer> scoreMap = new HashMap<>();

        scoreMap.putIfAbsent(playerList.get(0), 5);
        scoreMap.putIfAbsent(playerList.get(1), 4);
        scoreMap.putIfAbsent(playerList.get(2), 5);

        multiPlayerGame.setPlayersOutcome(scoreMap, playerList.get(1));

        for (Player player : multiPlayerGame.getPlayers()) {
            if (player.getUser().equals(userList.get(0)))
                assertEquals(Outcome.WIN.toString(), player.getOutcome().toString());
            else
                assertEquals(Outcome.LOSE.toString(), player.getOutcome().toString());
        }
    }

    @Test
    public void testGetWinnersTwoPlayerSameScore() {
        List<Player> playerList = new ArrayList<>();
        userList.forEach(user -> {
            playerList.add(new MultiPlayer(user, (new FavorToken(createEmptySchemaCard().getDifficulty())),
                    createEmptySchemaCard(), createMockListOfPrivateObjectiveCards()));
            multiPlayerGame.addNewPlayer(user, createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        });

        Map<Player, Integer> scoreMap = new HashMap<>();

        scoreMap.putIfAbsent(playerList.get(0), 5);
        scoreMap.putIfAbsent(playerList.get(1), 4);
        scoreMap.putIfAbsent(playerList.get(2), 5);

        multiPlayerGame.setPlayersOutcome(scoreMap, playerList.get(0));

        for (Player player : multiPlayerGame.getPlayers()) {
            if (player.getUser().equals(userList.get(2)))
                assertEquals(Outcome.WIN.toString(), player.getOutcome().toString());
            else
                assertEquals(Outcome.LOSE.toString(), player.getOutcome().toString());
        }
    }
    @Test
    public void testGetPreCommands() {
        ToolCard toolCard = new ToolCard(Color.BLUE, "name", "descr", "[1-Add dice to DraftPool]");
        Node<ICommand> expected = new Node<>(new RemoveFavorToken(toolCard.getCost()));
        ICommand temp = new ICommand() {
            @Override
            public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
                return null;
            }
        };

        expected.setLeftChild(new Node<>(temp));
        expected.getLeftChild().setLeftChild(new Node<>(new ClearAll()));

        assertEquals(expected.getData(), multiPlayerGame.getPreCommands(toolCard).getData());
        assertEquals(expected.getLeftChild().getLeftChild().getData(), multiPlayerGame.getPreCommands(toolCard).getLeftChild()
                .getLeftChild().getData());
    }

    @Test
    public void testGetPreCommandsExceution() throws Exception {
        ToolCard toolCard = mock(ToolCard.class);
        ToolCardExecutor toolCardExecutor = mock(ToolCardExecutor.class);
        SinglePlayer singlePlayer = mock(SinglePlayer.class);
        TurnState turnState = mock(TurnState.class);
        when(toolCard.getCost()).thenReturn(2);

        Node<ICommand> preCommands = multiPlayerGame.getPreCommands(toolCard);
        ICommand destroy = preCommands.getLeftChild().getData();

        assertEquals(CommandFlow.MAIN, destroy.executeCommand(singlePlayer, toolCardExecutor, turnState));
        verify(toolCard, times(1)).addTokens(2);
    }

    @Test(expected = IllegalStateException.class)
    public void testWinnersByFavorTokenException() {
        multiPlayerGame.setPlayersOutcome(new HashMap<>(), new MultiPlayer(userList.get(0), new FavorToken(1),createEmptySchemaCard(),
                createMockListOfPrivateObjectiveCards()));
    }

    private List<PrivateObjectiveCard> createMockListOfPrivateObjectiveCards() {
        List<PrivateObjectiveCard> privateObjectiveCards = new ArrayList<>();
        PrivateObjectiveCard privateObjectiveCard = new PrivateObjectiveCard("name", "description", Color.YELLOW);
        privateObjectiveCards.add(privateObjectiveCard);
        return privateObjectiveCards;
    }

    private SchemaCard createEmptySchemaCard() {
        IConstraint[][] matrix = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                matrix[i][j] = new NoConstraint();
            }
        }
        return new SchemaCard("schemaCardName", 5, matrix);
    }
}
