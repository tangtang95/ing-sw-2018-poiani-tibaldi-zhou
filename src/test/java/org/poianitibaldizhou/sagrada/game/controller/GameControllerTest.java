package org.poianitibaldizhou.sagrada.game.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationType;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.ObjectiveCardType;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.RowPublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.*;
import org.poianitibaldizhou.sagrada.game.model.coin.FavorToken;
import org.poianitibaldizhou.sagrada.game.model.players.MultiPlayer;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.EndTurnAction;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.GameNetworkManager;
import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GameControllerTest {

    private ClientCreateMessage clientCreateMessage;
    private GameController gameController;
    private String gameName;
    private String tokenUser, tokenUser2;
    private String userName, userName2;

    private List<String> tokenOfPlayerPlayingInGame;
    private SchemaCardWrapper schemaCardWrapper;
    private SchemaCard schemaCard;
    private List<PrivateObjectiveCard> privateObjectiveCards;
    private PrivateObjectiveCardWrapper privateObjectiveCardWrapper;
    private PrivateObjectiveCard privateObjectiveCard;

    @Mock
    private GameManager gameManager;

    @Mock
    private IGameView view, view2;

    @Mock
    private IDraftPoolObserver draftPoolObserver;

    @Mock
    private IRoundTrackObserver roundTrackObserver;

    @Mock
    private ISchemaCardObserver schemaCardObserver1, schemaCardObserver2;

    @Mock
    private IGameObserver gameObserver;

    @Mock
    private IPlayerObserver playerObserver1, playerObserver2;

    @Mock
    private IToolCardObserver toolCardObserver;

    @Mock
    private ITimeOutObserver timeOutObserver;

    @Mock
    private IStateObserver stateObserver;

    @Mock
    private IToolCardExecutorObserver toolCardExecutorObserver;

    @Mock
    private IDrawableCollectionObserver diceBagObserver;

    @Mock
    private IGame game;

    @Mock
    private GameObserverManager gameObserverManager;

    @Mock
    private GameNetworkManager gameNetworkManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clientCreateMessage = new ClientCreateMessage();
        when(gameManager.getGameNetworkManager()).thenReturn(gameNetworkManager);
        gameController = new GameController(gameManager);
        gameName = "gameName";

        userName = "username";
        userName2 = "username2";
        tokenUser = NetworkUtility.encryptUsername(userName);
        tokenUser2 = NetworkUtility.encryptUsername(userName2);
        tokenOfPlayerPlayingInGame = new ArrayList<>();
        tokenOfPlayerPlayingInGame.add(tokenUser);
        tokenOfPlayerPlayingInGame.add(tokenUser2);

        TileWrapper[][] tiles = new TileWrapper[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                tiles[i][j] = new TileWrapper(null);
            }
        }

        schemaCardWrapper = new SchemaCardWrapper("name", 2, tiles);

        Tile[][] tiles1 = new Tile[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                tiles1[i][j] = new Tile(null);
            }
        }

        schemaCard = new SchemaCard("name", 2, tiles1);

        privateObjectiveCards = new ArrayList<>();
        privateObjectiveCards.add(new PrivateObjectiveCard("name", "descr", Color.PURPLE));

        privateObjectiveCard = new PrivateObjectiveCard("Sfumature Viola - Privata",
                "Somma dei valori su tutti i dadi viola", Color.PURPLE);
        privateObjectiveCardWrapper = new PrivateObjectiveCardWrapper("Sfumature Viola - Privata",
                "Somma dei valori su tutti i dadi viola", ColorWrapper.PURPLE);
    }


    @Test
    public void testJoinGame() throws Exception {
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);


        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser)
                .createGameNameMessage(gameName).buildMessage();

        gameController.joinGame(clientMessage, view, gameObserver, roundTrackObserver,
                stateObserver, draftPoolObserver, diceBagObserver, timeOutObserver);

        verify(view).ack(anyString());
        verify(game).userJoin(tokenUser);
        verify(gameNetworkManager).putView(tokenUser, view);
    }

    @Test(expected = IOException.class)
    public void testJoinGameIOException() throws Exception {
        when(gameManager.notContainsGame(gameName)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();

        gameController.joinGame(clientMessage, view, gameObserver, roundTrackObserver,
                stateObserver, draftPoolObserver, diceBagObserver, timeOutObserver);

        verify(view).err(anyString());
    }

    @Test
    public void testJoinGameInvalidAction() throws Exception {
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();

        doThrow(InvalidActionException.class).when(game).userJoin(tokenUser);

        gameController.joinGame(clientMessage, view, gameObserver, roundTrackObserver,
                stateObserver, draftPoolObserver, diceBagObserver, timeOutObserver);

        verify(game).detachObservers(tokenUser);
        verify(view).err(anyString());
        verify(gameNetworkManager, times(0)).putView(tokenUser, view);
    }

    @Test
    public void testJoinGameInvalidActionAndIOException() throws Exception {
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();

        doThrow(InvalidActionException.class).when(game).userJoin(tokenUser);
        doThrow(IOException.class).when(view).err(anyString());

        gameController.joinGame(clientMessage, view, gameObserver, roundTrackObserver,
                stateObserver, draftPoolObserver, diceBagObserver, timeOutObserver);

        verify(game).detachObservers(tokenUser);
        verify(view).err(anyString());
        verify(gameNetworkManager, times(0)).putView(tokenUser, view);
    }

    @Test
    public void testJoinGameIOExceptionWhenACKing() throws Exception {
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).ack(anyString());

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser)
                .createGameNameMessage(gameName).buildMessage();

        gameController.joinGame(clientMessage, view, gameObserver, roundTrackObserver,
                stateObserver, draftPoolObserver, diceBagObserver, timeOutObserver);

        verify(view).ack(anyString());
        verify(game).userJoin(tokenUser);
        verify(gameNetworkManager).putView(tokenUser, view);
        verify(gameObserverManager).signalDisconnection(tokenUser);
    }

    @Test
    public void testUserChooseCard() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).
                createSchemaCardMessage(schemaCardWrapper).buildMessage();

        gameController.chooseSchemaCard(clientMessage);

        verify(game).userSelectSchemaCard(tokenUser, schemaCard);
        verify(view).ack(anyString());
    }

    @Test(expected = IOException.class)
    public void testUserChooseCardInitialCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).
                createSchemaCardMessage(schemaCardWrapper).buildMessage();

        gameController.chooseSchemaCard(clientMessage);
    }

    @Test
    public void testUserChooseCardTerminateGameDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).
                createSchemaCardMessage(schemaCardWrapper).buildMessage();

        gameController.chooseSchemaCard(clientMessage);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }

    @Test
    public void testUserChooseSchemaCardWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).
                createSchemaCardMessage(schemaCardWrapper).buildMessage();

        gameController.chooseSchemaCard(clientMessage);

        verify(view).err(anyString());
    }

    @Test
    public void testUserChooseCardInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userSelectSchemaCard(tokenUser, schemaCard);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).
                createSchemaCardMessage(schemaCardWrapper).buildMessage();

        try {
            gameController.chooseSchemaCard(clientMessage);
            fail("Exception expected");
        } catch (IOException e) {
            verify(view).err(anyString());
        }
    }

    @Test
    public void testUserChooseCardInvalidExceptionACKingIOException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userSelectSchemaCard(tokenUser, schemaCard);
        doThrow(IOException.class).when(view).err(anyString());

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).
                createSchemaCardMessage(schemaCardWrapper).buildMessage();
        gameController.chooseSchemaCard(clientMessage);
        verifyHandleIOException();
    }

    @Test
    public void testUserChooseCardACKingIOException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).ack(anyString());

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).
                createSchemaCardMessage(schemaCardWrapper).buildMessage();
        gameController.chooseSchemaCard(clientMessage);
        verifyHandleIOException();
    }

    @Test(expected = IOException.class)
    public void testUserBindPlayerInitCheckFail() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").
                createGameNameMessage(gameName).buildMessage();

        gameController.bindPlayer(clientMessage, playerObserver1, schemaCardObserver1);
    }

    @Test
    public void testUserBindPlayerGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").
                createGameNameMessage(gameName).buildMessage();

        gameController.bindPlayer(clientMessage, playerObserver1, schemaCardObserver1);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }

    @Test
    public void testUserBindPlayerWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").
                createGameNameMessage(gameName).buildMessage();

        gameController.bindPlayer(clientMessage, playerObserver1, schemaCardObserver1);

        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testUserBindPlayerNotExistingPlayer() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        List<Player> players = new ArrayList<>();
        when(game.getPlayers()).thenReturn(players);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").
                createGameNameMessage(gameName).buildMessage();

        gameController.bindPlayer(clientMessage, playerObserver1, schemaCardObserver1);
    }

    @Test
    public void testUserBindPlayer() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        List<Player> players = new ArrayList<>();
        players.add(new MultiPlayer(new User("username", tokenUser), new FavorToken(2), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(players);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").
                createGameNameMessage(gameName).buildMessage();

        gameController.bindPlayer(clientMessage, playerObserver1, schemaCardObserver1);

    }

    @Test
    public void testUserBindPlayerACKingIOException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).ack(anyString());

        List<Player> players = new ArrayList<>();
        players.add(new MultiPlayer(new User("username", tokenUser), new FavorToken(2), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(players);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").
                createGameNameMessage(gameName).buildMessage();

        gameController.bindPlayer(clientMessage, playerObserver1, schemaCardObserver1);
    }

    @Test
    public void testUserBindToolCardWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").createGameNameMessage(gameName).
                createToolCardMessage(new ToolCardWrapper("name", "descr", ColorWrapper.BLUE, 2)).buildMessage();

        gameController.bindToolCard(clientMessage, toolCardObserver);

        verify(view).err(anyString());
    }

    @Test
    public void testUserBindToolCardNotExistingToolCard() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        List<ToolCard> toolCards = new ArrayList<>();
        toolCards.add(new ToolCard(Color.BLUE, "nameDifferent", "descr", "[1-CA]"));

        when(game.getToolCards()).thenReturn(toolCards);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").createGameNameMessage(gameName).
                createToolCardMessage(new ToolCardWrapper("name", "descr", ColorWrapper.BLUE, 0)).buildMessage();

        gameController.bindToolCard(clientMessage, toolCardObserver);

        verify(view).err(anyString());
    }

    @Test
    public void testUserBindToolCardNotExistingToolCardErrorSignalingToView() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        List<ToolCard> toolCards = new ArrayList<>();
        toolCards.add(new ToolCard(Color.BLUE, "nameDifferent", "descr", "[1-CA]"));

        doThrow(IOException.class).when(view).err(anyString());

        when(game.getToolCards()).thenReturn(toolCards);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").createGameNameMessage(gameName).
                createToolCardMessage(new ToolCardWrapper("name", "descr", ColorWrapper.BLUE, 0)).buildMessage();

        gameController.bindToolCard(clientMessage, toolCardObserver);

        verifyHandleIOException();
    }

    @Test
    public void testUserBindToolCard() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        List<ToolCard> toolCards = new ArrayList<>();
        toolCards.add(new ToolCard(Color.PURPLE, "Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6", "[1-Choose dice]" +
                "[2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]"));

        when(game.getToolCards()).thenReturn(toolCards);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").createGameNameMessage(gameName).
                createToolCardMessage(new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                        "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                        ColorWrapper.PURPLE, 0)).buildMessage();

        gameController.bindToolCard(clientMessage, toolCardObserver);
    }

    @Test
    public void testUserBindToolCardErrorACKing() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        List<ToolCard> toolCards = new ArrayList<>();
        toolCards.add(new ToolCard(Color.BLUE, "name", "descr", "[1-CA]"));

        doThrow(IOException.class).when(view).ack(anyString());

        toolCards.add(new ToolCard(Color.PURPLE, "Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6", "[1-Choose dice]" +
                "[2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]"));

        when(game.getToolCards()).thenReturn(toolCards);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").createGameNameMessage(gameName).
                createToolCardMessage(new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                        "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                        ColorWrapper.PURPLE, 0)).buildMessage();

        gameController.bindToolCard(clientMessage, toolCardObserver);
    }

    @Test(expected = IOException.class)
    public void testUserBindToolCardInitCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").createGameNameMessage(gameName).
                createToolCardMessage(new ToolCardWrapper("name", "descr", ColorWrapper.BLUE, 2)).buildMessage();

        gameController.bindToolCard(clientMessage, toolCardObserver);
    }

    @Test
    public void testUserBindToolCardGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createUsernameMessage("username").createGameNameMessage(gameName).
                createToolCardMessage(new ToolCardWrapper("name", "descr", ColorWrapper.BLUE, 2)).buildMessage();

        gameController.bindToolCard(clientMessage, toolCardObserver);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }

    @Test(expected = IOException.class)
    public void testPlaceDiceCardInitialCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.placeDice(clientMessage);
    }

    @Test
    public void testUserPlaceDiceTerminateGameDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.placeDice(clientMessage);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }

    @Test
    public void testPlaceDiceWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.placeDice(clientMessage);

        verify(view).err(anyString());
    }

    @Test
    public void testPlaceDice() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.placeDice(clientMessage);

        verify(game).userPlaceDice(tokenUser, new Dice(5, Color.BLUE), new Position(1, 1));
    }

    @Test
    public void testPlaceDiceInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(new InvalidActionException(new RuleViolationException(RuleViolationType.TILE_UNMATCHED))).when(game).userPlaceDice(
                tokenUser, new Dice(5, Color.BLUE), new Position(1, 1));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.placeDice(clientMessage);

        verify(game).userPlaceDice(tokenUser, new Dice(5, Color.BLUE), new Position(1, 1));

        verify(view).err(anyString());
    }

    @Test
    public void testPlaceDiceInvalidActionNoneRuleViolationException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(new InvalidActionException()).when(game).userPlaceDice(
                tokenUser, new Dice(5, Color.BLUE), new Position(1, 1));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.placeDice(clientMessage);

        verify(game).userPlaceDice(tokenUser, new Dice(5, Color.BLUE), new Position(1, 1));

        verify(view).err(anyString());
    }

    @Test
    public void testPlaceDiceInvalidActionErrorACKing() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(new InvalidActionException()).when(game).userPlaceDice(
                tokenUser, new Dice(5, Color.BLUE), new Position(1, 1));

        doThrow(IOException.class).when(view).err(anyString());
        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.placeDice(clientMessage);

        verify(game).userPlaceDice(tokenUser, new Dice(5, Color.BLUE), new Position(1, 1));

        verify(view).err(anyString());
        verifyHandleIOException();
    }


    @Test
    public void testChooseActionWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createActionMessage(new EndTurnActionWrapper()).buildMessage();
        gameController.chooseAction(clientMessage);

        verify(view).err(anyString());
    }

    @Test
    public void testChooseAction() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);


        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createActionMessage(new EndTurnActionWrapper()).buildMessage();
        gameController.chooseAction(clientMessage);

        verify(game).userChooseAction(tokenUser, new EndTurnAction());
    }


    @Test
    public void testChooseActionErrorACKing() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        List<ToolCard> toolCards = new ArrayList<>();
        toolCards.add(new ToolCard(Color.BLUE, "name", "descr", "[1-CA]"));

        doThrow(IOException.class).when(view).ack(anyString());

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createActionMessage(new EndTurnActionWrapper()).buildMessage();
        gameController.chooseAction(clientMessage);
    }

    @Test
    public void testChooseActionInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userChooseAction(tokenUser, new EndTurnAction());

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createActionMessage(new EndTurnActionWrapper()).buildMessage();
        gameController.chooseAction(clientMessage);

        verify(game).userChooseAction(tokenUser, new EndTurnAction());

        verify(view).err(anyString());
    }

    @Test
    public void testChooseActionInvalidExceptionAndErrorSignalingToView() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userChooseAction(tokenUser, new EndTurnAction());
        doThrow(IOException.class).when(view).err(anyString());

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createActionMessage(new EndTurnActionWrapper()).buildMessage();
        gameController.chooseAction(clientMessage);

        verify(game).userChooseAction(tokenUser, new EndTurnAction());
        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testChooseActionToolCardInitCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createActionMessage(new EndTurnActionWrapper()).buildMessage();
        gameController.chooseAction(clientMessage);
    }

    @Test
    public void testChooseActionCardGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createActionMessage(new EndTurnActionWrapper()).buildMessage();
        gameController.chooseAction(clientMessage);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }

    @Test
    public void testUseToolCardWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);
        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        gameController.useToolCard(clientMessage, toolCardExecutorObserver);

        verify(view).err(anyString());
    }

    @Test
    public void testUseToolCard() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);


        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);
        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        gameController.useToolCard(clientMessage, toolCardExecutorObserver);

        verify(view, times(0)).err(anyString());
    }


    @Test
    public void testUseToolCardInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userChooseAction(tokenUser, new EndTurnAction());

        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);
        ToolCard toolCard = new ToolCard(Color.PURPLE, "Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6", "[1-Choose dice]" +
                "[2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]");
        doThrow(InvalidActionException.class).when(game).userUseToolCard(eq(tokenUser), eq(toolCard), any(IToolCardExecutorFakeObserver.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        gameController.useToolCard(clientMessage, toolCardExecutorObserver);

        verify(view).err(anyString());
    }

    @Test
    public void testUseToolCardInvalidExceptionAndErrorSignalingToView() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        ToolCard toolCard = new ToolCard(Color.PURPLE, "Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6", "[1-Choose dice]" +
                "[2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]");
        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);

        doThrow(InvalidActionException.class).when(game).userUseToolCard(eq(tokenUser), eq(toolCard), any(IToolCardExecutorFakeObserver.class));
        doThrow(IOException.class).when(view).err(anyString());

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        gameController.useToolCard(clientMessage, toolCardExecutorObserver);

        verify(view).err(anyString());
        verifyHandleIOException();
    }

    @Test(expected = IOException.class)
    public void testUseToolCardInitCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);
        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        gameController.useToolCard(clientMessage, toolCardExecutorObserver);
    }

    @Test
    public void testUseToolCardGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);
        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        gameController.useToolCard(clientMessage, toolCardExecutorObserver);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }


    @Test
    public void testChoosePrivateCardWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPrivateObjectiveCardMessage(privateObjectiveCardWrapper)
                .buildMessage();
        gameController.choosePrivateObjectiveCard(clientMessage);

        verify(view).err(anyString());
    }

    @Test
    public void testChoosePrivateCard() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);


        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPrivateObjectiveCardMessage(privateObjectiveCardWrapper)
                .buildMessage();
        gameController.choosePrivateObjectiveCard(clientMessage);

        verify(game).userChoosePrivateObjectiveCard(tokenUser, privateObjectiveCard);

        verify(view, times(0)).err(anyString());
    }


    @Test
    public void testChoosePrivateCardInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userChoosePrivateObjectiveCard(tokenUser, privateObjectiveCard);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPrivateObjectiveCardMessage(privateObjectiveCardWrapper)
                .buildMessage();
        gameController.choosePrivateObjectiveCard(clientMessage);

        verify(game).userChoosePrivateObjectiveCard(tokenUser, privateObjectiveCard);

        verify(view).err(anyString());
    }

    @Test
    public void testChoosePrivateCardInvalidExceptionAndErrorSignalingToView() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).err(anyString());
        doThrow(InvalidActionException.class).when(game).userChoosePrivateObjectiveCard(tokenUser, privateObjectiveCard);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPrivateObjectiveCardMessage(privateObjectiveCardWrapper)
                .buildMessage();
        gameController.choosePrivateObjectiveCard(clientMessage);

        verify(game).userChoosePrivateObjectiveCard(tokenUser, privateObjectiveCard);

        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testChoosePrivateCardInitCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPrivateObjectiveCardMessage(privateObjectiveCardWrapper)
                .buildMessage();
        gameController.choosePrivateObjectiveCard(clientMessage);
    }

    @Test
    public void testChoosePrivateCardGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPrivateObjectiveCardMessage(privateObjectiveCardWrapper)
                .buildMessage();
        gameController.choosePrivateObjectiveCard(clientMessage);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }

    @Test
    public void testSetDiceWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).buildMessage();
        gameController.setDice(clientMessage);

        verify(view).err(anyString());
    }

    @Test
    public void testSetDice() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).buildMessage();
        gameController.setDice(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(DiceExecutorEvent.class));
    }

    @Test
    public void testSetDiceInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(DiceExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).buildMessage();
        gameController.setDice(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(DiceExecutorEvent.class));

        verify(view).err(anyString());
    }

    @Test
    public void testSetDiceInvalidExceptionAndErrorSignalingToView() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).err(anyString());
        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(DiceExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).buildMessage();
        gameController.setDice(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(DiceExecutorEvent.class));

        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testSetDiceToolCardInitCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).buildMessage();
        gameController.setDice(clientMessage);

    }

    @Test
    public void testSetDiceGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(DiceExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createDiceMessage(new DiceWrapper(ColorWrapper.BLUE, 5)).buildMessage();
        gameController.setDice(clientMessage);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }


    @Test
    public void testSetValueWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createValueMessage(5).buildMessage();
        gameController.setNewValue(clientMessage);

        verify(view).err(anyString());
    }

    @Test
    public void testSetValue() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createValueMessage(5).buildMessage();
        gameController.setNewValue(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(ValueExecutorEvent.class));
        verify(view, times(0)).err(anyString());
    }

    @Test
    public void testSetValueInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(ValueExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createValueMessage(5).buildMessage();
        gameController.setNewValue(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(ValueExecutorEvent.class));

        verify(view).err(anyString());
    }

    @Test
    public void testSetValueInvalidExceptionAndErrorSignalingToView() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).err(anyString());
        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(ValueExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createValueMessage(5).buildMessage();
        gameController.setNewValue(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(ValueExecutorEvent.class));

        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testSetValueToolCardInitCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createValueMessage(5).buildMessage();
        gameController.setNewValue(clientMessage);
    }

    @Test
    public void testSetValueGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createColorMessage(ColorWrapper.YELLOW).buildMessage();
        gameController.setColor(clientMessage);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }

    @Test
    public void testSetColorWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createColorMessage(ColorWrapper.YELLOW).buildMessage();
        gameController.setColor(clientMessage);

        verify(view).err(anyString());
    }

    @Test
    public void testSetColor() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createColorMessage(ColorWrapper.YELLOW).buildMessage();
        gameController.setColor(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(ColorExecutorEvent.class));
        verify(view, times(0)).err(anyString());
    }

    @Test
    public void testSetColorInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(ColorExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createColorMessage(ColorWrapper.YELLOW).buildMessage();
        gameController.setColor(clientMessage);
        verify(game).userFireExecutorEvent(eq(tokenUser), any(ColorExecutorEvent.class));

        verify(view).err(anyString());
    }

    @Test
    public void testSetColorInvalidExceptionAndErrorSignalingToView() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).err(anyString());
        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(ColorExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createColorMessage(ColorWrapper.YELLOW).buildMessage();
        gameController.setColor(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(ColorExecutorEvent.class));

        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testSetColorInitCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createColorMessage(ColorWrapper.YELLOW).buildMessage();
        gameController.setColor(clientMessage);
    }

    @Test
    public void testSetColorGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.setPosition(clientMessage);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }


    @Test
    public void testSetPositionWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.setPosition(clientMessage);

        verify(view).err(anyString());
    }

    @Test
    public void testSetPosition() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.setPosition(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(PositionExecutorEvent.class));
        verify(view, times(0)).err(anyString());
    }

    @Test
    public void testSetPositionInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(PositionExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.setPosition(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(PositionExecutorEvent.class));

        verify(view).err(anyString());
    }

    @Test
    public void testSetPositionInvalidExceptionAndErrorSignalingToView() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).err(anyString());
        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(PositionExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.setPosition(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(PositionExecutorEvent.class));

        verifyHandleIOException();
    }

    @Test(expected = IOException.class)
    public void testSetPositionInitCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.setPosition(clientMessage);
    }

    @Test
    public void testSetPositionGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(ColorExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createPositionMessage(new PositionWrapper(1, 1)).buildMessage();
        gameController.setPosition(clientMessage);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }

    @Test
    public void testSetAnswerWasDisconnectedUser() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createBooleanMessage(false).buildMessage();
        gameController.setContinueAction(clientMessage);

        verify(view).err(anyString());
    }

    @Test
    public void testSetAnswer() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createBooleanMessage(false).buildMessage();
        gameController.setContinueAction(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(AnswerExecutorEvent.class));
        verify(view, times(0)).err(anyString());
    }

    @Test
    public void testSetAnswerInvalidException() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(AnswerExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createBooleanMessage(false).buildMessage();
        gameController.setContinueAction(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(AnswerExecutorEvent.class));

        verify(view).err(anyString());
    }

    @Test
    public void testSetAnswerInvalidExceptionAndErrorSignalingToView() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).err(anyString());
        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(AnswerExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createBooleanMessage(true).buildMessage();
        gameController.setContinueAction(clientMessage);

        verify(game).userFireExecutorEvent(eq(tokenUser), any(AnswerExecutorEvent.class));

        verifyHandleIOException();
    }

    @Test(expected = IOException.class)
    public void testSetAnswerInitCheckFailed() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.notContainsGame(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createBooleanMessage(false).buildMessage();
        gameController.setContinueAction(clientMessage);
    }

    @Test
    public void testSetAnswerGameEndedDetected() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenOfPlayerPlayingInGame);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        doThrow(InvalidActionException.class).when(game).userFireExecutorEvent(eq(tokenUser), any(AnswerExecutorEvent.class));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName)
                .createBooleanMessage(false).buildMessage();
        gameController.setContinueAction(clientMessage);

        verify(view).err(anyString());
        verify(gameNetworkManager).removeView(tokenUser);
    }

    @Test(expected = IOException.class)
    public void testReconnectNotPlayingUser() throws Exception {
        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(5), schemaCard,
                privateObjectiveCards));

        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.getPlayers()).thenReturn(playerList);
        when(game.getName()).thenReturn(gameName);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerObserverMap.putIfAbsent(tokenUser, playerObserver1);
        playerObserverMap.putIfAbsent(tokenUser2, playerObserver2);

        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardObserverMap.putIfAbsent("Pennello per Eglomise", toolCardObserver);

        Map<String, ISchemaCardObserver> stringISchemaCardObserverMap = new HashMap<>();
        stringISchemaCardObserverMap.putIfAbsent(tokenUser, schemaCardObserver1);
        stringISchemaCardObserverMap.putIfAbsent(tokenUser2, schemaCardObserver2);

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        gameController.reconnect(clientMessage, view, gameObserver, roundTrackObserver, stateObserver, playerObserverMap, toolCardObserverMap,
                stringISchemaCardObserverMap, draftPoolObserver, diceBagObserver,
                timeOutObserver);
        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testReconnectTerminatedGame() throws Exception {
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);
        when(game.getName()).thenReturn(gameName);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerObserverMap.putIfAbsent(tokenUser, playerObserver1);
        playerObserverMap.putIfAbsent(tokenUser2, playerObserver2);

        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardObserverMap.putIfAbsent("Pennello per Eglomise", toolCardObserver);

        Map<String, ISchemaCardObserver> stringISchemaCardObserverMap = new HashMap<>();
        stringISchemaCardObserverMap.putIfAbsent(tokenUser, schemaCardObserver1);
        stringISchemaCardObserverMap.putIfAbsent(tokenUser2, schemaCardObserver2);

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        gameController.reconnect(clientMessage, view, gameObserver, roundTrackObserver, stateObserver, playerObserverMap, toolCardObserverMap,
                stringISchemaCardObserverMap, draftPoolObserver, diceBagObserver,
                timeOutObserver);

        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testReconnectNotDisconnectedUser() throws Exception {
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);

        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerObserverMap.putIfAbsent(tokenUser, playerObserver1);
        playerObserverMap.putIfAbsent(tokenUser2, playerObserver2);

        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardObserverMap.putIfAbsent("Pennello per Eglomise", toolCardObserver);

        Map<String, ISchemaCardObserver> stringISchemaCardObserverMap = new HashMap<>();
        stringISchemaCardObserverMap.putIfAbsent(tokenUser, schemaCardObserver1);
        stringISchemaCardObserverMap.putIfAbsent(tokenUser2, schemaCardObserver2);

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        gameController.reconnect(clientMessage, view, gameObserver, roundTrackObserver, stateObserver, playerObserverMap, toolCardObserverMap,
                stringISchemaCardObserverMap, draftPoolObserver, diceBagObserver,
                timeOutObserver);

        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testReconnectWrongPlayerObservers() throws Exception {
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);

        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerObserverMap.putIfAbsent(userName, playerObserver1);
        playerObserverMap.putIfAbsent("wrongUsername", playerObserver2);

        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardObserverMap.putIfAbsent("Pennello per Eglomise", toolCardObserver);

        Map<String, ISchemaCardObserver> stringISchemaCardObserverMap = new HashMap<>();
        stringISchemaCardObserverMap.putIfAbsent(userName, schemaCardObserver1);
        stringISchemaCardObserverMap.putIfAbsent(userName2, schemaCardObserver2);

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        gameController.reconnect(clientMessage, view, gameObserver, roundTrackObserver, stateObserver, playerObserverMap, toolCardObserverMap,
                stringISchemaCardObserverMap, draftPoolObserver, diceBagObserver,
                timeOutObserver);

        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testReconnectWrongSchemaCardObservers() throws Exception {
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);

        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerObserverMap.putIfAbsent(userName, playerObserver1);
        playerObserverMap.putIfAbsent(userName2, playerObserver2);

        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardObserverMap.putIfAbsent("Pennello per Eglomise", toolCardObserver);

        Map<String, ISchemaCardObserver> stringISchemaCardObserverMap = new HashMap<>();
        stringISchemaCardObserverMap.putIfAbsent(userName, schemaCardObserver1);
        stringISchemaCardObserverMap.putIfAbsent("notExistingName", schemaCardObserver2);

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        gameController.reconnect(clientMessage, view, gameObserver, roundTrackObserver, stateObserver, playerObserverMap, toolCardObserverMap,
                stringISchemaCardObserverMap, draftPoolObserver, diceBagObserver,
                timeOutObserver);

        verify(view).err(anyString());
    }

    @Test(expected = IOException.class)
    public void testReconnectWrongToolCardObservers() throws Exception {
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(game.getToolCards()).thenReturn(Collections.singletonList(new ToolCard(Color.BLUE,
                "Pennello per Eglomise", "descr", "[1-CA]")));

        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerObserverMap.putIfAbsent(userName, playerObserver1);
        playerObserverMap.putIfAbsent(userName2, playerObserver2);

        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardObserverMap.putIfAbsent("Pennello per Eglomise fake", toolCardObserver);

        Map<String, ISchemaCardObserver> stringISchemaCardObserverMap = new HashMap<>();
        stringISchemaCardObserverMap.putIfAbsent(userName, schemaCardObserver1);
        stringISchemaCardObserverMap.putIfAbsent(userName2, schemaCardObserver2);

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        gameController.reconnect(clientMessage, view, gameObserver, roundTrackObserver, stateObserver, playerObserverMap, toolCardObserverMap,
                stringISchemaCardObserverMap, draftPoolObserver, diceBagObserver,
                timeOutObserver);

        verify(view).err(anyString());
    }

    @Test
    public void testReconnect() throws Exception {
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(game.getToolCards()).thenReturn(Collections.singletonList(new ToolCard(Color.BLUE,
                "Pennello per Eglomise", "descr", "[1-CA]")));
        when(gameNetworkManager.getViewByToken(tokenUser2)).thenReturn(view2);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);


        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerObserverMap.putIfAbsent(userName, playerObserver1);
        playerObserverMap.putIfAbsent(userName2, playerObserver2);

        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardObserverMap.putIfAbsent("Pennello per Eglomise", toolCardObserver);

        Map<String, ISchemaCardObserver> stringISchemaCardObserverMap = new HashMap<>();
        stringISchemaCardObserverMap.putIfAbsent(userName, schemaCardObserver1);
        stringISchemaCardObserverMap.putIfAbsent(userName2, schemaCardObserver2);

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        gameController.reconnect(clientMessage, view, gameObserver, roundTrackObserver, stateObserver, playerObserverMap, toolCardObserverMap,
                stringISchemaCardObserverMap, draftPoolObserver, diceBagObserver,
                timeOutObserver);

        verify(gameNetworkManager, times(1)).putView(tokenUser, view);
        verify(view).ack(anyString());
        verify(view2).ack(anyString());
    }

    @Test
    public void testReconnectErrorACKing() throws Exception {
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(game.getToolCards()).thenReturn(Collections.singletonList(new ToolCard(Color.BLUE,
                "Pennello per Eglomise", "descr", "[1-CA]")));
        when(gameNetworkManager.getViewByToken(tokenUser2)).thenReturn(view2);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view).ack(anyString());

        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerObserverMap.putIfAbsent(userName, playerObserver1);
        playerObserverMap.putIfAbsent(userName2, playerObserver2);

        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardObserverMap.putIfAbsent("Pennello per Eglomise", toolCardObserver);

        Map<String, ISchemaCardObserver> stringISchemaCardObserverMap = new HashMap<>();
        stringISchemaCardObserverMap.putIfAbsent(userName, schemaCardObserver1);
        stringISchemaCardObserverMap.putIfAbsent(userName2, schemaCardObserver2);

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        gameController.reconnect(clientMessage, view, gameObserver, roundTrackObserver, stateObserver, playerObserverMap, toolCardObserverMap,
                stringISchemaCardObserverMap, draftPoolObserver, diceBagObserver,
                timeOutObserver);

        verify(gameNetworkManager, times(1)).putView(tokenUser, view);
        verify(view).ack(anyString());
        verify(view2, times(0)).ack(anyString());
        verifyHandleIOException();
    }

    @Test
    public void testReconnectErrorACKingOtherPlayers() throws Exception {
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(5), schemaCard,
                privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(game.getToolCards()).thenReturn(Collections.singletonList(new ToolCard(Color.BLUE,
                "Pennello per Eglomise", "descr", "[1-CA]")));
        when(gameNetworkManager.getViewByToken(tokenUser2)).thenReturn(view2);
        when(gameNetworkManager.getViewByToken(tokenUser)).thenReturn(view);

        doThrow(IOException.class).when(view2).ack(anyString());

        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerObserverMap.putIfAbsent(userName, playerObserver1);
        playerObserverMap.putIfAbsent(userName2, playerObserver2);

        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardObserverMap.putIfAbsent("Pennello per Eglomise", toolCardObserver);

        Map<String, ISchemaCardObserver> stringISchemaCardObserverMap = new HashMap<>();
        stringISchemaCardObserverMap.putIfAbsent(userName, schemaCardObserver1);
        stringISchemaCardObserverMap.putIfAbsent(userName2, schemaCardObserver2);

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        gameController.reconnect(clientMessage, view, gameObserver, roundTrackObserver, stateObserver, playerObserverMap, toolCardObserverMap,
                stringISchemaCardObserverMap, draftPoolObserver, diceBagObserver,
                timeOutObserver);

        verify(gameNetworkManager, times(1)).putView(tokenUser, view);
        verify(view).ack(anyString());
        verify(gameObserverManager).signalDisconnection(tokenUser2);
        verify(gameNetworkManager).removeToken(tokenUser2);
    }

    @Test(expected = Exception.class)
    public void testAttemptReconnectOfNotPlayingUser() throws Exception {
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser);
        tokenList.add(tokenUser2);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));

        String clientMessage = clientCreateMessage.createUsernameMessage("notExistingUsername").buildMessage();
        String response = gameController.attemptReconnect(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getGameName(response);
    }

    @Test
    public void testAttemptReConnect() throws Exception {
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser);
        tokenList.add(tokenUser2);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        String response = gameController.attemptReconnect(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        assertEquals(gameName, clientGetMessage.getGameName(response));
    }

    @Test(expected = Exception.class)
    public void testAttemptReconnectOfNonDisconnectedUser() throws Exception {
        when(game.getName()).thenReturn(gameName);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser);
        tokenList.add(tokenUser2);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameList()).thenReturn(Collections.singletonList(game));
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());

        String clientMessage = clientCreateMessage.createUsernameMessage(userName).buildMessage();
        String response = gameController.attemptReconnect(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getGameName(response);
    }

    @Test(expected = Exception.class)
    public void testGetToolCardsNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getToolCards(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getToolCards(response);
    }

    @Test(expected = Exception.class)
    public void testGetToolCardsTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getToolCards(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getToolCards(response);
    }

    @Test
    public void testGetToolCards() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        List<ToolCard> toolCards = new ArrayList<>();
        toolCards.add(new ToolCard(Color.BLUE, "Name", "Descr", "[1-CA]"));

        when(game.getToolCards()).thenReturn(toolCards);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getToolCards(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        List<ToolCardWrapper> toolCardWrapperList = clientGetMessage.getToolCards(response);

        assertEquals(toolCards.size(), toolCardWrapperList.size());
        assertEquals(toolCards.get(0).getName(), toolCardWrapperList.get(0).getName());
        assertEquals(toolCards.get(0).getDescription(), toolCardWrapperList.get(0).getDescription());
        assertEquals(toolCards.get(0).getColor().toString(), toolCardWrapperList.get(0).getColor().toString());
        assertEquals(0, toolCardWrapperList.get(0).getToken());
    }


    @Test(expected = Exception.class)
    public void testGetPlayersCoinsNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPlayersCoins(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getPlayersCoins(response);
    }

    @Test(expected = Exception.class)
    public void testGetPlayersCoinSinglePlayer() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPlayersCoins(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getPlayersCoins(response);

    }

    @Test(expected = Exception.class)
    public void testGetPlayersCoinsTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPlayersCoins(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getPlayersCoins(response);
    }

    @Test
    public void testGetPlayersCoins() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);


        List<Player> multiPlayerList = new ArrayList<>();
        multiPlayerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(2), schemaCard, privateObjectiveCards));
        multiPlayerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(3), schemaCard, privateObjectiveCards));
        when(game.getPlayers()).thenReturn(multiPlayerList);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPlayersCoins(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        Map<UserWrapper, Integer> actualmap = clientGetMessage.getPlayersCoins(response);
        assertEquals(2, actualmap.size());
        assertEquals(new Integer(2), actualmap.get(new UserWrapper(userName)));
        assertEquals(new Integer(3), actualmap.get(new UserWrapper(userName2)));
    }

    @Test(expected = Exception.class)
    public void testGetMyCoinsNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getMyCoins(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getMyCoins(response);
    }

    @Test(expected = Exception.class)
    public void testGetMyCoinSinglePlayer() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(true);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getMyCoins(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getMyCoins(response);

    }

    @Test(expected = Exception.class)
    public void testGetMyCoinsTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getMyCoins(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getMyCoins(response);
    }

    @Test
    public void testGetMyCoins() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);


        List<Player> multiPlayerList = new ArrayList<>();
        multiPlayerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(2), schemaCard, privateObjectiveCards));
        multiPlayerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(3), schemaCard, privateObjectiveCards));
        when(game.getPlayers()).thenReturn(multiPlayerList);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getMyCoins(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        assertEquals(new Integer(2), clientGetMessage.getMyCoins(response));
    }

    @Test(expected = Exception.class)
    public void testGetMyCoinsNotPresentPlayer() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        List<Player> multiPlayerList = new ArrayList<>();
        multiPlayerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(3), schemaCard, privateObjectiveCards));
        when(game.getPlayers()).thenReturn(multiPlayerList);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getMyCoins(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        clientGetMessage.getMyCoins(response);
    }

    @Test(expected = Exception.class)
    public void testGetPublicCardsNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPublicObjectiveCards(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getPublicObjectiveCards(response);
    }

    @Test(expected = Exception.class)
    public void testGetPublicCardsTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPublicObjectiveCards(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getPublicObjectiveCards(response);
    }

    @Test
    public void testGetPublicCards() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        List<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();
        publicObjectiveCards.add(new RowPublicObjectiveCard("name", "descr", 2, ObjectiveCardType.NUMBER));
        when(game.getPublicObjectiveCards()).thenReturn(publicObjectiveCards);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPublicObjectiveCards(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        List<PublicObjectiveCardWrapper> publicObjectiveCardWrapperList = clientGetMessage.getPublicObjectiveCards(response);
        assertEquals(1, publicObjectiveCardWrapperList.size());
        assertEquals("name", publicObjectiveCardWrapperList.get(0).getName());
        assertEquals("descr", publicObjectiveCardWrapperList.get(0).getDescription());
        assertEquals(2, publicObjectiveCardWrapperList.get(0).getCardPoint());
    }

    @Test(expected = Exception.class)
    public void testGetPrivateCardByTokenNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPrivateObjectiveCardByToken(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getPrivateObjectiveCards(response);
    }

    @Test(expected = Exception.class)
    public void testGetPrivateCardByTokenTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPrivateObjectiveCardByToken(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getPrivateObjectiveCards(response);
    }

    @Test
    public void testGetPrivateCardByToken() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        when(game.getPrivateObjectiveCardsByToken(tokenUser)).thenReturn(privateObjectiveCards);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getPrivateObjectiveCardByToken(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        List<PrivateObjectiveCardWrapper> actualValues = clientGetMessage.getPrivateObjectiveCards(response);
        assertEquals(1, actualValues.size());
        assertEquals(privateObjectiveCards.get(0).getName(), actualValues.get(0).getName());
        assertEquals(privateObjectiveCards.get(0).getDescription(), actualValues.get(0).getDescription());
        assertEquals(privateObjectiveCards.get(0).getConstraint().getColor().toString(), actualValues.get(0).getColor().toString());
    }

    @Test(expected = Exception.class)
    public void testGetSchemaCardsNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getSchemaCards(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getSchemaCards(response);
    }

    @Test(expected = Exception.class)
    public void testGetSchemaCardsTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getSchemaCards(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getSchemaCards(response);
    }

    @Test
    public void testGetSchemaCards() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(2), schemaCard, privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(2), schemaCard, privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getSchemaCards(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        Map<UserWrapper, SchemaCardWrapper> actualMap = clientGetMessage.getSchemaCards(response);
        assertEquals(2, actualMap.size());
        assertEquals(schemaCardWrapper, actualMap.get(new UserWrapper(userName)));
        assertEquals(schemaCardWrapper, actualMap.get(new UserWrapper(userName2)));
    }

    @Test(expected = Exception.class)
    public void testGetSchemaCardByTokenNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getSchemaCardByToken(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getSchemaCard(response);
    }

    @Test(expected = Exception.class)
    public void testGetSchemaCardByTokenTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getSchemaCardByToken(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getSchemaCard(response);
    }

    @Test
    public void testGetSchemaCardByToken() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(new User(userName, tokenUser), new FavorToken(2), schemaCard, privateObjectiveCards));
        playerList.add(new MultiPlayer(new User(userName2, tokenUser2), new FavorToken(2), schemaCard, privateObjectiveCards));
        when(game.getPlayers()).thenReturn(playerList);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getSchemaCardByToken(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        assertEquals(schemaCardWrapper, clientGetMessage.getSchemaCard(response));
    }


    @Test(expected = Exception.class)
    public void testGetDraftPoolNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getDraftPool(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getDraftPool(response);
    }

    @Test(expected = Exception.class)
    public void testGetDraftPoolTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getDraftPool(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getDraftPool(response);
    }

    @Test
    public void testGetDraftPool() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        DraftPool draftPool = new DraftPool();
        draftPool.addDice(new Dice(4, Color.BLUE));
        when(game.getDraftPool()).thenReturn(draftPool);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getDraftPool(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        DraftPoolWrapper draftPoolWrapper = clientGetMessage.getDraftPool(response);
        assertEquals(draftPool.size(), draftPoolWrapper.getDices().size());
        assertEquals(new DiceWrapper(ColorWrapper.BLUE, 4), draftPoolWrapper.getDice(0));
    }

    @Test(expected = Exception.class)
    public void testGetRoundTrackNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getRoundTrack(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getRoundTrack(response);
    }

    @Test(expected = Exception.class)
    public void testGetRoundTrackTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getRoundTrack(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getRoundTrack(response);
    }

    @Test
    public void testGetRoundTrack() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        RoundTrack roundTrack = new RoundTrack();
        roundTrack.addDiceToRound(new Dice(1, Color.BLUE), RoundTrack.FIRST_ROUND);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getRoundTrack(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        RoundTrackWrapper roundTrackWrapper = clientGetMessage.getRoundTrack(response);
        assertEquals(new DiceWrapper(ColorWrapper.BLUE, 1), roundTrackWrapper.getDicesForRound(0).get(0));
        assertEquals(1, roundTrack.getDices(0).size());
        for (int i = 1; i < RoundTrackWrapper.NUMBER_OF_TRACK; i++) {
            assertEquals(0, roundTrackWrapper.getDicesForRound(i).size());
        }
    }

    @Test(expected = Exception.class)
    public void testGetToolCardByNameNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);
        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createToolCardMessage(toolCardWrapper).
                createGameNameMessage(gameName).buildMessage();
        String response = gameController.getToolCardByName(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getToolCard(response);
    }

    @Test(expected = Exception.class)
    public void testGetToolCardByNameTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);
        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createToolCardMessage(toolCardWrapper).
                createGameNameMessage(gameName).buildMessage();
        String response = gameController.getToolCardByName(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getToolCard(response);
    }

    @Test
    public void testGetToolCardByName() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);

        ToolCard toolCard = new ToolCard(Color.PURPLE, "Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6", "[1-CA]");
        when(game.getToolCards()).thenReturn(Collections.singletonList(toolCard));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createToolCardMessage(toolCardWrapper).
                createGameNameMessage(gameName).buildMessage();
        String response = gameController.getToolCardByName(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        assertEquals(toolCardWrapper, clientGetMessage.getToolCard(response));
    }

    @Test(expected = Exception.class)
    public void testGetToolCardByNameNotExistingToolCard() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("Pinza Sgrossatrice fake", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);

        ToolCard toolCard = new ToolCard(Color.PURPLE, "Pinza Sgrossatrice", "Dopo aver scelto un dado, " +
                "aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6", "[1-CA]");
        when(game.getToolCards()).thenReturn(Collections.singletonList(toolCard));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createToolCardMessage(toolCardWrapper).
                createGameNameMessage(gameName).buildMessage();
        String response = gameController.getToolCardByName(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        clientGetMessage.getToolCard(response);
    }

    @Test(expected = Exception.class)
    public void testGetCurrentPlayerNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getCurrentPlayer(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getUserWrapper(response);
    }

    @Test(expected = Exception.class)
    public void testGetCurrentPlayerTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getCurrentPlayer(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getUserWrapper(response);
    }

    @Test
    public void testGetCurrentPlayer() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        when(game.getCurrentPlayer()).thenReturn(new MultiPlayer(new User(userName, tokenUser), new FavorToken(0),
                schemaCard, privateObjectiveCards));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getCurrentPlayer(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        assertEquals(new UserWrapper(userName), clientGetMessage.getUserWrapper(response));
    }

    @Test(expected = Exception.class)
    public void testGetCurrentPlayerInvalidAction() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        doThrow(InvalidActionException.class).when(game.getCurrentPlayer());

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getCurrentPlayer(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        clientGetMessage.getUserWrapper(response);
    }

    @Test(expected = Exception.class)
    public void testGetListOfUsersNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getListOfUser(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getListOfUserWrapper(response);
    }

    @Test(expected = Exception.class)
    public void testGetListOfUsersTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getListOfUser(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getListOfUserWrapper(response);
    }

    @Test
    public void testGetListOfUsers() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        List<User> userList = new ArrayList<>();
        userList.add(new User(userName, tokenUser));
        userList.add(new User(userName2, tokenUser2));
        when(game.getUsers()).thenReturn(userList);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getListOfUser(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();

        Set<UserWrapper> userWrapperSet = new HashSet<>();
        userWrapperSet.add(new UserWrapper(userName));
        userWrapperSet.add(new UserWrapper(userName2));

        assertEquals(userWrapperSet, new HashSet<UserWrapper>(clientGetMessage.getListOfUserWrapper(response)));
    }

    @Test(expected = Exception.class)
    public void testGetTimeoutNotContainedTokenInGameNetworkManager() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(false);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(Collections.singleton(tokenUser));
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.isSinglePlayer()).thenReturn(false);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getTimeout(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getTimeout(response);
    }

    @Test(expected = Exception.class)
    public void testGetTimeoutTerminatedGame() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getTimeout(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        clientGetMessage.getTimeout(response);
    }

    @Test
    public void testGetTimeout() throws Exception {
        when(gameNetworkManager.containsToken(tokenUser)).thenReturn(true);
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameObserverManager.getDisconnectedPlayer()).thenReturn(new HashSet<>());
        when(gameManager.notContainsGame(gameName)).thenReturn(false);
        List<String> tokenList = new ArrayList<>();
        tokenList.add(tokenUser2);
        tokenList.add(tokenUser);
        when(gameManager.getPlayersByGame(gameName)).thenReturn(tokenList);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(gameObserverManager.getTimeToTimeout()).thenReturn(new Long(6000));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        String response = gameController.getTimeout(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        assertEquals("00:06", clientGetMessage.getTimeout(response));

    }

    @Test
    public void testCreateSinglePlayer() throws Exception {
        when(gameManager.createSinglePlayerGame(userName, 2)).thenReturn("game");
        String clientMessage = clientCreateMessage.createUsernameMessage(userName).createValueMessage(2).buildMessage();
        String response = gameController.createSinglePlayer(clientMessage);
        ClientGetMessage clientGetMessage = new ClientGetMessage();
        assertEquals("game", clientGetMessage.getGameName(response));
        assertEquals(tokenUser, clientGetMessage.getToken(response));
    }

    @Test
    public void testQuitGame() throws Exception {
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(false);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.getCurrentPlayer()).thenReturn(new MultiPlayer(new User(userName, tokenUser), new FavorToken(3), schemaCard, privateObjectiveCards));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        gameController.quitGame(clientMessage);
        verify(gameObserverManager).signalDisconnection(tokenUser);
        verify(game).userChooseAction(eq(tokenUser), any(EndTurnAction.class));
    }

    @Test
    public void testQuiGameEndGame() throws Exception {
        when(gameManager.getObserverManagerByGame(gameName)).thenReturn(gameObserverManager);
        when(gameNetworkManager.clearObservers(gameName)).thenReturn(true);
        when(gameManager.getGameByName(gameName)).thenReturn(game);
        when(game.getCurrentPlayer()).thenReturn(new MultiPlayer(new User(userName, tokenUser), new FavorToken(3), schemaCard, privateObjectiveCards));

        String clientMessage = clientCreateMessage.createTokenMessage(tokenUser).createGameNameMessage(gameName).buildMessage();
        gameController.quitGame(clientMessage);
        verify(gameObserverManager).signalDisconnection(tokenUser);
        verify(game, times(0)).userChooseAction(eq(tokenUser), any(EndTurnAction.class));

    }



    private void verifyHandleIOException() {
        verify(gameObserverManager).signalDisconnection(tokenUser);
        verify(gameNetworkManager).removeToken(tokenUser);
    }
}
