package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class AbstractView extends UnicastRemoteObject{

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

    public HBox showHelperText(Pane pane, String text) {
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

    public HBox showTopBarText(Pane pane, String text){
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

    public void showCrashErrorMessage(String text) {
        // TODO
    }

    public void clearNotifyPane() {
        notifyPane.getChildren().clear();
        notifyPane.getChildren().add(getBackgroundPane());
    }

    public void activateNotifyPane() {
        notifyPane.toFront();
        notifyPane.setVisible(true);
    }

    public void deactivateNotifyPane() {
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

    protected Pane getActivePane(){
        if(notifyPane.isVisible())
            return notifyPane;
        else
            return corePane;
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
