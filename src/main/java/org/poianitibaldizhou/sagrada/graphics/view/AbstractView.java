package org.poianitibaldizhou.sagrada.graphics.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.Animation;
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

    protected void clearNotifyPane() {
        notifyPane.getChildren().clear();
        notifyPane.getChildren().add(getBackgroundPane());
    }

    protected void activateNotifyPane() {
        notifyPane.toFront();
        notifyPane.setVisible(true);
    }

    protected void deactivateNotifyPane() {
        notifyPane.toBack();
        notifyPane.setVisible(false);
    }

    protected Pane getBackgroundPane() {
        Pane backgroundPane = new Pane();
        backgroundPane.setOpacity(0.6);
        backgroundPane.setStyle("-fx-background-color: black");
        backgroundPane.prefWidthProperty().bind(notifyPane.widthProperty());
        backgroundPane.prefHeightProperty().bind(notifyPane.heightProperty());
        return backgroundPane;
    }

    protected Pane getActivePane() {
        if (notifyPane.isVisible())
            return notifyPane;
        else
            return corePane;
    }

    protected void drawCenteredPanes(Pane targetPane, List<Pane> panes, String classCSS) {
        DoubleBinding y = getCenterY();

        for (int i = 0; i < panes.size(); i++) {
            DoubleBinding padding = panes.get(i).widthProperty().divide(2);
            DoubleBinding totalWidth = panes.get(i).widthProperty().multiply(panes.size())
                    .add(padding.multiply(panes.size() - 1));
            DoubleBinding x = getCenterX().subtract(totalWidth.divide(2))
                    .add(panes.get(i).widthProperty().multiply(i)).add(padding.multiply(i));

            panes.get(i).translateXProperty().bind(getPivotX(x, panes.get(i).widthProperty(), 1));
            panes.get(i).translateYProperty().bind(getPivotY(y, panes.get(i).heightProperty(), 0.5));
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
        if(!classCSS.isEmpty())
            pane.getStyleClass().add(classCSS);
        targetPane.getChildren().add(pane);
    }

    protected void drawRadioButtons(ToggleGroup toggleGroup, List<Pane> panes) {
        for (int i = 0; i < panes.size(); i++) {
            Pane schemaCardPane = panes.get(i);
            JFXRadioButton radioButton = GraphicsUtils.getRadioButton("",
                    "radio-button-notify-pane", Color.WHITE, Color.DEEPSKYBLUE);
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setUserData(schemaCardPane);

            DoubleBinding x = schemaCardPane.translateXProperty().add(schemaCardPane.widthProperty().divide(2));
            DoubleBinding y = schemaCardPane.translateYProperty().add(schemaCardPane.heightProperty()).add(PADDING * 4);

            schemaCardPane.setOnMousePressed(event -> radioButton.setSelected(true));

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
            clearNotifyPane();
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

}
