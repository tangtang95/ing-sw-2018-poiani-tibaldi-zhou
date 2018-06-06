package org.poianitibaldizhou.sagrada.game.controller;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.*;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.*;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ServerGetMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameController extends UnicastRemoteObject implements IGameController {

    private static final transient String INITIAL_CHECK_ERROR = "You're not playing the selected game or the game does not exist";
    private static final transient String FIRE_EVENT_ERROR = "Can't fire an event now";
    private static final transient String INVALID_ACTION_ERR = "You can't perform this action now";
    private static final transient String NOT_SYNCH = "You're not synchronized with the model and need to reconnect";

    private final transient HashMap<String, IGameView> viewMap = new HashMap<>();
    private final transient GameManager gameManager;

    private final transient ServerGetMessage serverGetMessage;
    private final transient ServerCreateMessage serverCreateMessage;

    public GameController(GameManager gameManager) throws RemoteException {
        super();
        this.gameManager = gameManager;
        this.serverGetMessage = new ServerGetMessage();
        this.serverCreateMessage = new ServerCreateMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void joinGame(final String message, IGameView view, IGameObserver gameObserver,
                         IRoundTrackObserver roundTrackObserver, IStateObserver stateObserver,
                         IDraftPoolObserver draftPoolObserver, IDrawableCollectionObserver diceBagObserver) throws IOException {
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);

        if (!gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            try {
                view.err("The game doesn't exist or you are not signaled as an entering player");
            } catch (IOException ignored) {
                // Ignored because nothing can be done
            }
            return;
        }

        synchronized (gameManager.getGameByName(gameName)) {
            IGame game = gameManager.getGameByName(gameName);


            GameObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
            game.attachGameObserver(token, new GameFakeObserver(token, gameObserver, observerManager));
            game.attachRoundTrackObserver(token, new RoundTrackFakeObserver(token, roundTrackObserver, observerManager));
            game.attachStateObserver(token, new StateFakeObserver(token, observerManager, stateObserver));
            game.attachDraftPoolObserver(token, new DraftPoolFakeObserver(token, draftPoolObserver, observerManager));
            game.attachDiceBagObserver(token, new DrawableCollectionFakeObserver<>(token, diceBagObserver, observerManager));

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

            viewMap.put(token, view);
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
    public void chosenSchemaCard(String message) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);
        SchemaCard schemaCard = serverGetMessage.getSchemaCard(message);

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName))
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored because nothing can be done
                }

            try {
                game.userSelectSchemaCard(token, schemaCard);
            } catch (InvalidActionException e) {
                if (viewMap.containsKey(token)) {
                    try {
                        viewMap.get(token).err("The schema card selected is not valid");
                    } catch (IOException e1) {
                        handleIOException(token, gameName);
                        return;
                    }
                }
                return;
            }
        }

        try {
            viewMap.get(token).ack("You have correctly selected the schema card: " + schemaCard.getName());
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored exception because nothing can be done
                }
            }

            for (Player p : game.getPlayers())
                if (p.getUser().getName().equals(userName))
                    player = p;

            if (player == null)
                throw new IOException();

            if (!game.getPlayers().contains(player)) {
                try {
                    viewMap.get(token).err("You are trying to listening the actions of an non existing player");
                } catch (IOException e) {
                    handleIOException(token, gameName);
                }
                return;
            }

            game.attachSchemaCardObserver(token, player.getSchemaCard(), new SchemaCardFakeObserver(token,
                    gameManager.getObserverManagerByGame(gameName), schemaCardObserver));
            game.attachPlayerObserver(token, player, new PlayerFakeObserver(token, gameManager.getObserverManagerByGame(gameName), playerObserver));
        }

        try {
            viewMap.get(token).ack("Binding to " + player.getUser().getName() + " successful");
        } catch (IOException e) {
            handleIOException(token, gameName);
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored exception because nothing can be done here
                }
            }

            if (!game.getToolCards().contains(toolCard)) {
                try {
                    viewMap.get(token).err("You are trying to listening on a non existing toolcard");
                } catch (IOException e) {
                    handleIOException(token, gameName);
                }
                return;
            }

            game.attachToolCardObserver(token, toolCard, new ToolCardFakeObserver(token, gameManager.getObserverManagerByGame(gameName), toolCardObserver));
        }

        try {
            viewMap.get(token).ack("Binding to " + toolCard.getName() + " successful");
        } catch (IOException e) {
            handleIOException(token, gameName);
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored because nothing can be done
                }
            }

            try {
                game.userChooseAction(token, actionCommand);
            } catch (InvalidActionException e) {
                try {
                    viewMap.get(token).err(INVALID_ACTION_ERR);
                } catch (IOException e1) {
                    handleIOException(token, gameName);
                }
            }
        }

        try {
            viewMap.get(token).ack("Action performed");
        } catch (IOException e) {
            handleIOException(token, gameName);
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored because nothing can be done
                }
            }

            try {
                game.userPlaceDice(token, dice, position);
            } catch (InvalidActionException e) {
                try {
                    if (e.getException() instanceof RuleViolationException)
                        handleRuleViolationException(viewMap.get(token), (RuleViolationException) e.getException());
                    else
                        viewMap.get(token).err(INVALID_ACTION_ERR);
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored because nothing can be done here
                }
            }

            try {
                game.userUseToolCard(token, toolCard, new ToolCardExecutorFakeObserver(token, gameManager.getObserverManagerByGame(gameName), executorObserver));
            } catch (InvalidActionException e) {
                try {
                    viewMap.get(token).err("You cannot take any action right now");
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);


            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored because nothing can be done
                }
            }

            try {
                game.userChoosePrivateObjectiveCard(token, privateObjectiveCard);
            } catch (InvalidActionException e) {
                try {
                    viewMap.get(token).err("The private objective card chosen is invalid");
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException e) {
                    // Ignored exception: can't do anything about this
                }
            }

            try {
                game.userFireExecutorEvent(token, new DiceExecutorEvent(dice));
            } catch (InvalidActionException e) {
                try {
                    viewMap.get(token).err("Can't fire an event");
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored exception: can't do anything about this
                }
            }

            try {
                game.userFireExecutorEvent(token, new ValueExecutorEvent(value));
            } catch (InvalidActionException e) {
                try {
                    viewMap.get(token).err(FIRE_EVENT_ERROR);
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored exception: can't do anything about this
                }
            }

            try {
                game.userFireExecutorEvent(token, new ColorExecutorEvent(color));
            } catch (InvalidActionException e) {
                try {
                    viewMap.get(token).err(FIRE_EVENT_ERROR);
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored exception: can't do anything about this
                }
            }

            try {
                game.userFireExecutorEvent(token, new PositionExecutorEvent(position));
            } catch (InvalidActionException e) {
                try {
                    viewMap.get(token).err(FIRE_EVENT_ERROR);
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

        if (initialCheck(token, gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (wasUserDisconnected(token, gameName)) {
                try {
                    viewMap.get(token).err(NOT_SYNCH);
                } catch (IOException ignored) {
                    // Ignored exception: can't do anything about this
                }
            }

            try {
                game.userFireExecutorEvent(token, new AnswerExecutorEvent(answer));
            } catch (InvalidActionException e) {
                try {
                    viewMap.get(token).err(FIRE_EVENT_ERROR);
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
                                  diceBagObserver) throws IOException {
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
            gameView.err("Not user with such token exists");
        }

        // check if the token is the one of a disconnected player
        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);
            GameObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
            IGame game = gameManager.getGameByName(gameName);
            try {
                if (!observerManager.getDisconnectedPlayer().contains(token)) {
                    gameView.err("A player with this name is already connected to the game");
                    return;
                }

                // check if given data are corrects
                if (!(game.getPlayers().containsAll(playerObserver.keySet()) && playerObserver.size() == game.getPlayers().size())) {
                    gameView.err("Player observers are wrong");
                    return;
                }
                if (!(game.getPlayers().containsAll(schemaCardObserver.keySet()) && game.getPlayers().size() == schemaCardObserver.size())) {
                    gameView.err("Schema card observers are wrong");
                    return;
                }
                if (!(game.getToolCards().containsAll(toolCardObserver.keySet()) && game.getToolCards().size() == toolCardObserver.size())) {
                    gameView.err("Tool card observers are wrong");
                    return;
                }
            } catch (IOException ignored) {
                // Ignored exceptions: can't do anything regarding this
            }

            // Attaching observer and view regarding the re-connected player
            if (viewMap.containsKey(token)) {
                viewMap.replace(token, gameView);
            }

            final String finalToken = token;
            game.getPlayers().forEach(player -> game.attachPlayerObserver(finalToken, player, new PlayerFakeObserver(finalToken, observerManager, playerObserver.get(player.getToken()))));
            game.getPlayers().forEach(player -> game.attachSchemaCardObserver(finalToken, player.getSchemaCard(),
                    new SchemaCardFakeObserver(finalToken, observerManager, schemaCardObserver.get(player.getToken()))));
            game.getToolCards().forEach(toolCard -> game.attachToolCardObserver(finalToken, toolCard, new ToolCardFakeObserver(finalToken, observerManager, toolCardObserver.get(toolCard.getName()))));
            game.attachDiceBagObserver(token, new DrawableCollectionFakeObserver<>(token, diceBagObserver, observerManager));
            game.attachDraftPoolObserver(token, new DraftPoolFakeObserver(token, draftPoolObserver, observerManager));
            game.attachGameObserver(token, new GameFakeObserver(token, gameObserver, observerManager));
            game.attachRoundTrackObserver(token, new RoundTrackFakeObserver(token, roundTrackObserver, observerManager));
            game.attachStateObserver(token, new StateFakeObserver(token, observerManager, stateObserver));

            observerManager.signalReconnect(token);
            try {
                gameView.ack(FIRE_EVENT_ERROR);
            } catch (IOException e) {
                handleIOException(token, gameName);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String attemptReconnect(String message) throws IOException {
        final String username = serverGetMessage.getUserName(message);
        final String token = String.valueOf(username.hashCode());
        final Optional<String> gameName;
        List<IGame> gameList = gameManager.getGameList();

        gameName = gameList.stream().filter(game -> gameManager.getPlayersByGame(game.getName()).contains(token)).map(IGame::getName).findFirst();

        if (!gameName.isPresent() || !gameManager.getObserverManagerByGame(gameName.get()).getDisconnectedPlayer().contains(token))
            return serverGetMessage.reconnectErrorMessage();

        ArrayList<User> users = new ArrayList<>();

        gameManager.getGameByName(gameName.get()).getPlayers().forEach(player -> users.add(player.getUser()));

        return serverCreateMessage.createGamenNameMessage(gameName.get()).createUserList(users).createTokenMessage(token).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolCards(String message) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
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
    public String getPublicObjectiveCards(String message) throws IOException {
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
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
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
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
        String token = serverGetMessage.getToken(message);
        String gameName = serverGetMessage.getGameName(message);

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
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
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
        }


        DraftPool draftPool;

        synchronized (gameManager.getGameByName(gameName)) {
            draftPool = gameManager.getGameByName(gameName).getDraftPool();
        }

        return serverCreateMessage.createDiceList(draftPool.getDices()).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRoundTrack(String message) throws IOException {
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
        }

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
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);
        String toolCardName = serverGetMessage.getToolCard(message).getName();

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
        }

        List<ToolCard> toolCards;

        synchronized (gameManager.getGameByName(gameName)) {
            IGame game = gameManager.getGameByName(gameName);
            toolCards = game.getToolCards();
        }

        Optional<ToolCard> toolCard = toolCards.stream().filter(card -> card.getName().equals(toolCardName)).findFirst();
        if (!toolCard.isPresent()) {
            return serverGetMessage.getErrorMessage();
        }

        return serverCreateMessage.createToolCardMessage(toolCard.get()).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentPlayer(String message) throws IOException {
        String gameName = serverGetMessage.getGameName(message);
        String token = serverGetMessage.getToken(message);

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
        }

        Player player;

        synchronized (gameManager.getGameByName(gameName)) {
            try {
                player = gameManager.getGameByName(gameName).getCurrentPlayer();
            } catch (InvalidActionException e) {
                return serverGetMessage.getErrorMessage();
            }
        }

        return serverCreateMessage.createUserMessage(player.getUser()).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSchemaCardByToken(String message) throws IOException {
        final String token = serverGetMessage.getToken(message);
        final String gameName = serverGetMessage.getGameName(message);
        SchemaCard schemaCard = null;

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
        }

        synchronized (gameManager.getGameByName(gameName)) {
            IGame game = gameManager.getGameByName(gameName);
            for (Player p : game.getPlayers()) {
                if (p.getToken().equals(token))
                    schemaCard = p.getSchemaCard();
            }
        }

        if (schemaCard == null)
            return serverGetMessage.getErrorMessage();

        return serverCreateMessage.createSchemaCardMessage(schemaCard).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getListOfUser(String message) throws IOException {
        final String token = serverGetMessage.getToken(message);
        final String gameName = serverGetMessage.getGameName(message);

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            return serverGetMessage.getErrorMessage();
        }

        List<Player> players;
        List<User> users = new ArrayList<>();

        synchronized (gameManager.getGameByName(gameName)) {
            players = gameManager.getGameByName(gameName).getPlayers();
        }

        players.forEach(player -> users.add(player.getUser()));

        return serverCreateMessage.createUserList(users).buildMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createSinglePlayer(String message) throws IOException {
        String username = serverGetMessage.getUserName(message);
        int difficulty = serverGetMessage.getInteger(message);

        String gameName = gameManager.createSinglePlayerGame(username, difficulty);

        return serverCreateMessage.createGamenNameMessage(gameName).createTokenMessage(String.valueOf(username.hashCode())).buildMessage();
    }

    /**
     * Handles network error in server controller
     *
     * @param token    player's token that generated the request that creates the exception handled
     * @param gameName game in which the player acts
     */
    private void handleIOException(String token, String gameName) {
        synchronized (gameManager.getGameByName(gameName)) {
            gameManager.getObserverManagerByGame(gameName).signalDisconnection(token);
            viewMap.remove(token);
        }
    }

    /**
     * It cleans the observer of a certain game, with the notify disconnections.
     * It also signals the disconnections of the various player.
     * It also handles the game termination when there aren't enough user to continue the game
     *
     * @param gameName game's name
     */
    private void cleanObservers(String gameName) {
        synchronized (gameManager.getGameByName(gameName)) {
            GameObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
            Set<String> toNotifyDisconnect = observerManager.getDisconnectedPlayerNotNotified();
            Set<String> disconnected = observerManager.getDisconnectedPlayer();
            List<Player> playerList = gameManager.getGameByName(gameName).getPlayers();

            toNotifyDisconnect.forEach(disconnectedToken -> {
                playerList.forEach(player -> {
                    if (!disconnected.contains(player.getToken())) {
                        try {
                            Optional<User> username = playerList.stream().map(Player::getUser).filter(user -> user.getToken().
                                    equals(disconnectedToken)).findFirst();
                            if (username.isPresent())
                                viewMap.get(player.getToken()).err(username.get().getName() + " disconnected");
                        } catch (IOException e) {
                            observerManager.signalDisconnection(player.getToken());
                        }
                    }
                });

                gameManager.getGameByName(gameName).detachObservers(disconnectedToken);
                observerManager.notifyDisconnection(disconnectedToken);
                viewMap.remove(disconnectedToken);
            });

            handleEndGame(gameManager.getGameByName(gameName), observerManager);
        }
    }

    /**
     * Force the termination of the game.
     * When the game is single player, if the only player present disconnects the game terminates.
     * When the game is multi player, and there is only one player connected, it handles its victory.
     *
     * @param game            handle the termination of this game
     * @param observerManager game observer manager of game
     */
    private void handleEndGame(IGame game, GameObserverManager observerManager) {
        if (!game.isSinglePlayer()) {
            if (observerManager.getDisconnectedPlayer().size() == game.getUsers().size() - 1) {
                // search for the player that it's not disconnected
                for (Player player : game.getPlayers())
                    if (!observerManager.getDisconnectedPlayer().contains(player.getToken())) {
                        game.forceGameTermination(player);
                    }
                gameManager.terminateGame(game.getName());
            } else if (observerManager.getDisconnectedPlayer().size() == game.getUsers().size()) {
                gameManager.terminateGame(game.getName());
            }
        } else {
            gameManager.terminateGame(game.getName());
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
     * @param gameName game's name
     * @return true if the condition mentioned above are respected, false otherwise
     */
    private boolean initialCheck(String token, String gameName) {
        if (!viewMap.containsKey(token))
            return true;
        if (!gameManager.getPlayersByGame(gameName).contains(token) || !gameManager.containsGame(gameName)) {
            try {
                viewMap.get(token).err(INITIAL_CHECK_ERROR);
            } catch (IOException ignored) {
                return true;
            }
        }
        return false;
    }
}
