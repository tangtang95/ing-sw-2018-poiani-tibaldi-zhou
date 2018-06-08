package org.poianitibaldizhou.sagrada.graphics.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import edu.emory.mathcs.backport.java.util.Collections;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.FrontBackSchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PrivateObjectiveCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameView {

    private final MultiPlayerController controller;
    private final Pane corePane;
    private final Pane notifyPane;

    private static final double FRONT_BACK_SCHEMA_CARD_SCALE = 0.3;
    private static final double PRIVATE_OBJECTIVE_CARD_SHOW_SCALE = 0.4;
    private static final double SCHEMA_CARD_SCALE = 0.25;
    private static final double PRIVATE_OBJECTIVE_CARD_SCALE = 0.2;

    private static final double HELPER_BAR_PERCENT_HEIGHT = 0.075;

    private static final double PADDING = 10;

    public GameView(MultiPlayerController controller, Pane corePane, Pane notifyPane) {
        this.controller = controller;
        this.corePane = corePane;
        this.notifyPane = notifyPane;
    }

    public void showFrontBackSchemaCards(List<FrontBackSchemaCardWrapper> frontBackSchemaCardList) {
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

        HBox helperBox = showHelperText("Hai ricevuto due window pattern fronte e retro, per girarle premi sulle carte e trascinale");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton continueButton = TextureUtils.getButton("Continua", "positive-button");
        continueButton.setOnAction(event -> {
            if(toggleGroup.getSelectedToggle() == null){
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

    public void showPrivateObjectiveCards(List<PrivateObjectiveCardWrapper> privateObjectiveCards) {

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

    public void drawUsers(List<UserWrapper> users, Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap,
                          List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers) {
        List<UserWrapper> orderedUsers = getUsersOrdered(users);
        for (int i = 0; i < orderedUsers.size(); i++) {
            double angle = 2*Math.PI*i / ((orderedUsers.size() == 3) ? orderedUsers.size() + 1 :
                    orderedUsers.size()) - Math.PI/2;
            DoubleBinding distance = getCenterY().subtract(getHeight().divide(1.8));
            DoubleBinding offsetX = distance.multiply(Math.cos(angle));
            DoubleBinding offsetY = distance.multiply(Math.sin(angle));

            SchemaCardWrapper schemaCard = schemaCardWrapperMap.get(orderedUsers.get(i));
            SchemaCardView schemaCardView = new SchemaCardView(schemaCard, SCHEMA_CARD_SCALE);
            schemaCardView.setRotate(angle*180.0/Math.PI - 90);
            DoubleBinding distanceSchemaCard = getCenterY().subtract(schemaCardView.heightProperty().divide(1.8));

            schemaCardView.translateXProperty().bind(getPivotX(getCenterX().add(offsetX), schemaCardView.widthProperty(), 0.5)
                    .add(distanceSchemaCard.multiply(Math.cos(angle))));
            schemaCardView.translateYProperty().bind(getPivotY(getCenterY().add(offsetY), schemaCardView.heightProperty(), 0.5)
                    .add(distanceSchemaCard.multiply(Math.sin(angle))));
            corePane.getChildren().addAll(schemaCardView);

            double tangentAngle = angle - Math.PI/2;
            DoubleBinding tangentDistance = schemaCardView.widthProperty().divide(2).add(PADDING);
            DoubleBinding pocX = getCenterX().add(offsetX.add(tangentDistance.multiply(Math.cos(tangentAngle))));
            DoubleBinding pocY = getCenterY().add(offsetY.add(tangentDistance.multiply(Math.sin(tangentAngle))));

            if(i == 0)
                drawPrivateObjectiveCard(privateObjectiveCardWrappers, pocX, pocY, angle);
            else
                drawRetroPrivateObjectiveCard(pocX, pocY, angle);

        }
    }

    public void drawPrivateObjectiveCard(List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers,
                                         DoubleBinding x, DoubleBinding y, double angle){
        for (int i = 0; i < privateObjectiveCardWrappers.size(); i++) {
            PrivateObjectiveCardView cardView = new PrivateObjectiveCardView(
                    privateObjectiveCardWrappers.get(i), PRIVATE_OBJECTIVE_CARD_SCALE);
            cardView.translateXProperty().bind(x);
            cardView.translateYProperty().bind(y);
            cardView.setRotate(angle*180.0/Math.PI + 90);
            corePane.getChildren().add(cardView);
        }

    }

    public void drawRetroPrivateObjectiveCard(DoubleBinding x, DoubleBinding y, double angle){
        PrivateObjectiveCardView cardView = new PrivateObjectiveCardView(PRIVATE_OBJECTIVE_CARD_SCALE);
        cardView.translateXProperty().bind(x);
        cardView.translateYProperty().bind(y);
        cardView.setRotate(angle*180.0/Math.PI + 90);
        corePane.getChildren().add(cardView);
    }

    private List<UserWrapper> getUsersOrdered(final List<UserWrapper> users){
        List<UserWrapper> orderedUsers = new ArrayList<>(users);
        while(orderedUsers.get(0).getUsername().equals(controller.getUsername())) {
            Collections.rotate(orderedUsers, 1);
        }
        return orderedUsers;
    }

    private void showMessage(Pane pane, String text, MessageType messageType){
        Label label = new Label(text);
        if(messageType == MessageType.ERROR)
            label.setTextFill(Color.ORANGERED);
        else
            label.setTextFill(Color.DEEPSKYBLUE);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), label);
        fadeTransition.setFromValue(0.3);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount((messageType == MessageType.ERROR) ? 6 : Animation.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
        fadeTransition.setOnFinished(event -> pane.getChildren().remove(label));

        label.translateXProperty().bind(getCenterX().subtract(label.widthProperty().divide(2)));
        label.translateYProperty().bind(getCenterY().add(getCenterX().divide(2.5)));

        pane.getChildren().add(label);
    }

    public HBox showHelperText(String text){
        HBox helperPane = new HBox(10);
        helperPane.setAlignment(Pos.CENTER_LEFT);
        helperPane.setPadding(new Insets(0,10,0, 10));
        helperPane.setStyle("-fx-background-color: black");
        helperPane.prefWidthProperty().bind(notifyPane.widthProperty());
        helperPane.prefHeightProperty().bind(notifyPane.heightProperty().multiply(HELPER_BAR_PERCENT_HEIGHT));

        Label textLabel = new Label(text);
        textLabel.setTextFill(Color.WHITE);
        helperPane.getChildren().add(textLabel);

        notifyPane.getChildren().add(helperPane);
        helperPane.setTranslateX(0);
        helperPane.translateYProperty().bind(notifyPane.prefHeightProperty().subtract(helperPane.prefHeightProperty()));
        return helperPane;
    }

    public void clearNotifyPane(){
        notifyPane.getChildren().clear();
        notifyPane.getChildren().add(getBackgroundPane());
    }

    public void activateNotifyPane(){
        notifyPane.toFront();
        notifyPane.setVisible(true);
    }

    public void deactivateNotifyPane(){
        notifyPane.toBack();
        notifyPane.setVisible(false);
    }

    private Pane getBackgroundPane() {
        Pane backgroundPane = new Pane();
        backgroundPane.setOpacity(0.6);
        backgroundPane.setStyle("-fx-background-color: black");
        backgroundPane.prefWidthProperty().bind(notifyPane.widthProperty());
        backgroundPane.prefHeightProperty().bind(notifyPane.heightProperty());
        return backgroundPane;
    }

    private DoubleBinding getWidth() {
        return notifyPane.widthProperty().divide(1);
    }

    private DoubleBinding getHeight() {
        return notifyPane.heightProperty().divide(1);
    }

    private DoubleBinding getCenterX() {
        return notifyPane.widthProperty().divide(2);
    }

    private DoubleBinding getCenterY() {
        return notifyPane.heightProperty().divide(2);
    }

    private DoubleBinding getPivotX(DoubleBinding x, DoubleBinding width, double pivotX){
        return x.subtract(width.multiply(1 - pivotX));
    }

    private DoubleBinding getPivotX(DoubleBinding x, ReadOnlyDoubleProperty width, double pivotX){
        return x.subtract(width.multiply(1 - pivotX));
    }

    private DoubleBinding getPivotY(DoubleBinding y, DoubleBinding height, double pivotY){
        return y.subtract(height.multiply(1 - pivotY));
    }

    private DoubleBinding getPivotY(DoubleBinding y, ReadOnlyDoubleProperty height, double pivotY){
        return y.subtract(height.multiply( 1 - pivotY));
    }

    private FrontBackSchemaCardView getFrontBackSchemaCardView(FrontBackSchemaCardWrapper frontBackSchemaCard) {
        return new FrontBackSchemaCardView(frontBackSchemaCard, FRONT_BACK_SCHEMA_CARD_SCALE);
    }

    public MultiPlayerController getController() {
        return controller;
    }

    public void showSevereErrorMessage(String text) {
        // TODO
    }
}
