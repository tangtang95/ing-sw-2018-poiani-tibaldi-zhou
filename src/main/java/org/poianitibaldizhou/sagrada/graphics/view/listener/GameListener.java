package org.poianitibaldizhou.sagrada.graphics.view.listener;

import com.jfoenix.controls.JFXButton;
import edu.emory.mathcs.backport.java.util.Collections;
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
import org.poianitibaldizhou.sagrada.graphics.controller.GameController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;
import org.poianitibaldizhou.sagrada.graphics.view.component.*;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameListener extends AbstractView implements IGameView, IGameObserver {

    private transient Pane publicObjectiveCardsContainer;
    private transient Pane toolCardsContainer;

    private transient Map<UserWrapper, SchemaCardListener> schemaCardViewMap;
    private transient List<ToolCardListener> toolCardListeners;

    private static final double FRONT_BACK_SCHEMA_CARD_SCALE = 0.3;
    private static final double PRIVATE_OBJECTIVE_CARD_SHOW_SCALE_AT_FIRST = 0.6;
    private static final double PUBLIC_OBJECTIVE_CARD_SHOW_SCALE = 0.65;
    private static final double PRIVATE_OBJECTIVE_CARD_SHOW_SCALE = 0.65;
    private static final double TOOL_CARD_SHOW_SCALE = 0.65;

    private static final double PADDING = 10;

    public GameListener(GameController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        schemaCardViewMap = new HashMap<>();
        toolCardListeners = new ArrayList<>();
    }

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
        Platform.runLater(() -> {
            showMessage(getActivePane(), err, MessageType.ERROR);
        });
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
            } catch (IOException e) {
                this.showCrashErrorMessage("Errore di connessione");
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
            publicObjectiveCardsContainer = drawPublicObjectiveCardsView(corePane, publicObjectiveCardWrappers,
                    controller.getGameViewStrategy().getPublicObjectiveCardScale());
            publicObjectiveCardsContainer.setOnMousePressed(this::onPublicObjectiveCardsPressed);
            controller.setRoundTrack();
            controller.setDraftPool();
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
            this.drawToolCards(toolCardWrappers, controller.getGameViewStrategy().getToolCardScale());
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
        Platform.runLater(() -> {
            this.showPrivateObjectiveCards(privateObjectiveCards);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSchemaCardsDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<FrontBackSchemaCardWrapper> frontBackSchemaCards = parser.getFrontBackSchemaCards(message);

        Platform.runLater(() -> {
            this.showFrontBackSchemaCards(frontBackSchemaCards);
        });
    }

    private void showFrontBackSchemaCards(List<FrontBackSchemaCardWrapper> frontBackSchemaCardList) {

        List<Pane> frontBackSchemaCardViewList = new ArrayList<>();
        frontBackSchemaCardList.forEach(frontBackSchemaCard -> {
            FrontBackSchemaCardView frontBackSchemaCardView = new FrontBackSchemaCardView(frontBackSchemaCard,
                    FRONT_BACK_SCHEMA_CARD_SCALE);
            frontBackSchemaCardViewList.add(frontBackSchemaCardView);
        });

        drawCenteredPanes(notifyPane, frontBackSchemaCardViewList, "on-notify-pane-card");
        ToggleGroup toggleGroup = new ToggleGroup();
        drawRadioButtons(toggleGroup, frontBackSchemaCardViewList);
        HBox helperBox = showHelperText(notifyPane,
                "Hai ricevuto due window pattern fronte e retro, per girarle premi sulle carte e trascinale");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
        continueButton.setOnAction(event -> {
            if (toggleGroup.getSelectedToggle() == null) {
                showMessage(notifyPane, "Devi scegliere una delle due Carte Schema fronte o retro", MessageType.ERROR);
                return;
            }
            FrontBackSchemaCardView schemaCardView = (FrontBackSchemaCardView) toggleGroup.getSelectedToggle().getUserData();
            try {
                controller.chooseSchemaCard(schemaCardView.getCurrentSchemaCardWrapper());
                toggleGroup.getToggles().forEach(toggle -> ((RadioButton)toggle).setDisable(true));
                ((Button) event.getSource()).setDisable(true);
            } catch (IOException e) {
                showMessage(notifyPane, "Errore di connessione", MessageType.ERROR);
                return;
            }
            showMessage(notifyPane, "In attesa degli altri giocatori", MessageType.INFO);
        });

        helperBox.getChildren().addAll(spacer, continueButton);
    }

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

    private void showChoosePrivateObjectiveCards(List<PrivateObjectiveCardWrapper> privateObjectiveCards) {
        List<Pane> privateObjectiveCardViews = new ArrayList<>();
        privateObjectiveCards.forEach(privateObjectiveCardWrapper ->
                privateObjectiveCardViews.add(new PrivateObjectiveCardView(privateObjectiveCardWrapper,
                        PRIVATE_OBJECTIVE_CARD_SHOW_SCALE)));
        drawCenteredPanes(notifyPane, privateObjectiveCardViews, "on-notify-pane-card");
        ToggleGroup toggleGroup = new ToggleGroup();
        drawRadioButtons(toggleGroup, privateObjectiveCardViews);
        HBox helperBox = showHelperText(notifyPane,
                "Prima del calcolo dei punti scegli una delle Carte Private che desideri");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
        continueButton.setOnAction(event -> {
            if (toggleGroup.getSelectedToggle() == null) {
                showMessage(notifyPane, "Devi scegliere una delle Carte Private", MessageType.ERROR);
                return;
            }
            PrivateObjectiveCardView privateObjectiveCardView = (PrivateObjectiveCardView) toggleGroup
                    .getSelectedToggle().getUserData();
            try {
                controller.choosePrivateObjectiveCard(privateObjectiveCardView.getPrivateObjectiveCardWrapper());
                toggleGroup.getToggles().forEach(toggle -> ((RadioButton)toggle).setDisable(true));
                ((Button) event.getSource()).setDisable(true);
            } catch (IOException e) {
                showMessage(notifyPane, "Errore di connessione", MessageType.ERROR);
                e.printStackTrace();
            }
        });

        helperBox.getChildren().addAll(spacer, continueButton);
    }

    private void drawUsers(List<UserWrapper> users, Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap,
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
            DoubleBinding tangentDistance = schemaCardView.widthProperty().divide(2).add(PADDING*2);
            DoubleBinding pocX = schemaCardView.translateXProperty().add(schemaCardView.widthProperty().divide(2))
                    .add(tangentDistance.multiply(Math.cos(tangentAngle)));
            DoubleBinding pocY = schemaCardView.translateYProperty().add(schemaCardView.heightProperty().divide(2))
                    .add(tangentDistance.multiply(Math.sin(tangentAngle)));

            // DRAW PRIVATE OBJECTIVE CARD
            if (i == 0)
                drawPrivateObjectiveCard(privateObjectiveCardWrappers, pocX, pocY);
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
                controller.bindPlayer(orderedUsers.get(i), playerListener, schemaCardListener);
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot initialize SchemaCardListener");
                showCrashErrorMessage("Errore di connessione");
            }
        }
    }

    private PlayerView drawUser(UserWrapper userWrapper, int favorTokens, Point2D direction) throws RemoteException {
        PlayerView playerView;
        if(favorTokens >= 0)
            playerView = new PlayerView(userWrapper, favorTokens);
        else
            playerView = new PlayerView(userWrapper, 10.0);
        if (direction.equals(new Point2D(1, 0))) {
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper).getView();
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.heightProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().add(schemaCardView.widthProperty()).add(PADDING);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(getPivotY(y, playerView.heightProperty(), 0));
        } else if (direction.equals(new Point2D(0, 1))) {
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper).getView();
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.widthProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().add(schemaCardView.heightProperty()).add(PADDING);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(y);
        } else if (direction.equals(new Point2D(-1, 0))) {
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper).getView();
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.heightProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().subtract(PADDING * 2);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(getPivotY(y, playerView.heightProperty(), 0));
        } else {
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper).getView();
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.widthProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().subtract(PADDING);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(getPivotY(y, playerView.heightProperty(), 0));
        }
        corePane.getChildren().add(playerView);
        return playerView;
    }

    public Pane drawPublicObjectiveCardsView(Pane corePane, List<PublicObjectiveCardWrapper> publicObjectiveCardWrappers, double scale) {
        Pane container = new Pane();
        container.getStyleClass().add("on-board-card");


        DoubleBinding y = new SimpleDoubleProperty(PADDING)
                .add(corePane.heightProperty().multiply(AbstractView.HELPER_BAR_PERCENT_HEIGHT));

        for (int i = 0; i < publicObjectiveCardWrappers.size(); i++) {
            PublicObjectiveCardView publicObjectiveCardView = new PublicObjectiveCardView(
                    publicObjectiveCardWrappers.get(i), scale);
            publicObjectiveCardView.setTranslateX((publicObjectiveCardWrappers.size()-i-1)*PADDING);
            publicObjectiveCardView.setTranslateY(i*PADDING);

            container.getChildren().add(publicObjectiveCardView);
        }

        container.translateXProperty().bind(corePane.widthProperty()
                .subtract(PADDING*(publicObjectiveCardWrappers.size()+1))
                .subtract(container.widthProperty()));
        container.translateYProperty().bind(y);
        corePane.getChildren().add(container);

        return container;
    }


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
            drawCenteredPanes(notifyPane, publicObjectiveCardViews, "on-notify-pane-card");
            drawSimpleCloseHelperBox(notifyPane, "Carte obiettivo pubbliche");

        } catch (IOException e) {
            showCrashErrorMessage("Errore di connessione");
        }
        event.consume();
    }

    private void drawToolCards(List<ToolCardWrapper> toolCardWrappers, double scale) {

        toolCardsContainer = new Pane();

        DoubleBinding x = new SimpleDoubleProperty(0).add(PADDING*2);
        DoubleBinding y = new SimpleDoubleProperty(0).add(corePane.heightProperty()
                .multiply(AbstractView.HELPER_BAR_PERCENT_HEIGHT).add(PADDING));

        toolCardsContainer.translateXProperty().bind(x);
        toolCardsContainer.translateYProperty().bind(y);

        for (int i = 0; i < toolCardWrappers.size(); i++) {
            ToolCardView toolCardView = new ToolCardView(toolCardWrappers.get(i), scale);
            toolCardView.setTranslateX(i * PADDING);
            toolCardView.setTranslateY(i * PADDING);
            try {
                ToolCardListener toolCardListener = new ToolCardListener(toolCardView, controller, corePane, notifyPane);
                controller.bindToolCard(toolCardWrappers.get(i), toolCardListener);
                toolCardListeners.add(toolCardListener);
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                showCrashErrorMessage("Errore di connessione");
            }
            toolCardsContainer.getChildren().add(toolCardView);
        }

        toolCardsContainer.setOnMousePressed(this::onToolCardsPressed);
        toolCardsContainer.getStyleClass().add("on-board-card");

        corePane.getChildren().add(toolCardsContainer);

    }

    private void onToolCardsPressed(MouseEvent event) {
        clearNotifyPane(true);
        activateNotifyPane();

        try {
            List<ToolCardWrapper> toolCardList = controller.getToolCards();
            if(!toolCardList.isEmpty()) {
                List<Pane> toolCardViews = new ArrayList<>();
                toolCardList.forEach(toolCardWrapper -> {
                    Pane cardView = new ToolCardView(toolCardWrapper, TOOL_CARD_SHOW_SCALE);
                    toolCardViews.add(cardView);
                });
                drawCenteredPanes(notifyPane, toolCardViews, "on-notify-pane-card");
                drawSimpleCloseHelperBox(notifyPane, "Carte utensili");
            }
        } catch (IOException e) {
            showCrashErrorMessage("Errore di connessione");
        }
        event.consume();
    }

    public void destroyToolCard(ToolCardListener toolCardListener) {
        toolCardsContainer.getChildren().remove(toolCardListener.getToolCardView());
        toolCardListeners.remove(toolCardListener);
        if(toolCardListeners.isEmpty())
            corePane.getChildren().remove(toolCardsContainer);
    }

    private void drawPrivateObjectiveCard(List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers,
                                          DoubleBinding x, DoubleBinding y) {
        Pane container = new Pane();
        for (int i = 0; i < privateObjectiveCardWrappers.size(); i++) {
            PrivateObjectiveCardView cardView = new PrivateObjectiveCardView(
                    privateObjectiveCardWrappers.get(i), controller.getGameViewStrategy().getPrivateObjectiveCardScale());
            cardView.setTranslateX(PADDING*i);
            cardView.setTranslateY(PADDING*i);
            container.getChildren().add(cardView);
        }
        DoubleBinding cardX = x.add(container.widthProperty().divide(2));
        container.translateXProperty().bind(getPivotX(cardX, container.widthProperty(), 0.5));
        container.translateYProperty().bind(getPivotY(y, container.heightProperty(), 0.5));
        container.getStyleClass().add("on-board-card");
        container.setOnMousePressed(this::onPrivateObjectivePressed);
        corePane.getChildren().add(container);
    }

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
        corePane.getChildren().add(cardView);
    }

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
            drawCenteredPanes(notifyPane, privateObjectiveCardViewList, "on-notify-pane-card");
            drawSimpleCloseHelperBox(notifyPane, "Carte obiettivo private");
        } catch (IOException e) {
            showCrashErrorMessage("Errore di connessione");
        }
        event.consume();
    }

    private List<UserWrapper> getUsersOrdered(final List<UserWrapper> users) {
        List<UserWrapper> orderedUsers = new ArrayList<>(users);
        while (!orderedUsers.get(0).getUsername().equals(controller.getUsername())) {
            Collections.rotate(orderedUsers, 1);
        }
        return orderedUsers;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GameListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
