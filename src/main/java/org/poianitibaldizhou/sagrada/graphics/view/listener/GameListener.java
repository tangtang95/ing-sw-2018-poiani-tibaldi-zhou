package org.poianitibaldizhou.sagrada.graphics.view.listener;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;
import org.poianitibaldizhou.sagrada.graphics.view.component.*;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: Listen to the modification of the game
 */
public class GameListener extends AbstractView implements IGameView, IGameObserver {

    private transient Pane publicObjectiveCardsContainer;
    private transient Pane toolCardsContainer;

    private final transient Map<UserWrapper, SchemaCardListener> schemaCardViewMap;
    private final transient List<ToolCardListener> toolCardListeners;
    private final transient Map<UserWrapper, PlayerListener> playerListenerMap;

    private static final double FRONT_BACK_SCHEMA_CARD_SCALE = 0.75;
    private static final double PRIVATE_OBJECTIVE_CARD_SHOW_SCALE_AT_FIRST = 0.9;
    private static final double PUBLIC_OBJECTIVE_CARD_SHOW_SCALE = 1;
    private static final double PRIVATE_OBJECTIVE_CARD_SHOW_SCALE = 1;
    private static final double TOOL_CARD_SHOW_SCALE = 1;

    private static final double PADDING = 10;

    private static final String CSS_CLASS1 = "on-notify-pane-card";
    private static final String CSS_CLASS2 = "on-board-card";

