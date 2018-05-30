package org.poianitibaldizhou.sagrada.game.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ColorExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.DiceExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.PositionExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ValueExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.*;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameController extends UnicastRemoteObject implements IGameController {

    private final transient HashMap<String, IGameView> viewMap = new HashMap<>();
    private final transient GameManager gameManager;

    public GameController(GameManager gameManager) throws RemoteException {
        super();
        this.gameManager = gameManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void joinGame(final String json, IGameView view, IGameObserver gameObserver,
                         IRoundTrackObserver roundTrackObserver, IStateObserver stateObserver,
                         IDraftPoolObserver draftPoolObserver, IDrawableCollectionObserver diceBagObserver) throws IOException {
        // Retriving information from the json
        final String gameName;
        final String token;
        JSONParser jsonParser = new JSONParser();
        JSONObject  jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(json);
            token = (String) jsonObject.get("token");
            gameName = (String) jsonObject.get("gameName");
        } catch (ParseException | ClassCastException e) {
            view.err("Error in network communication protocol");
            return;
        }

        // Operations requested by the method
        if (!gameManager.containsGame(gameName)) {
            view.err("The game doesn't exist");
            return;
        }

        synchronized (gameManager.getGameByName(gameName)) {
            IGame game = gameManager.getGameByName(gameName);

            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userJoin(token);
            } catch (InvalidActionException e) {
                view.err("You are not playing in this game");
                return;
            }
            view.ack("You are now ready to play");

            ObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
            game.attachGameObserver(token, new GameFakeObserver(token, gameObserver, observerManager));
            game.attachRoundTrackObserver(token, new RoundTrackFakeObserver(token, roundTrackObserver, observerManager));
            game.attachStateObserver(token, new StateFakeObserver(token, observerManager, stateObserver));
            game.attachDraftPoolObserver(token, new DraftPoolFakeObserver(token, draftPoolObserver, observerManager));
            game.attachDiceBagObserver(token, new DrawableCollectionFakeObserver<>(token, diceBagObserver, observerManager));
        }

        viewMap.put(token, view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void choosenSchemaCard(String json) throws IOException {
        final String token;
        final String gameName;
        final SchemaCard schemaCard = null;

        JSONParser jsonParser = new JSONParser();
        JSONObject  jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(json);
            token = (String) jsonObject.get("token");
            gameName = (String) jsonObject.get("gameName");
            // TODO read schema card (mattia)
        } catch (ParseException | ClassCastException e) {
            return;
        }

        if (!viewMap.containsKey(token) || !gameManager.getPlayersByGame(gameName).contains(token) || !gameManager.containsGame(gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userSelectSchemaCard(token, schemaCard);
            } catch (InvalidActionException e) {
                if (viewMap.containsKey(token)) {
                    viewMap.get(token).err("The schema card selected is not valid");
                }
                return;
            }
        }

        viewMap.get(token).ack("You have correctly selected the schema card: " + schemaCard.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindPlayer(String json, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws IOException {
        final String token = null;
        final String gameName = null;
        final Player player = null;

        // TODO MATTIA read json

        if (!viewMap.containsKey(token) || !gameManager.getPlayersByGame(gameName).contains(token) || !gameManager.containsGame(gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);


            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            if (!game.getPlayers().contains(player)) {
                viewMap.get(token).err("You are trying to listening the actions of an non existing player");
                return;
            }

            game.attachSchemaCardObserver(token, player.getSchemaCard(), new SchemaCardFakeObserver(token,
                    gameManager.getObserverManagerByGame(gameName), schemaCardObserver));
            game.attachPlayerObserver(token, player, new PlayerFakeObserver(token, gameManager.getObserverManagerByGame(gameName), playerObserver));
        }

        viewMap.get(token).ack("Binding to " + player.getUser().getName() + " successful");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindToolCard(String json, IToolCardObserver toolCardObserver) throws IOException {
        final String token = null;
        final String gameName = null;
        final ToolCard toolCard = null;

        // TODO MATTIA (read json)

        if (!viewMap.containsKey(token) || !gameManager.getPlayersByGame(gameName).contains(token) || !gameManager.containsGame(gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            if (!game.getToolCards().contains(toolCard)) {
                viewMap.get(token).err("You are trying to listening on a non existing toolcard");
                return;
            }

            game.attachToolCardObserver(token, toolCard, new ToolCardFakeObserver(token, gameManager.getObserverManagerByGame(gameName), toolCardObserver));
        }

        viewMap.get(token).ack("Binding to " + toolCard.getName() + " successful");
    }

    /**
     * {@inheritDoc}x
     */
    @Override
    public void chooseAction(String json) throws IOException {
        final String gameName = null;
        final String token = null;
        final IActionCommand actionCommand = null;

        // TODO MATTIA read json

        if (!viewMap.containsKey(token) || !gameManager.getPlayersByGame(gameName).contains(token) || !gameManager.containsGame(gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userChooseAction(token, actionCommand);
            } catch (InvalidActionException e) {
                viewMap.get(token).err("You cannot take any action right now");
            }
        }

        viewMap.get(token).ack("Action performed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeDice(String json) throws IOException {
        final String token = null;
        final String gameName = null;
        final Dice dice = null;
        final Position position = null;

        // TODO MATTIA read json

        if (!viewMap.containsKey(token) || !gameManager.getPlayersByGame(gameName).contains(token) || !gameManager.containsGame(gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userPlaceDice(token, dice, position);
            } catch (InvalidActionException e) {
                if (e.getException() instanceof RuleViolationException)
                    handleRuleViolationException(viewMap.get(token), (RuleViolationException) e.getException());
                else
                    viewMap.get(token).err("You cannot take any action right now");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useToolCard(String json, IToolCardExecutorObserver executorObserver)
            throws IOException {
        final String token = null;
        final String gameName = null;
        final ToolCard toolCard = null;

        // TODO Mattia read json

        if (!viewMap.containsKey(token) || !gameManager.getPlayersByGame(gameName).contains(token) || !gameManager.containsGame(gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);


            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userUseToolCard(token, toolCard, new ToolCardExecutorFakeObserver(token, gameManager.getObserverManagerByGame(gameName), executorObserver));
            } catch (InvalidActionException e) {
                viewMap.get(token).err("You cannot take any action right now");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void choosePrivateObjectiveCard(String json) throws IOException {
        final String token = null;
        final String gameName = null;
        final PrivateObjectiveCard privateObjectiveCard = null;

        // TODO MATTIA read json

        if (!viewMap.containsKey(token) || !gameManager.getPlayersByGame(gameName).contains(token) || !gameManager.containsGame(gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);


            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userChoosePrivateObjectiveCard(token, privateObjectiveCard);
            } catch (InvalidActionException e) {
                viewMap.get(token).err("The private objective card chosen is invalid");
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setDice(String json) throws IOException {
        final String gameName = null;
        final String token = null;
        final Dice dice = null;

        // TODO Mattia read json

        if (!viewMap.containsKey(token) || !gameManager.getPlayersByGame(gameName).contains(token) || !gameManager.containsGame(gameName))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);


            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userFireExecutorEvent(token, new DiceExecutorEvent(dice));
            } catch (InvalidActionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNewValue(String json) throws IOException {
        final String token = null;
        final String gameName = null;
        final Integer value = null;

        // TODO mattia ready json

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userFireExecutorEvent(token, new ValueExecutorEvent(value));
            } catch (InvalidActionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(String json) throws IOException {
        final String token = null;
        final String gameName = null;
        final Color color = null;


        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userFireExecutorEvent(token, new ColorExecutorEvent(color));
            } catch (InvalidActionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(String json) throws IOException {
        final String token = null;
        final String gameName = null;
        final Position position = null;

        // TODO mattia ready json

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token))
            return;

        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            IGame game = gameManager.getGameByName(gameName);

            if (checkSynchronize(token, gameName)) {
                viewMap.get(token).err("You're not synchronized with the model and need to reconnect");
            }

            try {
                game.userFireExecutorEvent(token, new PositionExecutorEvent(position));
            } catch (InvalidActionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reconnect(String json, IGameView gameView, IStateObserver stateObserver, Map<String, IPlayerObserver> playerObserver,
                          Map<String, IToolCardObserver> toolCardObserver, Map<String, ISchemaCardObserver> schemaCardObserver, IGameObserver gameObserver,
                          IDraftPoolObserver draftPoolObserver, IRoundTrackObserver roundTrackObserver, IDrawableCollectionObserver
                                  diceBagObserver) throws IOException {
        final String token = null;
        final String gameName = null;

        // TODO mattia read json

        // check if the token is the one of a disconnected player
        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);

            ObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
            if (!observerManager.getDisconnectedPlayer().contains(token)) {
                gameView.err("A player with this name is already connected to the game");
            }

            // check if given data are corrects
            IGame game = gameManager.getGameByName(gameName);
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

            // Attaching observer and view regarding the re-connected player
            if (viewMap.containsKey(token)) {
                viewMap.replace(token, gameView);
            }

            game.getPlayers().forEach(player -> game.attachPlayerObserver(token, player, new PlayerFakeObserver(token, observerManager, playerObserver.get(player.getToken()))));
            game.getPlayers().forEach(player -> game.attachSchemaCardObserver(token, player.getSchemaCard(),
                    new SchemaCardFakeObserver(token, observerManager, schemaCardObserver.get(player.getToken()))));
            game.getToolCards().forEach(toolCard -> game.attachToolCardObserver(token, toolCard, new ToolCardFakeObserver(token, observerManager, toolCardObserver.get(toolCard.getName()))));
            game.attachDiceBagObserver(token, new DrawableCollectionFakeObserver<>(token, diceBagObserver, observerManager));
            game.attachDraftPoolObserver(token, new DraftPoolFakeObserver(token, draftPoolObserver, observerManager));
            game.attachGameObserver(token, new GameFakeObserver(token, gameObserver, observerManager));
            game.attachRoundTrackObserver(token, new RoundTrackFakeObserver(token, roundTrackObserver, observerManager));
            game.attachStateObserver(token, new StateFakeObserver(token, observerManager, stateObserver));

            // synchronize data
            synchronizeModel(token, gameName);

            observerManager.signalReconnect(token);
            gameView.ack("You are now riconnected to the game");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolCards(String json) throws IOException {
        final String token = null;
        final String gameName = null;
        // TODO MATTIA ready json

        Map<String, String> jsonError = new HashMap<>();
        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            jsonError.putIfAbsent("error", "Can't get because you're not logged or you are not part of the specified " +
            "game or the specified does not exist.");
            return JSONObject.toJSONString(jsonError);
        }

        List<ToolCard> toolCards;

        synchronized (gameManager.getGameByName(gameName)) {
            toolCards = gameManager.getGameByName(gameName).getToolCards();
        }

        StringBuilder toolCardsJSONString = new StringBuilder();
        toolCards.forEach(toolCard -> toolCardsJSONString.append(toolCard.toJSON().toJSONString()));
        return toolCardsJSONString.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDraftPool(String json) throws IOException {
        final String gameName = null;
        final String token = null;

        // Todo mattia read json

        Map<String, String> jsonError = new HashMap<>();
        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            jsonError.putIfAbsent("error", "Can't get because you're not logged or you are not part of the specified " +
                    "game or the specified does not exist.");
            return JSONObject.toJSONString(jsonError);
        }


        DraftPool draftPool;

        synchronized (gameManager.getGameByName(gameName)) {
            draftPool = gameManager.getGameByName(gameName).getDraftPool();
        }

        // BUILD JSON and RETURN IT
        return draftPool.toJSON().toJSONString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRoundTrack(String json) throws IOException {
        final String gameName = null;
        final String token = null;

        // Todo mattia read json

        Map<String, String> jsonError = new HashMap<>();
        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            jsonError.putIfAbsent("error", "Can't get because you're not logged or you are not part of the specified " +
                    "game or the specified does not exist.");
            return JSONObject.toJSONString(jsonError);
        }

        RoundTrack roundTrack;

        synchronized (gameManager.getGameByName(gameName)) {
            roundTrack = gameManager.getGameByName(gameName).getRoundTrack();
        }

        return roundTrack.toJSON().toJSONString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolCardByName(String json) throws IOException {
        final String gameName = null;
        final String token = null;
        final String toolCardName = null;

        // Todo mattia read json


        Map<String, String> jsonError = new HashMap<>();

        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            jsonError.putIfAbsent("error", "Can't get because you're not logged or you are not part of the" +
                    " specified game or the specified does not exist.");
            return JSONObject.toJSONString(jsonError);
        }

        List<ToolCard> toolCards;

        synchronized (gameManager.getGameByName(gameName)) {
            IGame game = gameManager.getGameByName(gameName);
            toolCards = game.getToolCards();
        }

        Optional<ToolCard> toolCard = toolCards.stream().filter(card -> card.getName().equals(toolCardName)).findFirst();
        if (!toolCard.isPresent()) {
            viewMap.get(token).err("Requested tool card does not exist in game");
            jsonError.putIfAbsent("error", "Can't get because the requested tool card does not exist in game");
            return JSONObject.toJSONString(jsonError);
        }

        return toolCard.get().toJSON().toJSONString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentPlayer(String json) throws IOException {
        final String gameName = null;
        final String token = null;

        // Todo mattia read json


        Map<String, String> jsonError = new HashMap<>();
        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getPlayersByGame(gameName).contains(token)) {
            jsonError.putIfAbsent("error", "Can't get because you're not logged or you are not part of the specified " +
            "game or the specified does not exist.");
            return JSONObject.toJSONString(jsonError);
        }

        Player player;

        synchronized (gameManager.getGameByName(gameName)) {
            try {
                player = gameManager.getGameByName(gameName).getCurrentPlayer();
            } catch (InvalidActionException e) {
                viewMap.get(token).err("Can't request for the current in this precise moment");
                jsonError.putIfAbsent("error", "Can't request for the current player in this precise moment");
                return JSONObject.toJSONString(jsonError);
            }
        }

        return player.toJSON().toJSONString();
    }


    /**
     * Returns true if a certain player has the synchronized model
     *
     * @param token    player's token
     * @param gameName game's name
     * @return true if synchronized, false otherwise
     */
    private boolean isSynchronized(String token, String gameName) {
        return gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token);
    }

    /**
     * Handles network error in server controller
     *
     * @param token
     * @param gameName
     */
    private void handleIOException(String token, String gameName) {
        // TODO (?)
    }

    /**
     * It cleans the observer of a certain game, with the notify disconnections.
     * It also signals the disconnections of the various player.
     *
     * @param gameName game's name
     */
    private void cleanObservers(String gameName) {
        ObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
        Set<String> toNotifyDisconnect = observerManager.getDisconnectedPlayerNotNotified();
        Set<String> disconnected = observerManager.getDisconnectedPlayer();
        List<Player> playerList = gameManager.getGameByName(gameName).getPlayers();

        toNotifyDisconnect.forEach(disconnectedToken -> {
            playerList.forEach(player -> {
                if (!disconnected.contains(player.getToken())) {
                    try {
                        Optional<User> username = playerList.stream().map(Player::getUser).filter((user) -> user.getToken().
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

    private boolean checkSynchronize(String token, String gameName) {
        return gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token);
    }

    private void synchronizeModel(String token, String gameName) throws IOException {
        synchronized (gameManager.getGameByName(gameName)) {
            cleanObservers(gameName);
            IGame game = gameManager.getGameByName(gameName);

            if (!(game.containsToken(token))) {
                viewMap.get(token).err("You can't synchronize on a game that you're not playing");
                return;
            }

            viewMap.get(token).notifyModelSynch(game.getDraftPool(), game.getPlayers(), game.getRoundTrack(), game.getToolCards());
        }
    }

}
