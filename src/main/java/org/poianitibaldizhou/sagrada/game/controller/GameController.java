package org.poianitibaldizhou.sagrada.game.controller;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.*;
import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.*;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.EndTurnAction;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.GameNetworkManager;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ServerGetMessage;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @see IGameController
 */
public class GameController extends UnicastRemoteObject implements IGameController {

    private static final transient String FIRE_EVENT_ERROR = "Can't fire an event now";
    private static final transient String INVALID_ACTION_ERR = "You can't perform this action now";
    private static final transient String YOU_NEED_TO_RECONNECT = "You need to reconnect";
    private static final transient String GAME_TERMINATED = "The game has terminated";

    private final transient GameManager gameManager;

    private final transient ServerGetMessage serverGetMessage;

    private final transient GameNetworkManager gameNetworkManager;

    /**
     * Creates a new game controller with a game manager.
     *
     * @param gameManager game manager for the server side of the application
     * @throws RemoteException exception due to the fact that this is an unicast remote object
     */
    public GameController(GameManager gameManager) throws RemoteException {
        super();
        this.gameManager = gameManager;
        this.gameNetworkManager = gameManager.getGameNetworkManager();
        this.serverGetMessage = new ServerGetMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void joinGame(final String message, IGameView view, IGameObserver gameObserver,
                         IRoundTrackObserver roundTrackObserver, IStateObserver stateObserver,
                         IDraftPoolObserver draftPoolObserver, IDrawableCollectionObserver diceBagObserver, ITimeOutObserver timeOutObserver) throws IOException {
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);

        if (gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            view.err(ServerMessage.THE_GAME_DOSNT_EXIST);
            throw new IOException();
        }

        synchronized (gameManager.getGameByName(gameName)) {
            IGame game = gameManager.getGameByName(gameName);

            GameObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
            game.attachGameObserver(token, new GameFakeObserver(token, gameObserver, observerManager));
            game.attachRoundTrackObserver(token, new RoundTrackFakeObserver(token, roundTrackObserver, observerManager));
            game.attachStateObserver(token, new StateFakeObserver(token, observerManager, stateObserver));
            game.attachDraftPoolObserver(token, new DraftPoolFakeObserver(token, draftPoolObserver, observerManager));
            game.attachDiceBagObserver(token, new DrawableCollectionFakeObserver<>(token, diceBagObserver, observerManager));
            observerManager.attachTimeoutObserver(token, timeOutObserver);

            try {
                game.userJoin(token);
            } catch (InvalidActionException e) {

                game.detachObservers(token);

                try {
                    view.err("You are not playing in this game or it is impossible to join. Try with a reconnect");
                } catch (IOException ignored) {
                    // Ignored because nothing can be done
                }
                return;
            }

            gameNetworkManager.putView(token, view);
        }

        try {
            view.ack("You are now ready to play");
        } catch (IOException exception) {
            handleIOException(token, gameName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseSchemaCard(String message) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);
        SchemaCard schemaCard = serverGetMessage.getSchemaCard(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
            }

            try {
                game.userSelectSchemaCard(token, schemaCard);
            } catch (InvalidActionException e) {
                if (gameNetworkManager.containsToken(token)) {
                    try {
                        gameNetworkManager.getViewByToken(token).err("The schema card selected is not valid");
                    } catch (IOException e1) {
                        handleIOException(token, gameName);
                        return;
                    }
                }
                throw new IOException();
            }
        }

        try {
            gameNetworkManager.getViewByToken(token).ack("You have correctly selected the schema card: " + schemaCard.getName());
        } catch (IOException ioe) {
            handleIOException(token, gameName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindPlayer(String message, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);
        String userName = serverGetMessage.getUserName(message);
        Player player = null;

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            for (Player p : game.getPlayers())
                if (p.getUser().getName().equals(userName))
                    player = p;

            if (player == null)
                throw new IOException();

            game.attachSchemaCardObserver(token, player.getSchemaCard(), new SchemaCardFakeObserver(token,
                    gameManager.getObserverManagerByGame(gameName), schemaCardObserver));
            game.attachPlayerObserver(token, player, new PlayerFakeObserver(token, gameManager.getObserverManagerByGame(gameName), playerObserver));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindToolCard(String message, IToolCardObserver toolCardObserver) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);
        ToolCard toolCard = serverGetMessage.getToolCard(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                gameNetworkManager.removeView(token);
                return;
            }

            if (!game.getToolCards().contains(toolCard)) {
                try {
                    gameNetworkManager.getViewByToken(token).err("You are trying to listening on a non existing toolcard");
                } catch (IOException e) {
                    handleIOException(token, gameName);
                }
                return;
            }

            game.attachToolCardObserver(token, toolCard, new ToolCardFakeObserver(token, gameManager.getObserverManagerByGame(gameName), toolCardObserver));
        }

    }

