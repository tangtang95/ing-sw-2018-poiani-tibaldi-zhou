package org.poianitibaldizhou.sagrada.game.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.ColumnPublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.ObjectiveCardType;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.AnswerExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.coin.ExpendableDice;
import org.poianitibaldizhou.sagrada.game.model.coin.FavorToken;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.players.MultiPlayer;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.players.SinglePlayer;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.DrawableCollectionFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.*;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class GameTest {

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

    private SinglePlayerGame singlePlayerGame;

    private User user;
    private SchemaCard schemaCard;
    private List<PrivateObjectiveCard> privateObjectiveCards;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userList = new ArrayList<>();
        userList.add(new User("user1", NetworkUtility.encryptUsername("user1")));
        userList.add(new User("user2", NetworkUtility.encryptUsername("user2")));
        userList.add(new User("user3", NetworkUtility.encryptUsername("user3")));

        stateFakeObserverList = new ArrayList<>();
        stateFakeObserverList.add(stateFakeObserver1);
        stateFakeObserverList.add(stateFakeObserver2);
        stateFakeObserverList.add(stateFakeObserver3);

        gameFakeObserverList = new ArrayList<>();
        gameFakeObserverList.add(gameObserver1);
        gameFakeObserverList.add(gameObserver2);
        gameFakeObserverList.add(gameObserver3);

        multiPlayerGame = new MultiPlayerGame("Multi player game", userList, terminationGameManager);

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
    public void testDraftPoolObserver() {
        DraftPool draftPool = new DraftPool();
        multiPlayerGame.setDraftPool(draftPool);

        IDraftPoolFakeObserver draftPoolFakeObserver = mock(IDraftPoolFakeObserver.class);

        multiPlayerGame.attachDraftPoolObserver(userList.get(0).getToken(), draftPoolFakeObserver);

        Map<String, IDraftPoolFakeObserver> map = new HashMap<>();
        map.putIfAbsent(userList.get(0).getToken(), draftPoolFakeObserver);

        assertEquals(draftPool.getObserverMap(), map);

        multiPlayerGame.detachObservers(userList.get(0).getToken());

        assertTrue(draftPool.getObserverMap().isEmpty());
    }

    @Test
    public void testRoundTrackObserver() {
        RoundTrack roundTrack = new RoundTrack ();
        multiPlayerGame.setRoundTrack(roundTrack );

        IRoundTrackFakeObserver roundTrackFakeObserver = mock(IRoundTrackFakeObserver.class);

        multiPlayerGame.attachRoundTrackObserver(userList.get(0).getToken(), roundTrackFakeObserver);

        Map<String, IRoundTrackFakeObserver> map = new HashMap<>();
        map.putIfAbsent(userList.get(0).getToken(), roundTrackFakeObserver);

        assertEquals(roundTrack.getObserverMap(), map);

        multiPlayerGame.detachObservers(userList.get(0).getToken());

        assertTrue(roundTrack.getObserverMap().isEmpty());
    }

    @Test
    public void testDiceBagObserver() {
        DrawableCollection<Dice> diceBag = new DrawableCollection<>();
        multiPlayerGame.setDiceBag(diceBag);

        GameObserverManager gameObserverManager = mock(GameObserverManager.class);
        IDrawableCollectionObserver drawableCollectionObserver = mock(IDrawableCollectionObserver.class);
        IDrawableCollectionFakeObserver<Dice> diceIDrawableCollectionFakeObserver = new DrawableCollectionFakeObserver<>(userList.get(0).getToken(),
                drawableCollectionObserver, gameObserverManager);

        multiPlayerGame.attachDiceBagObserver(userList.get(0).getToken(), diceIDrawableCollectionFakeObserver);

        Map<String, IDrawableCollectionFakeObserver> map = new HashMap<>();
        map.putIfAbsent(userList.get(0).getToken(), diceIDrawableCollectionFakeObserver);

        assertEquals(diceBag.getObserverMap(), map);

        multiPlayerGame.detachObservers(userList.get(0).getToken());

        assertTrue(diceBag.getObserverMap().isEmpty());
    }

    @Test
    public void testSchemaCardObserver() {
        SchemaCard schemaCard = createEmptySchemaCard();
        multiPlayerGame.setPlayer(userList.get(0).getToken(), schemaCard, createMockListOfPrivateObjectiveCards());

        ISchemaCardFakeObserver schemaCardFakeObserver = mock(ISchemaCardFakeObserver.class);

        multiPlayerGame.attachSchemaCardObserver(userList.get(0).getToken(), schemaCard, schemaCardFakeObserver);

        Map<String, ISchemaCardFakeObserver> map = new HashMap<>();
        map.putIfAbsent(userList.get(0).getToken(), schemaCardFakeObserver);

        assertEquals(multiPlayerGame.getPlayers().get(0).getSchemaCard().getObserverMap(), map);

        multiPlayerGame.detachObservers(userList.get(0).getToken());

        assertTrue(multiPlayerGame.getPlayers().get(0).getSchemaCard().getObserverMap().isEmpty());
    }

    @Test
    public void testToolCardObserver() {
        ToolCard toolCard = new ToolCard(Color.GREEN, "name", "descr", "[1-CA]");
        multiPlayerGame.addToolCard(toolCard);

        IToolCardFakeObserver toolCardObserver = mock(IToolCardFakeObserver.class);

        multiPlayerGame.attachToolCardObserver(userList.get(0).getToken(),toolCard, toolCardObserver);

        Map<String, IToolCardFakeObserver> map = new HashMap<>();
        map.putIfAbsent(userList.get(0).getToken(), toolCardObserver);

        assertEquals(toolCard.getObserverMap(), map);

        multiPlayerGame.detachObservers(userList.get(0).getToken());

        assertTrue(toolCard.getObserverMap().isEmpty());
    }

    @Test
    public void testPlayerObserver() {
        MultiPlayer player = new MultiPlayer(userList.get(0), new FavorToken(createEmptySchemaCard().getDifficulty()),
                createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        multiPlayerGame.addNewPlayer(userList.get(0), createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());

        IPlayerFakeObserver playerFakeObserver = mock(IPlayerFakeObserver.class);

        multiPlayerGame.attachPlayerObserver(userList.get(0).getToken(), player, playerFakeObserver);

        Map<String, IPlayerFakeObserver> map = new HashMap<>();
        map.putIfAbsent(userList.get(0).getToken(), playerFakeObserver);

        assertEquals(map, multiPlayerGame.getPlayers().get(0).getObserverMap());

        multiPlayerGame.detachObservers(userList.get(0).getToken());

        assertTrue(multiPlayerGame.getPlayers().get(0).getObserverMap().isEmpty());
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


    @Test
    public void testAddRemainingDiceToRoundTrack() {
        RoundTrack roundTrack = new RoundTrack();
        roundTrack.addDiceToRound(new Dice(4, Color.BLUE), RoundTrack.FIRST_ROUND);
        DraftPool draftPool = new DraftPool();
        draftPool.addDice(new Dice(2, Color.RED));
        draftPool.addDice(new Dice(5, Color.RED));

        multiPlayerGame.setRoundTrack(roundTrack);
        multiPlayerGame.setDraftPool(draftPool);

        RoundTrack newRoundTrack = RoundTrack.newInstance(roundTrack);
        newRoundTrack.addDicesToRound(draftPool.getDices(), 1);

        multiPlayerGame.addRemainingDiceToRoundTrack(1);

        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            assertEquals (newRoundTrack.getDices(i), multiPlayerGame.getRoundTrack().getDices(i));
        }
    }

    @Test
    public void testAddDiceToDraftPoolFromDiceBag() {
        DrawableCollection<Dice> drawableCollection = new DrawableCollection<>();
        drawableCollection.addElement(new Dice(4, Color.BLUE));
        drawableCollection.addElement(new Dice(2, Color.GREEN));
        drawableCollection.addElement(new Dice(1, Color.BLUE));
        drawableCollection.addElement(new Dice(5, Color.GREEN));
        drawableCollection.addElement(new Dice(6, Color.RED));
        drawableCollection.addElement(new Dice(2, Color.PURPLE));
        drawableCollection.addElement(new Dice(1, Color.YELLOW));

        DraftPool draftPool = new DraftPool();
        draftPool.addDices(drawableCollection.getCollection());

        multiPlayerGame.setDiceBag(drawableCollection);

        multiPlayerGame.setDraftPool(new DraftPool());

        multiPlayerGame.addDicesToDraftPoolFromDiceBag();

        assertEquals(draftPool, multiPlayerGame.getDraftPool());
    }

    @Test(expected = Exception.class)
    public void testAddDiceToDraftPoolException() {
        DrawableCollection<Dice> drawableCollection = new DrawableCollection<>();
        drawableCollection.addElement(new Dice(4, Color.BLUE));
        drawableCollection.addElement(new Dice(2, Color.GREEN));

        multiPlayerGame.addDicesToDraftPoolFromDiceBag();
    }

    @Test
    public void testClearDraftPool() {
        DraftPool draftPool = new DraftPool();
        draftPool.addDice(new Dice(2, Color.GREEN));

        multiPlayerGame.setDraftPool(draftPool);
        multiPlayerGame.clearDraftPool();

        assertTrue(multiPlayerGame.getDraftPool().getDices().isEmpty());
    }

    @Test
    public void testInitDiceBag() {
        multiPlayerGame.initDiceBag();

        assertTrue(multiPlayerGame.getDiceBag().getCollection().size() == 90);
    }

    @Test
    public void testGetCurrentPlayer() throws Exception {
        MultiPlayer player = new MultiPlayer(userList.get(0), new FavorToken(createEmptySchemaCard().getDifficulty()),
                createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        when(stateGame.getCurrentPlayer()).thenReturn(player);
        multiPlayerGame.setState(stateGame);

        assertEquals(player, multiPlayerGame.getCurrentPlayer());
    }


    @Test
    public void testTerminateGame() {
        multiPlayerGame.terminateGame();
        verify(terminationGameManager, times(1)).terminateGame();
    }

    @Test
    public void testSetPlayer() {
        multiPlayerGame.setPlayer(userList.get(0).getToken(), createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        multiPlayerGame.setPlayer(userList.get(1).getToken(), createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());

        List<Player> playerList = new ArrayList<>();
        MultiPlayer player = new MultiPlayer(userList.get(0), new FavorToken(createEmptySchemaCard().getDifficulty()),
                createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        MultiPlayer player2 = new MultiPlayer(userList.get(1), new FavorToken(createEmptySchemaCard().getDifficulty()),
                createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        playerList.add(player);
        playerList.add(player2);

        assertEquals(playerList, multiPlayerGame.getPlayers());
    }

    @Test(expected = Exception.class)
    public void testJoinException() throws Exception{
        multiPlayerGame.userJoin("nonExistingToken");
    }

    @Test
    public void testJoin() throws Exception {
        multiPlayerGame.setState(stateGame);
        multiPlayerGame.userJoin(userList.get(0).getToken());
        verify(stateGame).readyGame(userList.get(0).getToken());
    }

    @Test
    public void testStateAndGameObserversJoinAndLeave() {
        Map<String, IStateFakeObserver> expectedStateMapObserver = new HashMap<>();
        Map<String, IGameFakeObserver> expectedGameMapObserver = new HashMap<>();
        for (int i = 0; i < multiPlayerGame.getUsers().size(); i++) {
            multiPlayerGame.attachStateObserver(userList.get(i).getToken(), stateFakeObserverList.get(i));
            multiPlayerGame.attachGameObserver(userList.get(i).getToken(), gameFakeObserverList.get(i));
            expectedGameMapObserver.putIfAbsent(userList.get(i).getToken(), gameFakeObserverList.get(i));
            expectedStateMapObserver.putIfAbsent(userList.get(i).getToken(), stateFakeObserverList.get(i));
        }

        multiPlayerGame.detachStateObserver(userList.get(0).getToken());
        multiPlayerGame.detachGameObserver(userList.get(0).getToken());
        expectedGameMapObserver.remove(userList.get(0).getToken());
        expectedStateMapObserver.remove(userList.get(0).getToken());

        assertEquals(expectedGameMapObserver, multiPlayerGame.getGameObservers());
        assertEquals(expectedStateMapObserver, multiPlayerGame.getStateObservers());
    }

    @Test
    public void testGetUserByToken() {
        assertEquals(userList.get(0), multiPlayerGame.getUserByToken(userList.get(0).getToken()));
    }

    @Test(expected = Exception.class)
    public void testGetUserByTokenException() {
        multiPlayerGame.getUserByToken("nonExistingToken");
    }

    @Test
    public void testGetListByReferences() {
        MultiPlayer player1 = new MultiPlayer(userList.get(0), new FavorToken(createEmptySchemaCard().getDifficulty()), createEmptySchemaCard(),
                createMockListOfPrivateObjectiveCards());
        MultiPlayer player2 = new MultiPlayer(userList.get(1), new FavorToken(createEmptySchemaCard().getDifficulty()),
                createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        multiPlayerGame.addNewPlayer(userList.get(0), createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        multiPlayerGame.addNewPlayer(userList.get(1), createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());

        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);

        List<Player> playerListRef = multiPlayerGame.getPlayerListReferences();

        assertEquals(playerList, playerListRef);

        for (Player refPlayer : playerListRef) {
            for(Player nonRefPlayer : playerList)
                assertFalse(refPlayer == nonRefPlayer);
        }
    }

    @Test
    public void testForceSkipTurn() throws Exception {
        multiPlayerGame.setState(stateGame);
        multiPlayerGame.forceSkipTurn();
        verify(stateGame, times(1)).forceSkipTurn();
    }

    @Test
    public void testForce() throws Exception {
        multiPlayerGame.setState(stateGame);
        multiPlayerGame.forceStateChange();
        verify(stateGame, times(1)).forceStateChange();
    }

    @Test
    public void testForceGame() throws Exception {
        multiPlayerGame.setState(stateGame);
        multiPlayerGame.forceGameTerminationBeforeStarting();
        verify(stateGame, times(1)).forceGameTerminationBeforeStarting();
    }

    @Test
    public void testGameTimedOutUsers() {
        multiPlayerGame.addNewPlayer(userList.get(0), createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        List<User> timedOutExpected = new ArrayList<>();
        timedOutExpected.add(userList.get(1));
        timedOutExpected.add(userList.get(2));

        assertEquals(timedOutExpected, multiPlayerGame.getTimedOutUsers());
    }

    @Test
    public void testFireExecutionEvent() throws Exception {
        multiPlayerGame.setState(stateGame);
        ExecutorEvent executorEvent = new AnswerExecutorEvent(true);
        multiPlayerGame.userFireExecutorEvent(userList.get(0).getToken(), executorEvent);

        verify(stateGame, times(1)).fireExecutorEvent(executorEvent);
    }

    @Test(expected = Exception.class)
    public void testUserSelectSchemaCardException() throws Exception {
        multiPlayerGame.userSelectSchemaCard("nonExitingToken", createEmptySchemaCard());
    }

    @Test
    public void testUserSelectSchemaCard() throws Exception {
        multiPlayerGame.setState(stateGame);
        multiPlayerGame.userSelectSchemaCard(userList.get(0).getToken(), createEmptySchemaCard());
        verify(stateGame, times(1)).ready(userList.get(0).getToken(), createEmptySchemaCard());
    }

    @Test(expected = Exception.class)
    public void testUserPlaceDiceExceptionNotContainsToken() throws Exception {
        multiPlayerGame.userPlaceDice("nonExistingToken", new Dice(5, Color.GREEN), new Position(0,0));
    }

    @Test(expected = Exception.class)
    public void testUserPlaceDiceExceptionNoDiceInDraftPool() throws Exception {
        DraftPool draftPool = new DraftPool();
        draftPool.addDice(new Dice(2, Color.GREEN));
        draftPool.addDice(new Dice(1, Color.BLUE));
        multiPlayerGame.setDraftPool(draftPool);

        multiPlayerGame.userPlaceDice(userList.get(0).getToken(), new Dice(2, Color.YELLOW), new Position(0,0));
    }

    @Test
    public void testUserPlaceDice() throws Exception {
        DraftPool draftPool = new DraftPool();
        draftPool.addDice(new Dice(2, Color.GREEN));
        draftPool.addDice(new Dice(1, Color.BLUE));
        multiPlayerGame.setDraftPool(draftPool);
        MultiPlayer player = new MultiPlayer(userList.get(0), new FavorToken(createEmptySchemaCard().getDifficulty()), createEmptySchemaCard(),
                createMockListOfPrivateObjectiveCards());
        multiPlayerGame.addNewPlayer(userList.get(0), createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        multiPlayerGame.setState(stateGame);
        multiPlayerGame.userPlaceDice(userList.get(0).getToken(), new Dice(2, Color.GREEN), new Position(0,0));
        verify(stateGame, times(1)).placeDice(player, new Dice(2, Color.GREEN), new Position(0,0 ));
    }

    @Test(expected = Exception.class)
    public void testUserUseToolCardExceptionNotPresentToken() throws Exception {
        ToolCard toolCard = mock(ToolCard.class);
        IToolCardExecutorFakeObserver toolCardExecutorFakeObserver = mock(IToolCardExecutorFakeObserver.class);
        multiPlayerGame.userUseToolCard("nonPresentToken", toolCard, toolCardExecutorFakeObserver);
    }

    @Test(expected = Exception.class)
    public void testUseToolCardNotPresentToolCard() throws Exception {
        ToolCard toolCard = new ToolCard(Color.GREEN, "toolcardname", "toolcarddescr", "[1-CA]");
        IToolCardExecutorFakeObserver toolCardExecutorFakeObserver = mock(IToolCardExecutorFakeObserver.class);
        multiPlayerGame.userUseToolCard(userList.get(0).getToken(), toolCard, toolCardExecutorFakeObserver);
    }

    @Test
    public void testUserUseToolCard() throws Exception {
        ToolCard toolCard = new ToolCard(Color.GREEN, "toolcardname", "toolcarddescr", "[1-CA]");
        IToolCardExecutorFakeObserver toolCardExecutorFakeObserver = mock(IToolCardExecutorFakeObserver.class);

        MultiPlayer multiPlayer = new MultiPlayer(userList.get(0), new FavorToken(createEmptySchemaCard().getDifficulty()),
                createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());

        multiPlayerGame.addToolCard(toolCard);
        multiPlayerGame.setState(stateGame);
        multiPlayerGame.addNewPlayer(userList.get(0), createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        multiPlayerGame.userUseToolCard(userList.get(0).getToken(), toolCard, toolCardExecutorFakeObserver);
        verify(stateGame, times(1)).useCard(multiPlayer, toolCard, toolCardExecutorFakeObserver);
    }

    @Test(expected = Exception.class)
    public void testUserChoseActionException() throws Exception {
        multiPlayerGame.userChooseAction("nonExistToken", new PlaceDiceAction());
    }

    @Test
    public void testUserChooseAction() throws Exception{
        multiPlayerGame.setState(stateGame);
        MultiPlayer player = new MultiPlayer(userList.get(0), new FavorToken(createEmptySchemaCard().getDifficulty()),createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        multiPlayerGame.addNewPlayer(userList.get(0), createEmptySchemaCard(), createMockListOfPrivateObjectiveCards());
        multiPlayerGame.userChooseAction(userList.get(0).getToken(), new PlaceDiceAction());
        verify(stateGame, times(1)).chooseAction(player, new PlaceDiceAction());
    }



    @Test
    public void testSelectPrivateObjectiveCard() {
        SinglePlayer singlePlayer = new SinglePlayer(user, new ExpendableDice(singlePlayerGame), schemaCard, privateObjectiveCards);
        singlePlayer.setPrivateObjectiveCard(privateObjectiveCards.get(0));
        singlePlayerGame.addNewPlayer(user, schemaCard, privateObjectiveCards);
        singlePlayerGame.selectPrivateObjectiveCard(singlePlayer, privateObjectiveCards.get(0));

        assertEquals(singlePlayer.getPrivateObjectiveCard(), singlePlayerGame.getPlayers().get(0).getPrivateObjectiveCard());
    }

    @Test(expected = Exception.class)
    public void testGetIndexOfPlayerException() {
        singlePlayerGame.getNextPlayer(new SinglePlayer(user, new ExpendableDice(singlePlayerGame), schemaCard, privateObjectiveCards),
                Direction.COUNTER_CLOCKWISE);
    }

    @Test
    public void testAddPublicObjectiveCard() {
        PublicObjectiveCard publicObjectiveCard = new ColumnPublicObjectiveCard("name", "descr", 2, ObjectiveCardType.NUMBER);
        singlePlayerGame.addPublicObjectiveCard(publicObjectiveCard);
        assertEquals(Collections.singletonList(publicObjectiveCard), singlePlayerGame.getPublicObjectiveCards());
    }

    @Test(expected = Exception.class)
    public void testUserChoosePrivateObjectiveCardException() throws Exception {
        PrivateObjectiveCard privateObjectiveCard = mock(PrivateObjectiveCard.class);
        singlePlayerGame.userChoosePrivateObjectiveCard("notExistingToken", privateObjectiveCard);
    }

    @Test
    public void testUserChoosePrivateObjectiveCard() throws Exception {
        PrivateObjectiveCard privateObjectiveCard = new PrivateObjectiveCard("name", "descr", Color.BLUE);
        SinglePlayer singlePlayer = new SinglePlayer(user, new ExpendableDice(singlePlayerGame), schemaCard, privateObjectiveCards);
        singlePlayerGame.addNewPlayer(user, schemaCard, privateObjectiveCards);
        singlePlayerGame.setState(stateGame);
        singlePlayerGame.userChoosePrivateObjectiveCard(user.getToken(), privateObjectiveCard);
        verify(stateGame, times(1)).choosePrivateObjectiveCard(singlePlayer, privateObjectiveCard);
    }

    @Test
    public void testGetPlayerScore() throws Exception {
        IConstraint[][] constraint = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraint[i][j] = new NoConstraint();
            }

        }
        SchemaCard notMockedSchemaCard = new SchemaCard("name", 4, constraint);

        PrivateObjectiveCard chosenCard = mock(PrivateObjectiveCard.class);
        when(chosenCard.getScore(notMockedSchemaCard)).thenReturn(0);
        singlePlayerGame.setState(stateGame);
        singlePlayerGame.addNewPlayer(user, notMockedSchemaCard, Collections.singletonList(chosenCard));
        singlePlayerGame.userChoosePrivateObjectiveCard(user.getToken(), chosenCard);


        SinglePlayer singlePlayer = new SinglePlayer(user, new ExpendableDice(singlePlayerGame), notMockedSchemaCard, Collections.singletonList(chosenCard));
        singlePlayer.setPrivateObjectiveCard(chosenCard);

        assertEquals(singlePlayer.getVictoryPoints(), singlePlayerGame.getPlayerScore(singlePlayer));
    }

    @Test
    public void testGetToolCards() {
        ToolCard toolCard1 = new ToolCard(Color.BLUE, "name1", "descr1", "[1-CA]");
        ToolCard toolCard2 = new ToolCard(Color.GREEN, "name2", "descr2", "[1-CC]");
        ToolCard toolCard3 = new ToolCard(Color.YELLOW, "name3", "descr3", "[1-CC]");
        List<ToolCard> toolCardList = new ArrayList<>();
        toolCardList.add(toolCard1);
        toolCardList.add(toolCard2);
        toolCardList.add(toolCard3);

        multiPlayerGame.addToolCard(toolCard1);
        multiPlayerGame.addToolCard(toolCard2);
        multiPlayerGame.addToolCard(toolCard3);

        assertEquals(toolCardList, multiPlayerGame.getToolCards());
    }

    @Test(expected = Exception.class)
    public void testAttachToolCardObserverException() {
        ToolCard toolCard1 = new ToolCard(Color.BLUE, "name1", "descr1", "[1-CA]");
        multiPlayerGame.addToolCard(toolCard1);

        IToolCardFakeObserver toolCardFakeObserver = mock(IToolCardFakeObserver.class);

        multiPlayerGame.attachToolCardObserver("token", new ToolCard(Color.BLUE, "name2", "descr2", "[1-CC]"),toolCardFakeObserver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttachSchemaCardObserverException() {
        ISchemaCardFakeObserver schemaCardFakeObserver = mock(ISchemaCardFakeObserver.class);
        multiPlayerGame.attachSchemaCardObserver("token", schemaCard, schemaCardFakeObserver);
    }

    @Test
    public void testGetPrivateObjectiveCardByToken() {
        multiPlayerGame.addNewPlayer(user, schemaCard, privateObjectiveCards);

        assertEquals(privateObjectiveCards, multiPlayerGame.getPrivateObjectiveCardsByToken(user.getToken()));
    }

    @Test(expected = IllegalStateException.class)
    public void testUserPlaceDiceIllegalState() throws Exception {
        DraftPool draftPool = mock(DraftPool.class);
        Dice dice = new Dice(4, Color.BLUE);
        List<Dice> diceList = Collections.singletonList(dice);

        when(draftPool.getDices()).thenReturn(diceList);
        doThrow(new DiceNotFoundException("")).when(draftPool).useDice(dice);
        multiPlayerGame.setDraftPool(draftPool);
        multiPlayerGame.addNewPlayer(userList.get(0), schemaCard, privateObjectiveCards);
        multiPlayerGame.setState(stateGame);

        multiPlayerGame.userPlaceDice(userList.get(0).getToken(), new Dice(4, Color.BLUE), new Position(0,0));

    }
}