    /**
     * Constructor.
     * Create a Game listener that update majorly the corePane when there are new notifies
     *
     * @param controller the game controller of the GUI
     * @param corePane   the core view of the game
     * @param notifyPane the view of the game to show the image on a greater size
     * @throws RemoteException network error
     */
    public GameListener(GameGraphicsController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        schemaCardViewMap = new HashMap<>();
        toolCardListeners = new ArrayList<>();
        playerListenerMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateView() {
        schemaCardViewMap.values().forEach(AbstractView::updateView);
        toolCardListeners.forEach(AbstractView::updateView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ack(String ack) throws IOException {
        /*NOT IMPORTANT FOR GUI*/
        Logger.getAnonymousLogger().log(Level.INFO, ack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void err(String err) throws IOException {
        /*NOT IMPORTANT FOR GUI*/
        Logger.getAnonymousLogger().log(Level.INFO, err);
        Platform.runLater(() -> showMessage(getActivePane(), err, MessageType.ERROR));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ping() throws IOException {
        /*NOT IMPORTANT FOR GUI*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlayersCreate(String message) throws IOException {
        Platform.runLater(() -> {
            Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap;
            List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers;
            Map<UserWrapper, Integer> favorTokeMap;
            List<UserWrapper> users;
            try {
                privateObjectiveCardWrappers = controller.getOwnPrivateObjectiveCard();
                favorTokeMap = controller.getCoinsMap();
                schemaCardWrapperMap = controller.getSchemaCardMap();
                users = controller.getUserList();
                this.drawUsers(users, schemaCardWrapperMap, privateObjectiveCardWrappers, favorTokeMap);
                for (UserWrapper userWrapper : users) {
                    controller.bindPlayer(userWrapper, playerListenerMap.get(userWrapper),
                            schemaCardViewMap.get(userWrapper));
                }

            } catch (IOException e) {
                this.showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPublicObjectiveCardsDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<PublicObjectiveCardWrapper> publicObjectiveCardWrappers = parser.getPublicObjectiveCards(message);

        Platform.runLater(() -> {
            this.drawPublicObjectiveCards(publicObjectiveCardWrappers, controller.getGameViewStrategy().getPublicObjectiveCardScale());
            controller.drawRoundTrack();
            controller.drawDraftPool();
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToolCardsDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<ToolCardWrapper> toolCardWrappers = parser.getToolCards(message);

        Platform.runLater(() -> {
            try {
                this.drawToolCards(toolCardWrappers, controller.getGameViewStrategy().getToolCardScale());
                for (ToolCardListener listener : toolCardListeners) {
                    controller.bindToolCard(listener.getToolCardView().getToolCardWrapper(), listener);
                }
            } catch (IOException e) {
                showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChoosePrivateObjectiveCards(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<PrivateObjectiveCardWrapper> privateObjectiveCards = parser.getPrivateObjectiveCards(message);

        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            this.showChoosePrivateObjectiveCards(privateObjectiveCards);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPrivateObjectiveCardDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<PrivateObjectiveCardWrapper> privateObjectiveCards = parser.getPrivateObjectiveCards(message);
        Platform.runLater(() -> this.showPrivateObjectiveCards(privateObjectiveCards));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSchemaCardsDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<FrontBackSchemaCardWrapper> frontBackSchemaCards = parser.getFrontBackSchemaCards(message);

        Platform.runLater(() -> this.showFrontBackSchemaCards(frontBackSchemaCards));
    }

    /**
     * @return a map that contains every playerObserver inside the gameListener
     */
    public Map<String, IPlayerObserver> getPlayerObservers() {
        Map<String, IPlayerObserver> playerObserverMap = new HashMap<>();
        playerListenerMap.forEach((key, value) -> playerObserverMap.put(key.getUsername(), value));
        return playerObserverMap;
    }

    /**
     * @return a map that contains every toolCardObserver inside the gameListener
     */
    public Map<String, IToolCardObserver> getToolCardObservers() {
        Map<String, IToolCardObserver> toolCardObserverMap = new HashMap<>();
        toolCardListeners.forEach(toolCardListener -> toolCardObserverMap.put(toolCardListener.getToolCardName(),
                toolCardListener));
        return toolCardObserverMap;
    }

    /**
     * @return a map that contains every SchemaCardObserver inside the gameListener
     */
    public Map<String, ISchemaCardObserver> getSchemaCardObservers() {
        Map<String, ISchemaCardObserver> schemaCardObserverMap = new HashMap<>();
        schemaCardViewMap.forEach((key, value) -> schemaCardObserverMap.put(key.getUsername(), value));
        return schemaCardObserverMap;
    }

    /**
     * Draw every player of the Game, his schemaCard, his name, his favorTokens and his privateObjectiveCards
     *
     * @param users                        the list of user
     * @param schemaCardWrapperMap         the map of schemaCard
     * @param privateObjectiveCardWrappers the list of private objective cards
     * @param favorTokenMap                the map of favor tokens
     */
    public void drawUsers(List<UserWrapper> users, Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap,
                          List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers,
                          Map<UserWrapper, Integer> favorTokenMap) {
        List<UserWrapper> orderedUsers = getUsersOrdered(users);
        for (int i = 0; i < orderedUsers.size(); i++) {
            final double angle = 2 * Math.PI * i / ((orderedUsers.size() == 3) ? orderedUsers.size() + 1 :
                    orderedUsers.size()) + Math.PI / 2 + ((i == 2 && orderedUsers.size() == 3) ? Math.PI / 2 : 0);

            SchemaCardView schemaCardView = controller.getGameViewStrategy().drawSchemaCardView(corePane,
                    schemaCardWrapperMap.get(orderedUsers.get(i)), angle);

            // CALCULATE PRIVATE OBJECTIVE CARD POSITION
            final double tangentAngle = angle - Math.PI / 2;
            DoubleBinding tangentDistance = schemaCardView.widthProperty().divide(2).add(PADDING * 2);
            DoubleBinding pocX = schemaCardView.translateXProperty().add(schemaCardView.widthProperty().divide(2))
                    .add(tangentDistance.multiply(Math.cos(tangentAngle)));
            DoubleBinding pocY = schemaCardView.translateYProperty().add(schemaCardView.heightProperty().divide(2))
                    .add(tangentDistance.multiply(Math.sin(tangentAngle)));

            // DRAW PRIVATE OBJECTIVE CARD
            if (i == 0)
                drawPrivateObjectiveCards(privateObjectiveCardWrappers, pocX, pocY);
            else
                drawRetroPrivateObjectiveCard(pocX, pocY, angle);

            try {
                SchemaCardListener schemaCardListener = new SchemaCardListener(schemaCardView,
                        controller, corePane, notifyPane, orderedUsers.get(i));
                schemaCardViewMap.putIfAbsent(orderedUsers.get(i), schemaCardListener);

                PlayerView playerView = drawUser(orderedUsers.get(i), favorTokenMap.get(orderedUsers.get(i)),
                        new Point2D(Math.round(Math.cos(angle)), Math.round(Math.sin(angle))));
                PlayerListener playerListener = new PlayerListener(playerView, controller, corePane, notifyPane,
                        orderedUsers.get(i));
                playerListenerMap.putIfAbsent(orderedUsers.get(i), playerListener);
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot initialize SchemaCardListener");
                showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
            }
        }
    }

    /**
     * Draw the public objective cars
     *
     * @param publicObjectiveCardWrappers public objective cards that will be drawn
     * @param publicObjectiveCardScale    scale of the public objective cards images
     */
    public void drawPublicObjectiveCards(List<PublicObjectiveCardWrapper> publicObjectiveCardWrappers, double publicObjectiveCardScale) {
        publicObjectiveCardsContainer = drawPublicObjectiveCardsView(corePane, publicObjectiveCardWrappers, publicObjectiveCardScale);
        publicObjectiveCardsContainer.setOnMousePressed(this::onPublicObjectiveCardsPressed);
    }

    /**
     * Draw the tool cards
     *
     * @param toolCardWrappers tool cards that will be drawn
     * @param scale            scale of the tool card images
     * @throws RemoteException due to the creation of a listener
     */
    public void drawToolCards(List<ToolCardWrapper> toolCardWrappers, double scale) throws RemoteException {

        toolCardsContainer = new Pane();

        DoubleBinding x = new SimpleDoubleProperty(0).add(PADDING * 2);
        DoubleBinding y = new SimpleDoubleProperty(0).add(corePane.heightProperty()
                .multiply(AbstractView.HELPER_BAR_PERCENT_HEIGHT).add(PADDING));

        toolCardsContainer.translateXProperty().bind(x);
        toolCardsContainer.translateYProperty().bind(y);

        for (int i = 0; i < toolCardWrappers.size(); i++) {
            ToolCardView toolCardView = new ToolCardView(toolCardWrappers.get(i), scale);
            toolCardView.setTranslateX(i * PADDING);
            toolCardView.setTranslateY(i * PADDING);
            ToolCardListener toolCardListener = new ToolCardListener(toolCardView, controller, corePane, notifyPane);
            toolCardListeners.add(toolCardListener);
            toolCardsContainer.getChildren().add(toolCardView);
        }

        toolCardsContainer.setOnMousePressed(this::onToolCardsPressed);
        toolCardsContainer.getStyleClass().add(CSS_CLASS2);

        corePane.getChildren().add(toolCardsContainer);

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GameListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

    /**
     * Shows the front back schema cards.
     * This method is used when a focus on the front back schema card is needed
     *
     * @param frontBackSchemaCardList front back schema cards that will be shown
     */
    private void showFrontBackSchemaCards(List<FrontBackSchemaCardWrapper> frontBackSchemaCardList) {

        List<Pane> frontBackSchemaCardViewList = new ArrayList<>();
        frontBackSchemaCardList.forEach(frontBackSchemaCard -> {
            FrontBackSchemaCardView frontBackSchemaCardView = new FrontBackSchemaCardView(frontBackSchemaCard,
                    FRONT_BACK_SCHEMA_CARD_SCALE);
            frontBackSchemaCardViewList.add(frontBackSchemaCardView);
        });

        drawCenteredPanes(notifyPane, frontBackSchemaCardViewList, CSS_CLASS1);
        ToggleGroup toggleGroup = new ToggleGroup();
        drawRadioButtons(toggleGroup, frontBackSchemaCardViewList);
        HBox helperBox = showHelperText(notifyPane,
                ClientMessage.INSTRUCTION_MESSAGE1);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
        continueButton.setOnAction(event -> {
            if (toggleGroup.getSelectedToggle() == null) {
                showMessage(notifyPane, ClientMessage.CHOOSE_SCHEMA_CARD_ITA, MessageType.ERROR);
                return;
            }
            FrontBackSchemaCardView schemaCardView = (FrontBackSchemaCardView) toggleGroup.getSelectedToggle().getUserData();
            try {
                controller.chooseSchemaCard(schemaCardView.getCurrentSchemaCardWrapper());
                toggleGroup.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));
                ((Button) event.getSource()).setDisable(true);
            } catch (IOException e) {
                showMessage(notifyPane, ClientMessage.CONNECTION_ERROR, MessageType.ERROR);
                return;
            }
            showMessage(notifyPane, ClientMessage.WAIT_FOR_USER, MessageType.INFO);
        });

        helperBox.getChildren().addAll(spacer, continueButton);
    }

    /**
     * Show the private cards.
     * This method is used when a focus on the private cards is needed.
     *
     * @param privateObjectiveCards private cards that will be shown
     */
    private void showPrivateObjectiveCards(List<PrivateObjectiveCardWrapper> privateObjectiveCards) {

        DoubleBinding x = getWidth().subtract(PADDING);
        DoubleBinding startY = new SimpleDoubleProperty(0).add(PADDING);

        for (int i = 0; i < privateObjectiveCards.size(); i++) {
            PrivateObjectiveCardWrapper privateObjectiveCardWrapper = privateObjectiveCards.get(i);
            PrivateObjectiveCardView objectiveCardView = new PrivateObjectiveCardView(privateObjectiveCardWrapper,
                    PRIVATE_OBJECTIVE_CARD_SHOW_SCALE_AT_FIRST);
            DoubleBinding y = startY.add(objectiveCardView.heightProperty().add(PADDING).multiply(i));

            objectiveCardView.translateXProperty().bind(getPivotX(x, objectiveCardView.widthProperty(), 0));
            objectiveCardView.translateYProperty().bind(getPivotY(y, objectiveCardView.heightProperty(), 1));

            notifyPane.getChildren().add(objectiveCardView);
        }
    }

    /**
     * Show a dialog that enable the user to choose a private objective cards.
     * This method gives a focus on the private objective cards.
     *
     * @param privateObjectiveCards the schema card that will be chosen belongs to this list
     */
    private void showChoosePrivateObjectiveCards(List<PrivateObjectiveCardWrapper> privateObjectiveCards) {
        List<Pane> privateObjectiveCardViews = new ArrayList<>();
        privateObjectiveCards.forEach(privateObjectiveCardWrapper ->
                privateObjectiveCardViews.add(new PrivateObjectiveCardView(privateObjectiveCardWrapper,
                        PRIVATE_OBJECTIVE_CARD_SHOW_SCALE)));
        drawCenteredPanes(notifyPane, privateObjectiveCardViews, CSS_CLASS1);
        ToggleGroup toggleGroup = new ToggleGroup();
        drawRadioButtons(toggleGroup, privateObjectiveCardViews);
        HBox helperBox = showHelperText(notifyPane,
                ClientMessage.INSTRUCTION_MESSAGE2);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
        continueButton.setOnAction(event -> {
            if (toggleGroup.getSelectedToggle() == null) {
                showMessage(notifyPane, ClientMessage.CHOOSE_PRIVATE_OBJECTIVE_CARD_ITA, MessageType.ERROR);
                return;
            }
            PrivateObjectiveCardView privateObjectiveCardView = (PrivateObjectiveCardView) toggleGroup
                    .getSelectedToggle().getUserData();
            try {
                controller.choosePrivateObjectiveCard(privateObjectiveCardView.getPrivateObjectiveCardWrapper());
                toggleGroup.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));
                ((Button) event.getSource()).setDisable(true);
            } catch (IOException e) {
                showMessage(notifyPane, ClientMessage.CONNECTION_ERROR, MessageType.ERROR);
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
            }
        });

        helperBox.getChildren().addAll(spacer, continueButton);
    }

    /**
     * Draw an user
     *
     * @param userWrapper user that will be drawn
     * @param favorTokens user's favor tokens
     * @param direction   indicates where to drawn the user
     * @return player view of the drawn user
     */
    private PlayerView drawUser(UserWrapper userWrapper, int favorTokens, Point2D direction) {
        PlayerView playerView;
        if (favorTokens >= 0)
            playerView = new PlayerView(userWrapper, favorTokens);
        else
            playerView = new PlayerView(userWrapper, 10.0);
        if (direction.equals(new Point2D(1, 0))) {
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper).getSchemaCardView();
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.heightProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().add(schemaCardView.widthProperty()).add(PADDING);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(getPivotY(y, playerView.heightProperty(), 0));
        } else if (direction.equals(new Point2D(0, 1))) {
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper).getSchemaCardView();
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.widthProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().add(schemaCardView.heightProperty()).add(PADDING);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(y);
        } else if (direction.equals(new Point2D(-1, 0))) {
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper).getSchemaCardView();
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.heightProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().subtract(PADDING * 2);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(getPivotY(y, playerView.heightProperty(), 0));
        } else {
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper).getSchemaCardView();
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.widthProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().subtract(PADDING);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(getPivotY(y, playerView.heightProperty(), 0));
        }
        corePane.getChildren().add(playerView);
        return playerView;
    }

