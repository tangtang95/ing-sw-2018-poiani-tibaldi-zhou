package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.poianitibaldizhou.sagrada.exception.NetworkException;
import org.poianitibaldizhou.sagrada.graphics.model.GameModel;
import org.poianitibaldizhou.sagrada.graphics.utils.Difficulty;
import org.poianitibaldizhou.sagrada.graphics.view.IGameViewStrategy;
import org.poianitibaldizhou.sagrada.graphics.view.MultiPlayerGameViewStrategy;
import org.poianitibaldizhou.sagrada.graphics.view.SinglePlayerGameViewStrategy;
import org.poianitibaldizhou.sagrada.graphics.view.component.RoundTrackView;
import org.poianitibaldizhou.sagrada.graphics.view.listener.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: Graphic controller for handling the game phase of Sagrada
 */
public class GameGraphicsController extends GraphicsController implements Initializable {

    @FXML
    public StackPane rootPane;
    @FXML
    public Pane corePane;
    @FXML
    public Pane notifyPane;

    private DraftPoolListener draftPoolListener;
    private RoundTrackListener roundTrackListener;
    private StateListener stateListener;
    private GameListener gameListener;
    private DiceBagListener diceBagListener;
    private TimeoutListener timeoutListener;

    private IGameViewStrategy gameViewStrategy;

    private GameModel gameModel;
    private Deque<VBox> stackMessages;
    private Thread messageHandlerThread;

    private static final long TIME_BETWEEN_TWO_MESSAGES_IN_MILLIS = 1000;

    /**
     * Initialize the notify pane, which a pane for showing the objects in a bigger way
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initNotifyPane();
        stackMessages = new ArrayDeque<>();
        messageHandlerThread = new Thread(()->{
            while (true) {
                synchronized (stackMessages) {
                    while (stackMessages.isEmpty()) {
                        try {
                            stackMessages.wait();
                        } catch (InterruptedException e) {
                            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    Platform.runLater(()->{
                        VBox messageBox = stackMessages.pop();
                        messageBox.setVisible(true);
                        ParallelTransition transition = (ParallelTransition) messageBox.getUserData();
                        transition.play();
                    });
                }
                try {
                    Thread.sleep(TIME_BETWEEN_TWO_MESSAGES_IN_MILLIS);
                } catch (InterruptedException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });
        messageHandlerThread.start();
    }

    /**
     * Update every views inside this controller
     *
     * @throws IOException network error
     */
    public void updateAllViews() throws IOException {
        draftPoolListener.updateView();
        roundTrackListener.updateView();
        gameListener.updateView();
        stateListener.updateView();
        timeoutListener.updateView();
        diceBagListener.updateView();
    }

    /**
     * Go to the ScorePlayerScene to show the winner
     *
     * @param winner        the user who won
     * @param victoryPoints the final points of each player
     */
    public void pushScorePlayerScene(UserWrapper winner, Map<UserWrapper, Integer> victoryPoints) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/score_scene.fxml"));

