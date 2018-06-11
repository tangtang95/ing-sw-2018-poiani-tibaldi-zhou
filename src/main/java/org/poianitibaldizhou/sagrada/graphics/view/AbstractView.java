package org.poianitibaldizhou.sagrada.graphics.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public abstract class AbstractView extends UnicastRemoteObject {

    protected final transient MultiPlayerController controller;
    protected final transient Pane corePane;
    protected final transient Pane notifyPane;

    private static final double HELPER_BAR_PERCENT_HEIGHT = 0.05;

    protected static final double PADDING = 10;

    protected AbstractView(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super();
        this.controller = controller;
        this.corePane = corePane;
        this.notifyPane = notifyPane;
    }

    //GETTER
    public MultiPlayerController getController() {
        return controller;
    }


    protected void showMessage(Pane pane, String text, MessageType messageType) {
        Label label = new Label(text);
        if (messageType == MessageType.ERROR)
            label.setTextFill(Color.ORANGERED);
        else
            label.setTextFill(Color.DEEPSKYBLUE);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), label);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(6);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();

        label.setTranslateX(getSceneWidth()/2);
        label.setTranslateY(getSceneHeight() - PADDING*5);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), label);
        translateTransition.setByY(PADDING*4);
        translateTransition.play();
        translateTransition.setOnFinished(event -> pane.getChildren().remove(label));

        pane.getChildren().add(label);
    }

    protected HBox showHelperText(Pane pane, String text) {
        HBox helperPane = new HBox(10);
        helperPane.setAlignment(Pos.CENTER_LEFT);
        helperPane.setPadding(new Insets(0, 10, 0, 10));
        helperPane.setStyle("-fx-background-color: black");
        helperPane.prefWidthProperty().bind(pane.widthProperty());
        helperPane.prefHeightProperty().bind(pane.heightProperty().multiply(HELPER_BAR_PERCENT_HEIGHT));

        Label textLabel = new Label(text);
        textLabel.setTextFill(Color.WHITE);
        helperPane.getChildren().add(textLabel);

        pane.getChildren().add(helperPane);
        helperPane.setTranslateX(0);
        helperPane.translateYProperty().bind(pane.heightProperty().subtract(helperPane.heightProperty()));
        return helperPane;
    }

    protected HBox showTopBarText(Pane pane, String text) {
        HBox topBarBox = new HBox(10);
        topBarBox.setAlignment(Pos.CENTER_LEFT);
        topBarBox.setPadding(new Insets(0, 10, 0, 10));
        topBarBox.setStyle("-fx-background-color: black");
        topBarBox.prefWidthProperty().bind(pane.widthProperty());
        topBarBox.prefHeightProperty().bind(pane.heightProperty().multiply(HELPER_BAR_PERCENT_HEIGHT));

        Label textLabel = new Label(text);
        textLabel.setTextFill(Color.WHITE);
        topBarBox.getChildren().add(textLabel);

        pane.getChildren().add(topBarBox);
        topBarBox.setTranslateX(0);
        topBarBox.setTranslateY(0);
        return topBarBox;
    }

    protected void showCrashErrorMessage(String text) {
        // TODO
    }

    protected void clearNotifyPane(boolean isFastCloseable) {
        notifyPane.getChildren().clear();
        notifyPane.getChildren().add(getBackgroundPane(isFastCloseable));
    }

    protected void activateNotifyPane() {
        notifyPane.toFront();
        notifyPane.setVisible(true);
    }

    protected void deactivateNotifyPane() {
        notifyPane.toBack();
        notifyPane.setVisible(false);
    }

    protected Pane getBackgroundPane(boolean isFastCloseable) {
        Pane backgroundPane = new Pane();
        backgroundPane.setOpacity(0.6);
        backgroundPane.setStyle("-fx-background-color: black");
        backgroundPane.prefWidthProperty().bind(notifyPane.widthProperty());
        backgroundPane.prefHeightProperty().bind(notifyPane.heightProperty());
        if(isFastCloseable)
            backgroundPane.setOnMousePressed(event -> {
                clearNotifyPane(false);
                deactivateNotifyPane();
            });
        return backgroundPane;
    }

    protected Pane getActivePane() {
        if (notifyPane.isVisible())
            return notifyPane;
        else
            return corePane;
    }

    protected void drawCenteredPanes(@NotNull Pane targetPane, @NotNull List<Pane> panes, String classCSS) {
        DoubleBinding y = getCenterY();
        drawCenteredPanes(targetPane, panes, classCSS, getPivotY(y, panes.get(0).heightProperty(), 0.5));
    }

    protected void drawCenteredPanes(Pane targetPane, List<Pane> panes, String classCSS, DoubleBinding y) {

        for (int i = 0; i < panes.size(); i++) {
            DoubleBinding padding = panes.get(i).widthProperty().divide(2);
            DoubleBinding totalWidth = panes.get(i).widthProperty().multiply(panes.size())
                    .add(padding.multiply(panes.size() - 1));
            DoubleBinding x = getCenterX().subtract(totalWidth.divide(2))
                    .add(panes.get(i).widthProperty().multiply(i)).add(padding.multiply(i));

            panes.get(i).translateXProperty().bind(getPivotX(x, panes.get(i).widthProperty(), 1));
            panes.get(i).translateYProperty().bind(y);
            panes.get(i).setOnMousePressed(Event::consume);
            if(!classCSS.isEmpty())
                panes.get(i).getStyleClass().add(classCSS);
            targetPane.getChildren().add(panes.get(i));
        }
    }

    protected void drawCenteredPane(Pane targetPane, Pane pane, String classCSS) {
        DoubleBinding x = getCenterX();
        DoubleBinding y = getCenterY();

        pane.translateXProperty().bind(getPivotX(x, pane.widthProperty(), 0.5));
        pane.translateYProperty().bind(getPivotY(y, pane.heightProperty(), 0.5));
        pane.setOnMousePressed(Event::consume);
        if(!classCSS.isEmpty())
            pane.getStyleClass().add(classCSS);
        targetPane.getChildren().add(pane);
    }

    protected void drawPane(Pane targetPane, Pane pane, String classCSS, DoubleBinding x, DoubleBinding y) {
        pane.translateXProperty().bind(x);
        pane.translateYProperty().bind(y);
        pane.setOnMousePressed(Event::consume);
        if(!classCSS.isEmpty())
            pane.getStyleClass().add(classCSS);
        targetPane.getChildren().add(pane);
    }

    protected void drawRadioButtons(ToggleGroup toggleGroup, List<Pane> panes) {
        for (int i = 0; i < panes.size(); i++) {
            Pane pane = panes.get(i);
            JFXRadioButton radioButton = GraphicsUtils.getRadioButton("",
                    "radio-button-notify-pane", Color.WHITE, Color.DEEPSKYBLUE);
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setUserData(pane);

            DoubleBinding x = pane.translateXProperty().add(pane.widthProperty().divide(2));
            DoubleBinding y = pane.translateYProperty().add(pane.heightProperty()).add(PADDING * 4);

            pane.setOnMousePressed(event -> {
                radioButton.setSelected(true);
                event.consume();
            });

            radioButton.translateXProperty().bind(x);
            radioButton.translateYProperty().bind(y);

            notifyPane.getChildren().add(radioButton);
        }
    }

    protected void drawSimpleCloseHelperBox(Pane pane, String text) {
        HBox helperBox = showHelperText(pane, text);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton cancelButton = GraphicsUtils.getButton("Chiudi", "negative-button");
        cancelButton.setOnAction(event -> {
            clearNotifyPane(false);
            deactivateNotifyPane();
        });

        helperBox.getChildren().addAll(spacer, cancelButton);
    }

    protected DoubleBinding getWidth() {
        return notifyPane.widthProperty().divide(1);
    }

    protected DoubleBinding getHeight() {
        return notifyPane.heightProperty().divide(1);
    }

    protected DoubleBinding getCenterX() {
        return notifyPane.widthProperty().divide(2);
    }

    protected DoubleBinding getCenterY() {
        return notifyPane.heightProperty().divide(2);
    }

    protected DoubleBinding getPivotX(DoubleBinding x, DoubleBinding width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    protected DoubleBinding getPivotX(DoubleBinding x, ReadOnlyDoubleProperty width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    protected DoubleBinding getPivotY(DoubleBinding y, DoubleBinding height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

    protected DoubleBinding getPivotY(DoubleBinding y, ReadOnlyDoubleProperty height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

    protected double getSceneHeight(){
        return corePane.getScene().getWindow().getHeight();
    }

    protected double getSceneWidth(){
        return corePane.getScene().getWindow().getWidth();
    }

}