    /**
     * Draw the public cards
     *
     * @param corePane                    used to draw the public cards
     * @param publicObjectiveCardWrappers public cards that will be drawn
     * @param scale                       scale of the public cards
     * @return pane that contains the public cards
     */
    private Pane drawPublicObjectiveCardsView(Pane corePane, List<PublicObjectiveCardWrapper> publicObjectiveCardWrappers, double scale) {
        Pane container = new Pane();
        container.getStyleClass().add(CSS_CLASS2);


        DoubleBinding y = new SimpleDoubleProperty(PADDING)
                .add(corePane.heightProperty().multiply(AbstractView.HELPER_BAR_PERCENT_HEIGHT));

        for (int i = 0; i < publicObjectiveCardWrappers.size(); i++) {
            PublicObjectiveCardView publicObjectiveCardView = new PublicObjectiveCardView(
                    publicObjectiveCardWrappers.get(i), scale);
            publicObjectiveCardView.setTranslateX((publicObjectiveCardWrappers.size() - i - 1) * PADDING);
            publicObjectiveCardView.setTranslateY(i * PADDING);

            container.getChildren().add(publicObjectiveCardView);
        }

        container.translateXProperty().bind(corePane.widthProperty()
                .subtract(PADDING * (publicObjectiveCardWrappers.size() + 1))
                .subtract(container.widthProperty()));
        container.translateYProperty().bind(y);
        corePane.getChildren().add(container);

        return container;
    }

