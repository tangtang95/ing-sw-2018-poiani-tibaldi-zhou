package org.poianitibaldizhou.sagrada.graphics.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.utils.AlertBox;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class with generic implementation of method that inherit all
 * GUI view.
 */
public abstract class AbstractView extends UnicastRemoteObject {

    protected final transient GameGraphicsController controller;
    protected final transient Pane corePane;
    protected final transient Pane notifyPane;

    public static final double HELPER_BAR_PERCENT_HEIGHT = 0.05;
    
    protected static final double PADDING = 10;

    /**
     * Constructor.
     * Create an abstract view with the controller and two fundamental panes and initialize a thread that
     * takes care of the message showed on the panes.
     *
     * @param controller graphic controller.
     * @param corePane principal screen panel
     * @param notifyPane pane for notify to player.
     * @throws RemoteException network error
     */
    protected AbstractView(GameGraphicsController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super();
        this.controller = controller;
        this.corePane = corePane;
        this.notifyPane = notifyPane;
    }

    /**
     * @return the graphic controller.
     */
    //GETTER
    public GameGraphicsController getController() {
        return controller;
    }


    /**
     * Show message on view.
     *
     * @param pane principal pane
     * @param text message to show on screen.
     * @param messageType message type (INFO or ERROR)
     */
    protected void showMessage(Pane pane, String text, MessageType messageType) {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.prefWidthProperty().bind(pane.widthProperty());
        container.setFillWidth(true);
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 1.6em");
        container.getChildren().add(label);
        container.setVisible(false);
        if (messageType == MessageType.ERROR)
            label.setTextFill(Color.ORANGERED);
        else
            label.setTextFill(Color.DEEPSKYBLUE);

        ParallelTransition parallelTransition = new ParallelTransition(label);
        Duration duration = Duration.millis(2500);
        FadeTransition fadeTransition = new FadeTransition(duration, label);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        TranslateTransition translateTransition = new TranslateTransition(duration, container);
        translateTransition.fromYProperty().bind(getHeight().subtract(getHeight().divide(8)));
        translateTransition.toYProperty().bind(getHeight().subtract(PADDING*2));
        parallelTransition.getChildren().addAll(fadeTransition, translateTransition);
        parallelTransition.setOnFinished(event -> pane.getChildren().remove(container));

        container.setUserData(parallelTransition);
        pane.getChildren().add(container);
        controller.pushMessage(container);
    }

    /**
     * Show the helper text on screen.
     *
     * @param pane principal pane
     * @param text text message to show
     * @return the helperPane
     */
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

    /**
     * Show the top bar text on screen.
     *
     * @param pane principal pane
     * @param text text to show
     * @return a topBarBox
     */
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

    /**
     * Show crash error message on screen
     *
     * @param text to show
     */
    protected void showCrashErrorMessage(String text) {
        AlertBox.displayBox("Errore di connessione", text);
        try {
            controller.quitGame();
        } catch (IOException e) {
            controller.popGameScene();
        }
    }

    /**
     * Clear a notify panel.
     *
     * @param isFastCloseable for determinate if the pane is closable by clicking on screen
     */
    protected void clearNotifyPane(boolean isFastCloseable) {
        notifyPane.getChildren().clear();
        notifyPane.getChildren().add(getBackgroundPane(isFastCloseable));
    }

    /**
     * Active notify pane, put the pane visible
     */
    protected void activateNotifyPane() {
        notifyPane.toFront();
        notifyPane.setVisible(true);
    }

    /**
     * Deactivate the notify pane, put it not visible.
     */
    protected void deactivateNotifyPane() {
        notifyPane.toBack();
        notifyPane.setVisible(false);
    }

