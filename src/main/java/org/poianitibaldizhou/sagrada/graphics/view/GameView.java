package org.poianitibaldizhou.sagrada.graphics.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.FadeTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
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

import java.io.IOException;
import java.util.List;

public class GameView {

    private final MultiPlayerController controller;
    private final Pane corePane;
    private final Pane notifyPane;

    private static final double FRONT_BACK_SCHEMA_CARD_SCALE = 0.3;

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
            radioButton.setUserData(frontBackSchemaCardView.getCurrentSchemaCardWrapper());
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
                showErrorMessage(notifyPane, "Devi scegliere una delle due window pattern");
                return;
            }
            SchemaCardWrapper schemaCardWrapper = (SchemaCardWrapper) toggleGroup.getSelectedToggle().getUserData();
            try {
                controller.chooseSchemaCard(schemaCardWrapper);
            } catch (IOException e) {
                showErrorMessage(notifyPane, "Errore di connessione");
            }
        });

        helperBox.getChildren().addAll(spacer, continueButton);
    }

    public void showPrivateObjectiveCards(List<PrivateObjectiveCardWrapper> privateObjectiveCards) {

        DoubleBinding x = getWidth().subtract(PADDING);
        DoubleBinding y = getCenterX().subtract(getWidth().divide(4));

        for (int i = 0; i < privateObjectiveCards.size(); i++) {
            
        }
    }

    private void showErrorMessage(Pane pane, String text){
        Label errorLabel = new Label(text);
        errorLabel.setTextFill(Color.ORANGERED);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), errorLabel);
        fadeTransition.setFromValue(0.3);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(6);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
        fadeTransition.setOnFinished(event -> pane.getChildren().remove(errorLabel));

        errorLabel.translateXProperty().bind(getCenterX().subtract(errorLabel.widthProperty().divide(2)));
        errorLabel.translateYProperty().bind(getCenterY().add(getCenterX().divide(2.5)));

        pane.getChildren().add(errorLabel);
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
        return x.subtract(width.multiply(pivotX));
    }

    private DoubleBinding getPivotX(DoubleBinding x, ReadOnlyDoubleProperty width, double pivotX){
        return x.subtract(width.multiply(pivotX));
    }

    private DoubleBinding getPivotY(DoubleBinding y, DoubleBinding height, double pivotY){
        return y.subtract(height.multiply(pivotY));
    }

    private DoubleBinding getPivotY(DoubleBinding y, ReadOnlyDoubleProperty height, double pivotY){
        return y.subtract(height.multiply(pivotY));
    }

    private FrontBackSchemaCardView getFrontBackSchemaCardView(FrontBackSchemaCardWrapper frontBackSchemaCard) {
        return new FrontBackSchemaCardView(frontBackSchemaCard, FRONT_BACK_SCHEMA_CARD_SCALE);
    }
}