    /**
     * Actions performed when the public cards are pressed
     *
     * @param event pressed mouse event
     */
    private void onPublicObjectiveCardsPressed(MouseEvent event) {
        clearNotifyPane(true);
        activateNotifyPane();

        try {
            List<PublicObjectiveCardWrapper> publicObjectiveCardList = controller.getPublicObjectiveCards();
            List<Pane> publicObjectiveCardViews = new ArrayList<>();
            publicObjectiveCardList.forEach(publicObjectiveCard -> {
                PublicObjectiveCardView cardView =
                        new PublicObjectiveCardView(publicObjectiveCard, PUBLIC_OBJECTIVE_CARD_SHOW_SCALE);
                publicObjectiveCardViews.add(cardView);
            });
            drawCenteredPanes(notifyPane, publicObjectiveCardViews, CSS_CLASS1);
            drawSimpleCloseHelperBox(notifyPane, "Carte obiettivo pubbliche");

        } catch (IOException e) {
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
        }
        event.consume();
    }

    /**
     * Action performed when the tool cards are pressed
     *
     * @param event pressed mouse event
     */
    private void onToolCardsPressed(MouseEvent event) {
        clearNotifyPane(true);
        activateNotifyPane();

        try {
            List<ToolCardWrapper> toolCardList = controller.getToolCards();
            if (!toolCardList.isEmpty()) {
                List<Pane> toolCardViews = new ArrayList<>();
                toolCardList.forEach(toolCardWrapper -> {
                    Pane cardView = new ToolCardView(toolCardWrapper, TOOL_CARD_SHOW_SCALE);
                    toolCardViews.add(cardView);
                });
                drawCenteredPanes(notifyPane, toolCardViews, CSS_CLASS1);
                drawSimpleCloseHelperBox(notifyPane, "Carte utensili");
            }
        } catch (IOException e) {
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
        }
        event.consume();
    }