        try {
            Parent root = loader.load();
            ScorePlayerGraphicsController controller = loader.getController();
            controller.setSceneManager(sceneManager);
            controller.initScoreScene(winner, victoryPoints);
            playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.replaceScene(root));
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, ClientMessage.LOAD_FXML_ERROR);
        }
    }

    /**
     * Init the listeners for the basic game objects, and for the timeout
     */
    private void initListeners() {
        try {
            draftPoolListener = new DraftPoolListener(this, corePane, notifyPane);
            roundTrackListener = new RoundTrackListener(this, corePane, notifyPane,
                    new RoundTrackView(gameViewStrategy.getRoundTrackScale()));
            stateListener = new StateListener(this, corePane, notifyPane);
            gameListener = new GameListener(this, corePane, notifyPane);
            diceBagListener = new DiceBagListener(this, corePane, notifyPane);
            timeoutListener = new TimeoutListener(this, corePane, notifyPane);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Initialize the notify pane
     */
    private void initNotifyPane() {
        notifyPane.setVisible(false);
        notifyPane.prefWidthProperty().bind(rootPane.widthProperty());
        notifyPane.prefHeightProperty().bind(rootPane.heightProperty());
        notifyPane.toBack();
    }

    /**
     * Init method for Multi player games
     *
     * @param token             the token got from lobby
     * @param username          the username chosen
     * @param gameName          the game name got from lobby
     * @param connectionManager the manager of the connection
     * @throws NetworkException if cannot connect to the server
     */
    public void initMultiPlayerGame(String token, String username, String gameName, ConnectionManager connectionManager) throws NetworkException {
        gameViewStrategy = new MultiPlayerGameViewStrategy(corePane, notifyPane);
        initListeners();
        gameModel = new GameModel(username, token, gameName, connectionManager);
        try {
            gameModel.joinGame(gameListener, gameListener, stateListener,
                    roundTrackListener, draftPoolListener, diceBagListener, timeoutListener);
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Init method for Single player games
     *
     * @param username          the username chosen
     * @param difficulty        the difficulty level chosen
     * @param connectionManager the manager of the connection
     * @throws NetworkException if cannot connect to the server
     */
    public void initSinglePlayerGame(String username, Difficulty difficulty, ConnectionManager connectionManager) throws NetworkException {
        gameViewStrategy = new SinglePlayerGameViewStrategy(corePane);
        initListeners();
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createUsernameMessage(username).createValueMessage(difficulty.getDifficultyValue()).buildMessage();
        try {
            String response = connectionManager.getGameController().createSinglePlayer(request);
            String token = parser.getToken(response);
            String gameName = parser.getGameName(response);
            gameModel = new GameModel(username, token, gameName, connectionManager);
            gameModel.joinGame(gameListener, gameListener, stateListener, roundTrackListener, draftPoolListener,
                    diceBagListener, timeoutListener);
        } catch (IOException e) {
            throw new NetworkException(e);
        }

    }

    /**
     * Init method for Reconnecting to Multi Player Games
     *
     * @param username          the username used before
     * @param connectionManager the manager of the connection
     * @throws NetworkException network error
     */
    public void initReconnectMultiPlayerGame(String username, ConnectionManager connectionManager) throws NetworkException {
        gameViewStrategy = new MultiPlayerGameViewStrategy(corePane, notifyPane);
        initListeners();
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createUsernameMessage(username).buildMessage();
        try {
            String response = connectionManager.getGameController().attemptReconnect(request);
            if (parser.hasReconnectError(response))
                throw new NetworkException(ClientMessage.NO_GAME_AVAILABLE);
            String token = parser.getToken(response);
            String gameName = parser.getGameName(response);
            gameModel = new GameModel(username, token, gameName, connectionManager);
            List<UserWrapper> userList = parser.getListOfUserWrapper(response);
            Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap = gameModel.getSchemaCardMap();
            Map<UserWrapper, Integer> coinMap = gameModel.getCoinsMap();
            List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers = gameModel.getOwnPrivateObjectiveCard();
            gameListener.drawUsers(userList, schemaCardWrapperMap, privateObjectiveCardWrappers, coinMap);
            List<ToolCardWrapper> toolCardWrappers = gameModel.getToolCards();
            gameListener.drawToolCards(toolCardWrappers, gameViewStrategy.getToolCardScale());
            List<PublicObjectiveCardWrapper> publicObjectiveCardWrappers = gameModel.getPublicObjectiveCards();
            gameListener.drawPublicObjectiveCards(publicObjectiveCardWrappers, gameViewStrategy.getPublicObjectiveCardScale());
            connectionManager.getGameController().reconnect(
                    builder.createUsernameMessage(username).buildMessage(),
                    gameListener, gameListener, roundTrackListener, stateListener, gameListener.getPlayerObservers(),
                    gameListener.getToolCardObservers(), gameListener.getSchemaCardObservers(), draftPoolListener,
                    diceBagListener, timeoutListener);
            updateAllViews();
            drawRoundTrack();
            drawDraftPool();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Draw the Round Track
     */
    public void drawRoundTrack() {
        roundTrackListener.drawRoundTrack();
    }

    /**
     * Draw the Draft Pool
     */
    public void drawDraftPool() {
        draftPoolListener.drawDraftPool();
    }

    // REQUEST TO THE GAME MODEL

    /**
     * Request signaling that a schema card has been chosen
     *
     * @param schemaCardWrapper chosen schema card
     * @throws IOException network communication error
     */
    public void chooseSchemaCard(SchemaCardWrapper schemaCardWrapper) throws IOException {
        gameModel.chooseSchemaCard(schemaCardWrapper);
    }

    /**
     * Get the schema cards of the various player that have joined the game
     *
     * @return map of users matched with their schema card
     * @throws IOException network communication error
     */
    public Map<UserWrapper, SchemaCardWrapper> getSchemaCardMap() throws IOException {
        return gameModel.getSchemaCardMap();
    }

    /**
     * Get the user name of the client
     *
     * @return client's username
     */
    public String getUsername() {
        return gameModel.getUsername();
    }

    /**
     * Get the private objective cards of the player who is playing with this client
     *
     * @return list of private objective cards of the player who is playing with this client
     * @throws IOException network communication error
     */
    public List<PrivateObjectiveCardWrapper> getOwnPrivateObjectiveCard() throws IOException {
        return gameModel.getOwnPrivateObjectiveCard();
    }

    /**
     * Get the round track of the game
     *
     * @return game's round track
     * @throws IOException network communication error
     */
    public RoundTrackWrapper getRoundTrack() throws IOException {
        return gameModel.getRoundTrack();
    }

    /**
     * Send a request of binding a certain player to get the notification of modification and changes
     *
     * @param user               user that will be bind
     * @param playerObserver     observer that will listen to modification of user
     * @param schemaCardObserver observer that will listen to modification of user's schema card
     * @throws IOException network communication error
     */
    public void bindPlayer(UserWrapper user, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws IOException {
        gameModel.bindPlayer(user, playerObserver, schemaCardObserver);
    }

    /**
     * Get the draft pool of the game
     *
     * @return draft pool of the game
     * @throws IOException network communication error
     */
    public DraftPoolWrapper getDraftPool() throws IOException {
        return gameModel.getDraftPool();
    }

    /**
     * Send a request of binding a certain tool card to get the notification of its modification and changes
     *
     * @param toolCard         tool card that will be bind
     * @param toolCardObserver observer that will listen to modification of tool card
     * @throws IOException network communication error
     */
    public void bindToolCard(ToolCardWrapper toolCard, IToolCardObserver toolCardObserver) throws IOException {
        gameModel.bindToolCard(toolCard, toolCardObserver);
    }

    /**
     * Send a request of terminating the turn
     *
     * @throws IOException network communication error
     */
    public void endTurn() throws IOException {
        gameModel.endTurn();
    }

    /**
     * Get the map of the coins of the various players
     *
     * @return map of player associated with their coins
     * @throws IOException network communication error
     */
    public Map<UserWrapper, Integer> getCoinsMap() throws IOException {
        List<UserWrapper> userWrappers = gameModel.getUserList();
        if (userWrappers.size() == 1) {
            Map<UserWrapper, Integer> fakeCoin = new HashMap<>();
            fakeCoin.putIfAbsent(userWrappers.get(0), -1);
            return fakeCoin;
        }
        return gameModel.getCoinsMap();
    }

    /**
     * Get the schema card of the user that is playing with this client
     *
     * @return schema card of the user playing with this client
     * @throws IOException network communication error
     */
    public SchemaCardWrapper getOwnSchemaCard() throws IOException {
        return gameModel.getOwnSchemaCard();
    }

    /**
     * Send a request of placing a dice in a certain position
     *
     * @param dice            dice that is asked for being placed
     * @param positionWrapper the dice will be placed in this position
     * @throws IOException network communication error
     */
    public void placeDice(DiceWrapper dice, PositionWrapper positionWrapper) throws IOException {
        gameModel.placeDice(dice, positionWrapper);
    }

    /**
     * Get the public objective cards of this game
     *
     * @return public cards of the game
     * @throws IOException network communication error
     */
    public List<PublicObjectiveCardWrapper> getPublicObjectiveCards() throws IOException {
        return gameModel.getPublicObjectiveCards();
    }

    /**
     * Get the tool cards of the game
     *
     * @return tool cards of the game
     * @throws IOException network communication error
     */
    public List<ToolCardWrapper> getToolCards() throws IOException {
        return gameModel.getToolCards();
    }

    /**
     * Get the list of users that are playing in this game.
     * This does not implies that all the users in here has connected to the server once that the lobby has determinated,
     * neither they are active and actually connected.
     *
     * @return list of users that are playing this game
     * @throws IOException network communication error
     */
    public List<UserWrapper> getUserList() throws IOException {
        return gameModel.getUserList();
    }

    /**
     * Send a request of using a tool card
     *
     * @param toolCardWrapper  the client wants to use this tool card
     * @param executorObserver tool card executor observer that is needed for interact with the server during the
     *                         user of the tool card
     * @throws IOException network communication error
     */
    public void useToolCard(ToolCardWrapper toolCardWrapper, IToolCardExecutorObserver executorObserver) throws IOException {
        gameModel.useToolCard(toolCardWrapper, executorObserver);
    }

    /**
     * Send a dice to the server
     *
     * @param diceWrapper dice that will be sent
     * @throws IOException network communication error
     */
    public void sendDiceObject(DiceWrapper diceWrapper) throws IOException {
        gameModel.sendDiceObject(diceWrapper);
    }

    /**
     * Send a color to the server
     *
     * @param colorWrapper color that will be sent
     * @throws IOException network communication error
     */
    public void sendColorObject(ColorWrapper colorWrapper) throws IOException {
        gameModel.sendColorObject(colorWrapper);
    }

    /**
     * Send a position to the server
     *
     * @param positionWrapper position that will be sent
     * @throws IOException network communication error
     */
    public void sendPositionObject(PositionWrapper positionWrapper) throws IOException {
        gameModel.sendPositionObject(positionWrapper);
    }

    /**
     * Send an answer to the server. The answer is a boolean value that represents yes or no.
     *
     * @param answer answer that will be sent
     * @throws IOException network communication error
     */
    public void sendAnswerObject(boolean answer) throws IOException {
        gameModel.sendAnswerObject(answer);
    }

    /**
     * Send a integer value to the server
     *
     * @param value integer value that will be sent
     * @throws IOException network communication error
     */
    public void sendValueObject(int value) throws IOException {
        gameModel.sendValueObject(value);
    }

    /**
     * Get the token of the user who is playing with this client
     *
     * @return token of the user who is playing with this client
     * @throws IOException network communication error
     */
    public int getOwnToken() throws IOException {
        return gameModel.getOwnToken();
    }

    /**
     * Get the tool card by its name
     *
     * @param toolCardWrapper tool card wanted
     * @return tool card asked
     * @throws IOException network communication error
     */
    public ToolCardWrapper getToolCardByName(ToolCardWrapper toolCardWrapper) throws IOException {
        return gameModel.getToolCardByName(toolCardWrapper);
    }

    /**
     * Return a strategy that is used for handling the differences between single and multi player.
     *
     * @return strategy used for handling the differences between single and multi player
     */
    public IGameViewStrategy getGameViewStrategy() {
        return gameViewStrategy;
    }

    /**
     * Send a request of destroying a certain tool card
     *
     * @param toolCardListener tool card that will be destroyed
     */
    public void destroyToolCard(ToolCardListener toolCardListener) {
        gameListener.destroyToolCard(toolCardListener);
    }

    /**
     * Send a request for choosing a private card
     *
     * @param privateObjectiveCardWrapper private card chosen
     * @throws IOException network communication error
     */
    public void choosePrivateObjectiveCard(PrivateObjectiveCardWrapper privateObjectiveCardWrapper) throws IOException {
        gameModel.choosePrivateObjectiveCard(privateObjectiveCardWrapper);
    }

    /**
     * Send a request of leaving the game
     *
     * @throws IOException network communication error
     */
    public void quitGame() throws IOException {
        gameModel.quitGame();
        popGameScene();
    }

    /**
     * Get the timeout in millis
     *
     * @return timeout in millis
     * @throws IOException network communication error
     */
    public long getMillisTimeout() throws IOException {
        return gameModel.getMillisTimeout();
    }

    /**
     * Pop the actual game scene from the sceneManager
     */
    public void popGameScene() {
        messageHandlerThread.interrupt();
        playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.popScene());
    }

    /**
     * Push the container inside the stack of messages handled by another thread
     * @param container
     */
    public void pushMessage(VBox container) {
        synchronized (stackMessages) {
            stackMessages.push(container);
            stackMessages.notifyAll();
        }
    }

    /**
     * Add a text message to the logger text area
     * @param text the message to add
     */
    public void addMessageToLoggerTextArea(String text) {
        gameListener.addLoggerMessage(text);
    }
}
