package org.poianitibaldizhou.sagrada.graphics.view.listener;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import edu.emory.mathcs.backport.java.util.Collections;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;
import org.poianitibaldizhou.sagrada.graphics.view.component.*;
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

public class GameListener extends AbstractView implements IGameView, IGameObserver{

    private transient Pane publicObjectiveCardsContainer;
    private transient Pane toolCardsContainer;

    private transient Map<UserWrapper, SchemaCardView> schemaCardViewMap;

    private static final double FRONT_BACK_SCHEMA_CARD_SCALE = 0.3;
    private static final double PRIVATE_OBJECTIVE_CARD_SHOW_SCALE = 0.4;
    private static final double SCHEMA_CARD_SCALE = 0.25;
    private static final double PRIVATE_OBJECTIVE_CARD_SCALE = 0.25;
    private static final double PUBLIC_OBJECTIVE_CARD_SCALE = 0.35;
    private static final double TOOL_CARD_SCALE = 0.35;
    private static final double PUBLIC_OBJECTIVE_CARD_SHOW_SCALE = 0.7;

    private static final double PADDING = 10;

    public GameListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        schemaCardViewMap = new HashMap<>();
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
    public void onPlayersCreate(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<UserWrapper> users = parser.getListOfUserWrapper(message);

        Platform.runLater(()->{
            Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap;
            Map<UserWrapper, Integer> favorTokenMap;
            List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers;
            try {
                privateObjectiveCardWrappers = controller.getOwnPrivateObjectiveCard();
                favorTokenMap = controller.getCoinsMap();
                schemaCardWrapperMap = controller.getSchemaCardMap();
            } catch (IOException e) {
                this.showCrashErrorMessage("Errore di connessione");
                return;
            }

            this.drawUsers(users, schemaCardWrapperMap, privateObjectiveCardWrappers, favorTokenMap);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPublicObjectiveCardsDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<PublicObjectiveCardWrapper> publicObjectiveCardWrappers = parser.getPublicObjectiveCards(message);

        Platform.runLater(()->{
            this.drawPublicObjectiveCards(publicObjectiveCardWrappers);
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
            this.drawToolCards(toolCardWrappers);
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
            clearNotifyPane();
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
        DoubleBinding startX = getCenterX();
        DoubleBinding y = getCenterX().subtract(getWidth().divide(4));

        ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 0; i < frontBackSchemaCardList.size(); i++) {
            FrontBackSchemaCardView frontBackSchemaCardView = getFrontBackSchemaCardView(frontBackSchemaCardList.get(i));
            DoubleBinding x = startX.add(frontBackSchemaCardView.widthProperty().multiply((i == 0) ? -1 : 1));

            frontBackSchemaCardView.translateXProperty()
                    .bind(getPivotX(x, frontBackSchemaCardView.widthProperty(), 0.5));
            frontBackSchemaCardView.translateYProperty()
                    .bind(getPivotY(y, frontBackSchemaCardView.heightProperty(), 0.5));

            JFXRadioButton radioButton = TextureUtils.getRadioButton("",
                    "radio-button-notify-pane", Color.WHITE, Color.DEEPSKYBLUE);
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setUserData(frontBackSchemaCardView);
            radioButton.translateXProperty().bind(x);
            radioButton.translateYProperty().bind(y.add(frontBackSchemaCardView.heightProperty().divide(1.3)));

            notifyPane.getChildren().addAll(frontBackSchemaCardView, radioButton);
        }

        HBox helperBox = showHelperText(notifyPane,
                "Hai ricevuto due window pattern fronte e retro, per girarle premi sulle carte e trascinale");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton continueButton = TextureUtils.getButton("Continua", "positive-button");
        continueButton.setOnAction(event -> {
            if (toggleGroup.getSelectedToggle() == null) {
                showMessage(notifyPane, "Devi scegliere una delle due window pattern", MessageType.ERROR);
                return;
            }
            FrontBackSchemaCardView schemaCardView = (FrontBackSchemaCardView) toggleGroup.getSelectedToggle().getUserData();
            try {
                controller.chooseSchemaCard(schemaCardView.getCurrentSchemaCardWrapper());
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
                    PRIVATE_OBJECTIVE_CARD_SHOW_SCALE);
            DoubleBinding y = startY.add(objectiveCardView.heightProperty().add(PADDING).multiply(i));

            objectiveCardView.translateXProperty().bind(getPivotX(x, objectiveCardView.widthProperty(), 0));
            objectiveCardView.translateYProperty().bind(getPivotY(y, objectiveCardView.heightProperty(), 1));

            notifyPane.getChildren().add(objectiveCardView);
        }
    }

    private void showChoosePrivateObjectiveCards(List<PrivateObjectiveCardWrapper> privateObjectiveCards) {
    }

    private void drawUsers(List<UserWrapper> users, Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap,
                           List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers, Map<UserWrapper, Integer> coinsMap) {
        List<UserWrapper> orderedUsers = getUsersOrdered(users);
        for (int i = 0; i < orderedUsers.size(); i++) {
            final double angle = 2 * Math.PI * i / ((orderedUsers.size() == 3) ? orderedUsers.size() + 1 :
                    orderedUsers.size()) + Math.PI / 2 + ((i == 2 && orderedUsers.size() == 3) ? Math.PI/2 : 0);
            // CALCULATE SCHEMA CARD POSITION
            DoubleBinding distance;
            if(Math.abs(Math.abs(angle) - 2*Math.PI) < 0.0001f || Math.abs(Math.abs(angle) - Math.PI) < 0.0001f)
                distance = getHeight().divide(2).subtract(getHeight().divide(3));
            else
                distance = getHeight().divide(2).subtract(getHeight().divide(1.8)).subtract(PADDING);
            DoubleBinding offsetX = distance.multiply(Math.cos(angle));
            DoubleBinding offsetY = distance.multiply(Math.sin(angle));

            // DRAW SCHEMA CARD
            SchemaCardWrapper schemaCard = schemaCardWrapperMap.get(orderedUsers.get(i));
            SchemaCardView schemaCardView = new SchemaCardView(schemaCard, SCHEMA_CARD_SCALE);
            schemaCardView.setRotate(angle * 180.0 / Math.PI - 90);
            DoubleBinding distanceSchemaCard = getCenterY().subtract(schemaCardView.heightProperty().divide(1.8));
            schemaCardView.translateXProperty().bind(getPivotX(getCenterX().add(offsetX), schemaCardView.widthProperty(), 0.5)
                    .add(distanceSchemaCard.multiply(Math.cos(angle))));
            schemaCardView.translateYProperty().bind(getPivotY(getCenterY().add(offsetY), schemaCardView.heightProperty(), 0.5)
                    .add(distanceSchemaCard.multiply(Math.sin(angle))));
            schemaCardViewMap.putIfAbsent(orderedUsers.get(i), schemaCardView);
            corePane.getChildren().addAll(schemaCardView);

            // CALCULATE PRIVATE OBJECTIVE CARD POSITION
            final double tangentAngle = angle - Math.PI / 2;
            DoubleBinding tangentDistance = schemaCardView.widthProperty().divide(2).add(PADDING);
            DoubleBinding pocX = getCenterX().add(offsetX).add(distanceSchemaCard.multiply(Math.cos(angle)))
                    .add(tangentDistance.multiply(Math.cos(tangentAngle)));
            DoubleBinding pocY = getCenterY().add(offsetY).add(distanceSchemaCard.multiply(Math.sin(angle)))
                    .add(tangentDistance.multiply(Math.sin(tangentAngle)));

            // DRAW PRIVATE OBJECTIVE CARD
            if (i == 0)
                drawPrivateObjectiveCard(privateObjectiveCardWrappers, pocX, pocY, angle);
            else
                drawRetroPrivateObjectiveCard(pocX, pocY, angle);

            try {
                PlayerListener playerListener = drawUser(orderedUsers.get(i), coinsMap.get(orderedUsers.get(i)), new Point2D(Math.round(Math.cos(angle)),
                        Math.round(Math.sin(angle))));
                SchemaCardListener schemaCardListener = new SchemaCardListener(schemaCardView,
                        controller, corePane, notifyPane);
                controller.bindPlayer(orderedUsers.get(i), playerListener, schemaCardListener);
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot initialize SchemaCardListener");
                showCrashErrorMessage("Errore di connessione");
            }
        }
    }

    private PlayerListener drawUser(UserWrapper userWrapper, int favorTokens, Point2D direction) throws RemoteException {
        PlayerView playerView = new PlayerView(userWrapper, favorTokens);
        if(direction.equals(new Point2D(1,0))){
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper);
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.heightProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().add(schemaCardView.widthProperty()).add(PADDING);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(getPivotY(y, playerView.heightProperty(), 0));
        }
        else if(direction.equals(new Point2D(0, 1))){
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper);
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.widthProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().add(schemaCardView.heightProperty()).add(PADDING);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(y);
        }
        else if(direction.equals(new Point2D(-1, 0 ))){
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper);
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.heightProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().subtract(PADDING*2);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(getPivotY(y, playerView.heightProperty(), 0));
        }
        else{
            SchemaCardView schemaCardView = schemaCardViewMap.get(userWrapper);
            DoubleBinding x = schemaCardView.translateXProperty().add(schemaCardView.widthProperty().divide(2));
            DoubleBinding y = schemaCardView.translateYProperty().subtract(PADDING);

            playerView.translateXProperty().bind(getPivotX(x, playerView.widthProperty(), 0.5));
            playerView.translateYProperty().bind(getPivotY(y, playerView.heightProperty(), 0));
        }
        corePane.getChildren().add(playerView);
        return new PlayerListener(playerView, controller, corePane, notifyPane);
    }

    private void drawPublicObjectiveCards(List<PublicObjectiveCardWrapper> publicObjectiveCardWrappers) {

        publicObjectiveCardsContainer = new Pane();

        DoubleBinding x = new SimpleDoubleProperty(0).add(PADDING);
        DoubleBinding y = new SimpleDoubleProperty(0).add(PADDING);

        publicObjectiveCardsContainer.translateXProperty().bind(x.add(toolCardsContainer.widthProperty().add(PADDING*4)));
        publicObjectiveCardsContainer.translateYProperty().bind(y);

        for (int i = 0; i < publicObjectiveCardWrappers.size(); i++) {
            PublicObjectiveCardView publicObjectiveCardView = new PublicObjectiveCardView(
                    publicObjectiveCardWrappers.get(i), PUBLIC_OBJECTIVE_CARD_SCALE);
            publicObjectiveCardView.setTranslateX(i*PADDING);
            publicObjectiveCardView.setTranslateY(i*PADDING);

            publicObjectiveCardsContainer.getChildren().add(publicObjectiveCardView);
        }

        publicObjectiveCardsContainer.setOnMousePressed(this::onPublicObjectiveCardsPressed);
        publicObjectiveCardsContainer.getStyleClass().add("on-board-card");

        corePane.getChildren().add(publicObjectiveCardsContainer);
    }


    private void onPublicObjectiveCardsPressed(MouseEvent event){
        clearNotifyPane();
        activateNotifyPane();

        try {
            List<PublicObjectiveCardWrapper> publicObjectiveCardList = controller.getPublicObjectiveCards();
            DoubleBinding y = getCenterY();

            for (int i = 0; i < publicObjectiveCardList.size(); i++) {
                PublicObjectiveCardView publicObjectiveCardView =
                        new PublicObjectiveCardView(publicObjectiveCardList.get(i), PUBLIC_OBJECTIVE_CARD_SHOW_SCALE);
                DoubleBinding padding = publicObjectiveCardView.widthProperty().divide(2);
                DoubleBinding totalWidth = publicObjectiveCardView.widthProperty().multiply(publicObjectiveCardList.size())
                        .add(padding.multiply(publicObjectiveCardList.size() - 1));
                DoubleBinding x = getCenterX().subtract(totalWidth.divide(2))
                        .add(publicObjectiveCardView.widthProperty().multiply(i)).add(padding.multiply(i));

                publicObjectiveCardView.translateXProperty().bind(getPivotX(x, publicObjectiveCardView.widthProperty(), 1));
                publicObjectiveCardView.translateYProperty().bind(getPivotY(y, publicObjectiveCardView.heightProperty(), 0.5));
                publicObjectiveCardView.getStyleClass().add("on-notify-pane-card");
                notifyPane.getChildren().add(publicObjectiveCardView);
            }
            drawSimpleCloseHelperBox(notifyPane);

        } catch (IOException e) {
            showCrashErrorMessage("Errore di connessione");
        }
    }

    private void drawToolCards(List<ToolCardWrapper> toolCardWrappers) {

        toolCardsContainer = new Pane();

        DoubleBinding x = new SimpleDoubleProperty(0).add(PADDING);
        DoubleBinding y = new SimpleDoubleProperty(0).add(PADDING);

        toolCardsContainer.translateXProperty().bind(x);
        toolCardsContainer.translateYProperty().bind(y);

        for (int i = 0; i < toolCardWrappers.size(); i++) {
            ToolCardView toolCardView = new ToolCardView(toolCardWrappers.get(i), TOOL_CARD_SCALE);
            toolCardView.setTranslateX(i*PADDING);
            toolCardView.setTranslateY(i*PADDING);
            try {
                ToolCardListener toolCardListener = new ToolCardListener(toolCardView, controller, corePane, notifyPane);
                controller.bindToolCard(toolCardWrappers.get(i), toolCardListener);
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
        controller.getToolCards();
    }

    private void drawPrivateObjectiveCard(List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers,
                                          DoubleBinding x, DoubleBinding y, double angle) {
        for (int i = 0; i < privateObjectiveCardWrappers.size(); i++) {
            PrivateObjectiveCardView cardView = new PrivateObjectiveCardView(
                    privateObjectiveCardWrappers.get(i), PRIVATE_OBJECTIVE_CARD_SCALE);
            setupPrivateObjectiveCard(cardView, x, y, angle);
        }

    }

    private void drawRetroPrivateObjectiveCard(DoubleBinding x, DoubleBinding y, double angle) {
        PrivateObjectiveCardView cardView = new PrivateObjectiveCardView(PRIVATE_OBJECTIVE_CARD_SCALE);
        setupPrivateObjectiveCard(cardView, x, y, angle);
    }

    private void drawSimpleCloseHelperBox(Pane pane){
        HBox helperBox = showHelperText(pane, "Carte obiettivo pubbliche");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton cancelButton = TextureUtils.getButton("Chiudi", "negative-button");
        cancelButton.setOnAction(event -> {
            clearNotifyPane();
            deactivateNotifyPane();
        });

        helperBox.getChildren().addAll(spacer, cancelButton);
    }

    private void setupPrivateObjectiveCard(PrivateObjectiveCardView cardView, DoubleBinding x,
                                           DoubleBinding y, double angle){
        if(Math.abs(Math.abs(angle) - 2*Math.PI) < 0.0001f || Math.abs(Math.abs(angle) - Math.PI) < 0.0001f) {
            DoubleBinding cardY = y.add(cardView.widthProperty().divide(2).multiply(Math.sin(angle - 90)));
            cardView.translateXProperty().bind(getPivotX(x, cardView.widthProperty(), 0.5));
            cardView.translateYProperty().bind(getPivotY(cardY, cardView.heightProperty(), 0.5));
        }
        else{
            DoubleBinding cardX = x.add(cardView.widthProperty().divide(2).multiply(Math.cos(angle - 90)));
            cardView.translateXProperty().bind(getPivotX(cardX, cardView.widthProperty(), 0.5));
            cardView.translateYProperty().bind(getPivotY(y, cardView.heightProperty(), 0.5));
        }
        cardView.setRotate(angle * 180.0 / Math.PI - 90);
        corePane.getChildren().add(cardView);
    }

    private List<UserWrapper> getUsersOrdered(final List<UserWrapper> users) {
        List<UserWrapper> orderedUsers = new ArrayList<>(users);
        while (!orderedUsers.get(0).getUsername().equals(controller.getUsername())) {
            Collections.rotate(orderedUsers, 1);
        }
        return orderedUsers;
    }

    private FrontBackSchemaCardView getFrontBackSchemaCardView(FrontBackSchemaCardWrapper frontBackSchemaCard) {
        return new FrontBackSchemaCardView(frontBackSchemaCard, FRONT_BACK_SCHEMA_CARD_SCALE);
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