    /**
     * Destroy a tool cards from the list of tool cards of the game
     *
     * @param toolCardListener tool card listener of the tool card that will be destroyed
     */
    public void destroyToolCard(ToolCardListener toolCardListener) {
        toolCardsContainer.getChildren().remove(toolCardListener.getToolCardView());
        toolCardListeners.remove(toolCardListener);
        if (toolCardListeners.isEmpty())
            corePane.getChildren().remove(toolCardsContainer);
    }

    /**
     * Draw the private objective cards
     *
     * @param privateObjectiveCardWrappers private cards that will be drawn
     * @param x                            where to draw the card on the x axis
     * @param y                            where to draw the card on the y axis
     */
    private void drawPrivateObjectiveCards(List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers,
                                           DoubleBinding x, DoubleBinding y) {
        Pane container = new Pane();
        for (int i = 0; i < privateObjectiveCardWrappers.size(); i++) {
            PrivateObjectiveCardView cardView = new PrivateObjectiveCardView(
                    privateObjectiveCardWrappers.get(i), controller.getGameViewStrategy().getPrivateObjectiveCardScale());
            cardView.setTranslateX(PADDING * i);
            cardView.setTranslateY(PADDING * i);
            container.getChildren().add(cardView);
        }
        DoubleBinding cardX = x.add(container.widthProperty().divide(2));
        container.translateXProperty().bind(getPivotX(cardX, container.widthProperty(), 0.5));
        container.translateYProperty().bind(getPivotY(y, container.heightProperty(), 0.5));
        container.getStyleClass().add(CSS_CLASS2);
        container.setOnMousePressed(this::onPrivateObjectivePressed);
        corePane.getChildren().add(container);
    }