    /**
     * Get the background pane.
     *
     * @param isFastCloseable for determinate if the pane is closable by clicking on screen
     * @return the backGround pane
     */
    private Pane getBackgroundPane(boolean isFastCloseable) {
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

    /**
     * @return the current active pane.
     */
    protected Pane getActivePane() {
        if (notifyPane.isVisible())
            return notifyPane;
        else
            return corePane;
    }

    /**
     * Draw on the center of pane target.
     *
     * @param targetPane the pane target
     * @param panes list of pane
     * @param classCSS css style
     */
    protected void drawCenteredPanes(@NotNull Pane targetPane, @NotNull List<Pane> panes, String classCSS) {
        DoubleBinding y = getCenterY();
        GraphicsUtils.drawCenteredPanes(targetPane, panes, classCSS, getCenterX(),
                getPivotY(y, panes.get(0).heightProperty(), 0.5));
    }

    /**
     * Draw on the center of pane target.
     *
     * @param targetPane the pane target
     * @param pane to draw
     * @param classCSS css style
     */
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

    /**
     * Draw in a target pane a generic pane.
     *
     * @param targetPane target pane.
     * @param pane to draw
     * @param classCSS CSS style
     * @param x listener on x
     * @param y listener on y
     */
    protected void drawPane(Pane targetPane, Pane pane, String classCSS, DoubleBinding x, DoubleBinding y) {
        pane.translateXProperty().bind(x);
        pane.translateYProperty().bind(y);
        pane.setOnMousePressed(Event::consume);
        if(!classCSS.isEmpty())
            pane.getStyleClass().add(classCSS);
        targetPane.getChildren().add(pane);
    }

    /**
     * Draw a generic radio button
     *
     * @param toggleGroup a group of radio button
     * @param panes list of pane
     */
    protected void drawRadioButtons(ToggleGroup toggleGroup, List<Pane> panes) {
        for (Pane pane : panes) {
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

    /**
     * draw a simple helper box on screen.
     *
     * @param pane to draw
     * @param text text message
     */
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

    /**
     * @return the pane width
     */
    protected DoubleBinding getWidth() {
        return notifyPane.widthProperty().divide(1);
    }

    /**
     * @return the pane height
     */
    protected DoubleBinding getHeight() {
        return notifyPane.heightProperty().divide(1);
    }

    /**
     * @return the pane center on variable x
     */
    protected DoubleBinding getCenterX() {
        return notifyPane.widthProperty().divide(2);
    }

    /**
     * @return the pae center on variable y
     */
    protected DoubleBinding getCenterY() {
        return notifyPane.heightProperty().divide(2);
    }

    /**
     * return the correct position for fixing an object on the screen.
     *
     * @param x listener on x position.
     * @param width listener on width.
     * @param pivotX system reference.
     * @return the correct position.
     */
    protected DoubleBinding getPivotX(DoubleBinding x, DoubleBinding width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    /**
     * return the correct position for fixing an object on the screen.
     *
     * @param x listener on x position.
     * @param width listener on width.
     * @param pivotX system reference.
     * @return the correct position.
     */
    protected DoubleBinding getPivotX(DoubleBinding x, ReadOnlyDoubleProperty width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    /**
     * return the correct position for fixing an object on the screen.
     *
     * @param y listener on y position.
     * @param height listener on height.
     * @param pivotY system reference.
     * @return the correct position.
     */
    protected DoubleBinding getPivotY(DoubleBinding y, DoubleBinding height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

    /**
     * return the correct position for fixing an object on the screen.
     *
     * @param y listener on y position.
     * @param height listener on height.
     * @param pivotY system reference.
     * @return the correct position.
     */
    protected   DoubleBinding getPivotY(DoubleBinding y, ReadOnlyDoubleProperty height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

    /**
     * @return the scene height value
     */
    protected double getSceneHeight(){
        return corePane.getScene().getWindow().getHeight();
    }

    /**
     * @return the scene width value
     */
    protected double getSceneWidth(){
        return corePane.getScene().getWindow().getWidth();
    }

    /**
     * Update the view by getting the most recent model
     */
    public abstract void updateView();

    /**
     * Equal method
     *
     * @param o generic object to compare
     * @return true if the object o is equals to this.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractView)) return false;
        if (!super.equals(o)) return false;
        AbstractView that = (AbstractView) o;
        return Objects.equals(getController(), that.getController()) &&
                Objects.equals(corePane, that.corePane) &&
                Objects.equals(notifyPane, that.notifyPane);
    }

    /**
     * @return the hash code
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getController(), corePane, notifyPane);
    }
}