    /**
     * {@inheritDoc}x
     */
    @Override
    public void chooseAction(String message) throws IOException {
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);
        IActionCommand actionCommand = serverGetMessage.getActionCommand(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            try {
                game.userChooseAction(token, actionCommand);
            } catch (InvalidActionException e) {
                try {
                    gameNetworkManager.getViewByToken(token).err(INVALID_ACTION_ERR + " CHOOSE ACTION");
                } catch (IOException e1) {
                    handleIOException(token, gameName);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeDice(String message) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);
        Dice dice = serverGetMessage.getDice(message);
        Position position = serverGetMessage.getPosition(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            try {
                game.userPlaceDice(token, dice, position);
            } catch (InvalidActionException e) {
                try {
                    if (e.getException() instanceof RuleViolationException)
                        handleRuleViolationException(gameNetworkManager.getViewByToken(token), (RuleViolationException) e.getException());
                    else
                        gameNetworkManager.getViewByToken(token).err(INVALID_ACTION_ERR + " dice placing");
                } catch (IOException ioe) {
                    handleIOException(token, gameName);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useToolCard(String message, IToolCardExecutorObserver executorObserver) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);
        ToolCard toolCard = serverGetMessage.getToolCard(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            try {
                game.userUseToolCard(token, toolCard, new ToolCardExecutorFakeObserver(token, gameManager.getObserverManagerByGame(gameName), executorObserver));
            } catch (InvalidActionException e) {
                try {
                    gameNetworkManager.getViewByToken(token).err("You cannot take any action right now");
                } catch (IOException e1) {
                    handleIOException(token, gameName);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void choosePrivateObjectiveCard(String message) throws IOException {
        final String token = serverGetMessage.getToken(message);
        final String gameName = serverGetMessage.getGameName(message);
        final PrivateObjectiveCard privateObjectiveCard = serverGetMessage.getPrivateObjectiveCard(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            try {
                game.userChoosePrivateObjectiveCard(token, privateObjectiveCard);
            } catch (InvalidActionException e) {
                try {
                    gameNetworkManager.getViewByToken(token).err("The private objective card chosen is invalid");
                } catch (IOException e1) {
                    handleIOException(token, gameName);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setDice(String message) throws IOException {
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);
        Dice dice = serverGetMessage.getDice(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            try {
                game.userFireExecutorEvent(token, new DiceExecutorEvent(dice));
            } catch (InvalidActionException e) {
                try {
                    gameNetworkManager.getViewByToken(token).err("Can't fire an event");
                } catch (IOException e1) {
                    handleIOException(token, gameName);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNewValue(String message) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);
        Integer value = serverGetMessage.getInteger(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            try {
                game.userFireExecutorEvent(token, new ValueExecutorEvent(value));
            } catch (InvalidActionException e) {
                try {
                    gameNetworkManager.getViewByToken(token).err(FIRE_EVENT_ERROR);
                } catch (IOException e1) {
                    handleIOException(token, gameName);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(String message) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);
        Color color = serverGetMessage.getColor(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            try {
                game.userFireExecutorEvent(token, new ColorExecutorEvent(color));
            } catch (InvalidActionException e) {
                try {
                    gameNetworkManager.getViewByToken(token).err(FIRE_EVENT_ERROR);
                } catch (IOException e1) {
                    handleIOException(token, gameName);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(String message) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);
        Position position = serverGetMessage.getPosition(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            try {
                game.userFireExecutorEvent(token, new PositionExecutorEvent(position));
            } catch (InvalidActionException e) {
                try {
                    gameNetworkManager.getViewByToken(token).err(FIRE_EVENT_ERROR);
                } catch (IOException e1) {
                    handleIOException(token, gameName);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContinueAction(String message) throws IOException {
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);
        boolean answer = serverGetMessage.getBoolean(message);

        if (initialCheck(token))
            throw new IOException();

        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameNetworkManager.getViewByToken(token).err(GAME_TERMINATED);
                gameNetworkManager.removeView(token);
                return;
            }

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                gameNetworkManager.getViewByToken(token).err(YOU_NEED_TO_RECONNECT);
                return;
            }

            try {
                game.userFireExecutorEvent(token, new AnswerExecutorEvent(answer));
            } catch (InvalidActionException e) {
                try {
                    gameNetworkManager.getViewByToken(token).err(FIRE_EVENT_ERROR);
                } catch (IOException e1) {
                    handleIOException(token, gameName);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reconnect(String message, IGameView gameView, IStateObserver stateObserver, Map<String, IPlayerObserver> playerObserver,
                          Map<String, IToolCardObserver> toolCardObserver, Map<String, ISchemaCardObserver> schemaCardObserver, IGameObserver gameObserver,
                          IDraftPoolObserver draftPoolObserver, IRoundTrackObserver roundTrackObserver, IDrawableCollectionObserver
                                  diceBagObserver, ITimeOutObserver timeOutObserver) throws IOException {
        final String userName = serverGetMessage.getUserName(message);
        String token = null;
        String gameName = null;

        List<IGame> games = gameManager.getGameList();

        for (IGame game : games) {
            for (Player player : game.getPlayers())
                if (player.getUser().getName().equals(userName)) {
                    token = player.getUser().getToken();
                    gameName = game.getName();
                }
        }

        if (token == null || gameName == null) {
            gameView.err("No user with such token exists");
            throw new IOException();
        }

        // check if the token is the one of a disconnected player
        synchronized (gameManager.getGameByName(gameName)) {
            if (gameNetworkManager.clearObservers(gameName)) {
                gameView.err(GAME_TERMINATED);
                throw new IOException();
            }

            GameObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
            IGame game = gameManager.getGameByName(gameName);

            if (!observerManager.getDisconnectedPlayer().contains(token)) {
                gameView.err("A player with this name is already connected to the game");
                throw new IOException();
            }

            // check if given data are corrects
            if (!isObserverBindCorrect(game, playerObserver, schemaCardObserver, toolCardObserver)) {
                gameView.err("Observers are wrong");
                throw new IOException();
            }

            // Attaching observer and view regarding the re-connected player
            gameNetworkManager.putView(token, gameView);

            //
            HashMap<String, ISchemaCardObserver> schemaCardObserverHashMapToken = new HashMap<>();
            HashMap<String, IPlayerObserver> playerObserverHashMapToken = new HashMap<>();

            schemaCardObserver.forEach((key, value) -> schemaCardObserverHashMapToken.putIfAbsent(NetworkUtility.encryptUsername(key),schemaCardObserver.get(key)));
            playerObserver.forEach((key, value) -> playerObserverHashMapToken.putIfAbsent(NetworkUtility.encryptUsername(key), playerObserver.get(key)));

            // Attaching observers
            final String finalToken = token;
            game.getPlayers().forEach(player -> game.attachPlayerObserver(finalToken, player, new PlayerFakeObserver(finalToken, observerManager, playerObserverHashMapToken.get(player.getToken()))));
            game.getPlayers().forEach(player -> game.attachSchemaCardObserver(finalToken, player.getSchemaCard(),
                    new SchemaCardFakeObserver(finalToken, observerManager, schemaCardObserverHashMapToken.get(player.getToken()))));
            game.getToolCards().forEach(toolCard -> game.attachToolCardObserver(finalToken, toolCard, new ToolCardFakeObserver(finalToken, observerManager, toolCardObserver.get(toolCard.getName()))));
            game.attachDiceBagObserver(token, new DrawableCollectionFakeObserver<>(token, diceBagObserver, observerManager));
            game.attachDraftPoolObserver(token, new DraftPoolFakeObserver(token, draftPoolObserver, observerManager));
            game.attachGameObserver(token, new GameFakeObserver(token, gameObserver, observerManager));
            game.attachRoundTrackObserver(token, new RoundTrackFakeObserver(token, roundTrackObserver, observerManager));
            game.attachStateObserver(token, new StateFakeObserver(token, observerManager, stateObserver));
            observerManager.attachTimeoutObserver(token, timeOutObserver);

            // Notify reconnection
            notifyReconnection(gameName, token, userName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String attemptReconnect(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        final String username = serverGetMessage.getUserName(message);
        final String token = NetworkUtility.encryptUsername(username);
        final Optional<String> gameName;
        List<IGame> gameList = gameManager.getGameList();

        gameName = gameList.stream().filter(game -> gameManager.getPlayersByGame(game.getName()).contains(token)).map(IGame::getName).findFirst();

        if (!gameName.isPresent() || !gameManager.getObserverManagerByGame(gameName.get()).getDisconnectedPlayer().contains(token))
            return serverCreateMessage.reconnectErrorMessage();

        List<User> users = gameManager.getGameByName(gameName.get()).getUsers();

        return serverCreateMessage.createGameNameMessage(gameName.get()).createUserList(users).createTokenMessage(token).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolCards(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }

        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        List<ToolCard> toolCards;

        synchronized (gameManager.getGameByName(gameName)) {
            toolCards = gameManager.getGameByName(gameName).getToolCards();
        }

        return serverCreateMessage.createToolCardList(toolCards).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlayersCoins(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }

        if (gameManager.getGameByName(gameName).isSinglePlayer()) {
            return serverCreateMessage.getErrorMessage();
        }

        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        HashMap<User, Integer> coinsMap = new HashMap<>();

        synchronized (gameManager.getGameByName(gameName)) {
            gameManager.getGameByName(gameName).getPlayers().forEach(player -> coinsMap.putIfAbsent(player.getUser(),
                    player.getCoins()));
        }

        return serverCreateMessage.createPlayersCoinsMessage(coinsMap).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMyCoins(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }

        if (gameManager.getGameByName(gameName).isSinglePlayer())
            return serverCreateMessage.getErrorMessage();

        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        Optional<Player> requestingPlayer;

        synchronized (gameManager.getGameByName(gameName)) {
            requestingPlayer = gameManager.getGameByName(gameName).getPlayers().stream().filter(player -> player.getToken().equals(token)).findFirst();
        }

        if (!requestingPlayer.isPresent())
            return serverCreateMessage.getErrorMessage();

        return serverCreateMessage.createCoinsMessage(requestingPlayer.get().getCoins()).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPublicObjectiveCards(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }
        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        List<PublicObjectiveCard> publicObjectiveCardList;

        synchronized (gameManager.getGameByName(gameName)) {
            publicObjectiveCardList = gameManager.getGameByName(gameName).getPublicObjectiveCards();
        }

        return serverCreateMessage.createPublicObjectiveCardList(publicObjectiveCardList).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrivateObjectiveCardByToken(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }
        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        List<PrivateObjectiveCard> privateObjectiveCardList;

        synchronized (gameManager.getGameByName(gameName)) {
            privateObjectiveCardList = gameManager.getGameByName(gameName).getPrivateObjectiveCardsByToken(token);
        }

        return serverCreateMessage.createPrivateObjectiveCardList(privateObjectiveCardList).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSchemaCards(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }
        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        Map<User, SchemaCard> stringSchemaCardMap = new HashMap<>();

        synchronized (gameManager.getGameByName(gameName)) {
            List<Player> players = gameManager.getGameByName(gameName).getPlayers();
            players.forEach(player -> stringSchemaCardMap.put(player.getUser(), player.getSchemaCard()));
        }

        return serverCreateMessage.createSchemaCardMapMessage(stringSchemaCardMap).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDraftPool(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }
        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        DraftPool draftPool;

        synchronized (gameManager.getGameByName(gameName)) {
            draftPool = gameManager.getGameByName(gameName).getDraftPool();
        }

        return serverCreateMessage.createDraftPoolMessage(draftPool).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRoundTrack(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }

        if (gameNetworkManager.clearObservers(gameName))
            return serverCreateMessage.getGameTerminatedErrorMessage();

        RoundTrack roundTrack;

        synchronized (gameManager.getGameByName(gameName)) {
            roundTrack = gameManager.getGameByName(gameName).getRoundTrack();
        }

        return serverCreateMessage.createRoundTrackMessage(roundTrack).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolCardByName(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);
        String toolCardName = serverGetMessage.getToolCard(message).getName();

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }
        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        List<ToolCard> toolCards;

        synchronized (gameManager.getGameByName(gameName)) {
            IGame game = gameManager.getGameByName(gameName);
            toolCards = game.getToolCards();
        }

        Optional<ToolCard> toolCard = toolCards.stream().filter(card -> card.getName().equals(toolCardName)).findFirst();
        if (!toolCard.isPresent()) {
            return serverCreateMessage.getErrorMessage();
        }

        return serverCreateMessage.createToolCardMessage(toolCard.get()).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentPlayer(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }
        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        Player player;

        synchronized (gameManager.getGameByName(gameName)) {
            try {
                player = gameManager.getGameByName(gameName).getCurrentPlayer();
            } catch (InvalidActionException e) {
                return serverCreateMessage.getErrorMessage();
            }
        }

        return serverCreateMessage.createUserMessage(player.getUser()).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSchemaCardByToken(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        final String token = serverGetMessage.getToken(message);
        final String gameName = serverGetMessage.getGameName(message);
        SchemaCard schemaCard = null;

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }
        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        synchronized (gameManager.getGameByName(gameName)) {
            IGame game = gameManager.getGameByName(gameName);
            for (Player p : game.getPlayers()) {
                if (p.getToken().equals(token)) {
                    schemaCard = p.getSchemaCard();
                }
            }
        }

        if (schemaCard == null)
            return serverCreateMessage.getErrorMessage();

        return serverCreateMessage.createSchemaCardMessage(schemaCard).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getListOfUser(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        final String token = serverGetMessage.getToken(message);
        final String gameName = serverGetMessage.getGameName(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }
        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        List<User> users;

        synchronized (gameManager.getGameByName(gameName)) {
            users = gameManager.getGameByName(gameName).getUsers();
        }

        return serverCreateMessage.createUserList(users).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTimeout(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        final String token = serverGetMessage.getToken(message);
        final String gameName = serverGetMessage.getGameName(message);

        if ((!gameNetworkManager.containsToken(token) && !gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token)) ||
                gameManager.notContainsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverCreateMessage.getErrorMessage();
        }
        if (gameNetworkManager.clearObservers(gameName)) {
            return serverCreateMessage.getGameTerminatedErrorMessage();
        }

        long timeToTimeout;

        synchronized (gameManager.getGameByName(gameName)) {
            timeToTimeout = gameManager.getObserverManagerByGame(gameName).getTimeToTimeout();
        }

        return serverCreateMessage.createTimeoutMessage(formatTime(timeToTimeout)).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createSinglePlayer(String message) throws IOException {
        ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
        String username = serverGetMessage.getUserName(message);
        int difficulty = serverGetMessage.getInteger(message);

        String gameName = gameManager.createSinglePlayerGame(username, difficulty);

        return serverCreateMessage.createGameNameMessage(gameName).createTokenMessage(NetworkUtility.encryptUsername(username)).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quitGame(String message) throws IOException {
        final String token = serverGetMessage.getToken(message);
        final String gameName = serverGetMessage.getGameName(message);

        synchronized (gameManager.getGameByName(gameName)) {
            gameManager.getObserverManagerByGame(gameName).signalDisconnection(token);
            if (gameNetworkManager.clearObservers(gameName))
                return;

            try {
                if (gameManager.getGameByName(gameName).getCurrentPlayer().getToken().equals(token))
                    gameManager.getGameByName(gameName).userChooseAction(token, new EndTurnAction());
            } catch (InvalidActionException e) {
                // DO NOTHING
            }
        }
    }

    /**
     * Handles network error in server controller
     *
     * @param token    player's token that generated the request that creates the exception handled
     * @param gameName game in which the player acts
     * @throws IOException if the game is terminated
     */
    private void handleIOException(String token, String gameName) throws IOException {
        synchronized (gameManager.getGameByName(gameName)) {
            gameManager.getObserverManagerByGame(gameName).signalDisconnection(token);
            gameNetworkManager.removeToken(token);
        }
    }

    /**
     * Sends an error depending on the rule violation found while executing.
     *
     * @param view      view that needs to be signaled of the error
     * @param exception rule violation exception
     * @throws IOException network communication error
     */
    private void handleRuleViolationException(IGameView view, RuleViolationException exception) throws IOException {
        switch (exception.getViolationType()) {
            case NO_DICE_NEAR:
                view.err("You cannot place dice because there are no dice near");
                break;
            case SIMILAR_DICE_NEAR:
                view.err("You cannot place dice because there is a similar dice near");
                break;
            case TILE_FILLED:
                view.err("You cannot place dice because there is already a dice on it");
                break;
            case TILE_UNMATCHED:
                view.err("You cannot place dice on this tile");
                break;
            case HAS_DICE_NEAR:
                view.err("You cannot place dice because there is at least one dice near");
                break;
            case NO_VIOLATION:
                view.err("You cannot take any action right now");
                break;
            case NOT_BORDER_TILE:
                view.err("You have to place a dice around the border of the schema card");
                break;
            default:
                break;
        }
    }

    /**
     * Returns true if a certain player has the synchronized model
     *
     * @param token    player's token
     * @param gameName game's name
     * @return true if synchronized, false otherwise
     */
    private boolean wasUserDisconnected(String token, String gameName) {
        return gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token);
    }

    /**
     * Checks that the view map contains the player's token, that the specified game exists and that
     * the user is part of the that game.
     *
     * @param token    player's token
     * @return true if the condition mentioned above are not respected, false otherwise
     */
    private boolean initialCheck(String token) {
        return !gameNetworkManager.containsToken(token);
    }

    /**
     * Format a certain time given as long in mm:ss
     *
     * @param time time to format
     * @return string in format mm:ss representing time
     */
    private String formatTime(long time) {
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

    /**
     * Checks if the observer given for reconnecting are correct for the specific game or not.
     * They are wrong if they are not matching the size of player list for the game, or if they are matched
     * to non-existing player of that game
     *
     * @param game               game on which the check occurs
     * @param playerObserver     map of player observer (username, observer)
     * @param schemaCardObserver map of schema card observer (username, observer)
     * @param toolCardObserver   map of tool card observer (tool card name, observer)
     * @return true if they are correct, false otherwise
     */
    private boolean isObserverBindCorrect(IGame game, Map<String, IPlayerObserver> playerObserver, Map<String, ISchemaCardObserver> schemaCardObserver, Map<String, IToolCardObserver> toolCardObserver) {

        List<String> playerKeys = playerObserver.keySet().stream().map(NetworkUtility::encryptUsername).collect(Collectors.toList());
        List<String> schemaCardsKey = schemaCardObserver.keySet().stream().map(NetworkUtility::encryptUsername).collect(Collectors.toList());

        if (!(playerKeys.containsAll(gameManager.getPlayersByGame(game.getName())) &&
                gameManager.getPlayersByGame(game.getName()).containsAll(playerKeys))) {
            return false;
        }
        if (!((schemaCardsKey.containsAll(gameManager.getPlayersByGame(game.getName()))) && (gameManager.getPlayersByGame(game.getName()).containsAll(schemaCardsKey)))) {
            return false;
        }

        List<String> toolCardsNameOnServer = game.getToolCards().stream().map(Card::getName).collect(Collectors.toList());

        return toolCardsNameOnServer.containsAll(toolCardObserver.keySet()) && toolCardObserver.keySet().containsAll(toolCardsNameOnServer);
    }

    /**
     * Notify the reconnection of a certain player
     *
     * @param gameName             name of the game that the player has reconnected to
     * @param reconnectingToken    reconnecting player's token
     * @param reconnectingUserName reconnecting player's username
     * @throws IOException if the game has terminated
     */
    private void notifyReconnection(String gameName, String reconnectingToken, String reconnectingUserName) throws IOException {
        gameManager.getObserverManagerByGame(gameName).signalReconnect(reconnectingToken);
        try {
            gameNetworkManager.getViewByToken(reconnectingToken).ack("Reconnected successful.");
            for (String playerToken : gameManager.getPlayersByGame(gameName)) {
                if (!gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(playerToken)) {
                    try {
                        gameNetworkManager.getViewByToken(playerToken).ack("Player " + reconnectingUserName + " has reconnected.");
                    } catch (IOException e) {
                        handleIOException(playerToken, gameName);
                    }
                }
            }
        } catch (IOException e) {
            handleIOException(reconnectingToken, gameName);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameController)) return false;
        if (!super.equals(o)) return false;
        GameController that = (GameController) o;
        return Objects.equals(gameManager, that.gameManager) &&
                Objects.equals(serverGetMessage, that.serverGetMessage) &&
                Objects.equals(gameNetworkManager, that.gameNetworkManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameManager, serverGetMessage, gameNetworkManager);
    }
}