    /**
     * Draw the retro of the private objective card
     *
     * @param x     where to draw the retro of the private card on the x axis
     * @param y     where to draw the retro of the private card on the y axis
     * @param angle
     */
    private void drawRetroPrivateObjectiveCard(DoubleBinding x, DoubleBinding y, double angle) {
        PrivateObjectiveCardView cardView = new PrivateObjectiveCardView(
                controller.getGameViewStrategy().getPrivateObjectiveCardScale());
        if (Math.abs(Math.abs(angle) - 2 * Math.PI) < 0.0001f || Math.abs(Math.abs(angle) - Math.PI) < 0.0001f) {
            DoubleBinding cardY = y.add(cardView.widthProperty().divide(2).multiply(Math.sin(angle - 90)));
            cardView.translateXProperty().bind(getPivotX(x, cardView.widthProperty(), 0.5));
            cardView.translateYProperty().bind(getPivotY(cardY, cardView.heightProperty(), 0.5));
        } else {
            DoubleBinding cardX = x.add(cardView.widthProperty().divide(2).multiply(Math.cos(angle - 90)));
            cardView.translateXProperty().bind(getPivotX(cardX, cardView.widthProperty(), 0.5));
            cardView.translateYProperty().bind(getPivotY(y, cardView.heightProperty(), 0.5));
        }
        cardView.setRotate(angle * 180 / Math.PI - 90);
        corePane.getChildren().add(cardView);
        cardView.toFront();
    }

    /**
     * Action performed when the private cards are pressed
     *
     * @param event pressed mouse event
     */
    private void onPrivateObjectivePressed(MouseEvent event) {
        clearNotifyPane(true);
        activateNotifyPane();

        try {
            List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers = controller.getOwnPrivateObjectiveCard();
            List<Pane> privateObjectiveCardViewList = new ArrayList<>();
            privateObjectiveCardWrappers.forEach(privateObjectiveCardWrapper -> {
                PrivateObjectiveCardView cardView =
                        new PrivateObjectiveCardView(privateObjectiveCardWrapper, PRIVATE_OBJECTIVE_CARD_SHOW_SCALE);
                privateObjectiveCardViewList.add(cardView);
            });
            drawCenteredPanes(notifyPane, privateObjectiveCardViewList, CSS_CLASS1);
            drawSimpleCloseHelperBox(notifyPane, "Carte obiettivo private");
        } catch (IOException e) {
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
        }
        event.consume();
    }

    /**
     * Return the list of users. The first element is the one who is playing with this application; the following
     * order follows the turn rotation.
     *
     * @param users list of users ordered by turn in which the first element is not the one who is playing
     *              with this client
     * @return list of users ordered by turn and in which the first element is the one who is playing with this
     * client
     */
    private List<UserWrapper> getUsersOrdered(final List<UserWrapper> users) {
        List<UserWrapper> orderedUsers = new ArrayList<>(users);
        while (!orderedUsers.get(0).getUsername().equals(controller.getUsername())) {
            Collections.rotate(orderedUsers, 1);
        }
        return orderedUsers;
    }
}
