package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.FrontBackSchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

/**
 * OVERVIEW: Represents the view for a front back schema car
 */
public class FrontBackSchemaCardView extends Pane {

    private SchemaCardView frontSchemaCard;
    private SchemaCardView backSchemaCard;

    private SchemaCardView currentSchemaCard;
    private RotateTransition transition;

    private double initialX;

    private static final double ROTATE_AMPLIFY = 0.8;

    /**
     * Constructor.
     * Create a new FrontBackSchemaCardView (pane) that contains the two schemaCard (front and back)
     *
     * @param frontBackSchemaCard the model of the frontBackSchemaCard
     * @param scale the scale value
     */
    public FrontBackSchemaCardView(FrontBackSchemaCardWrapper frontBackSchemaCard, double scale) {
        this.frontSchemaCard = new SchemaCardView(frontBackSchemaCard.getFrontSchemaCard(), scale);
        this.backSchemaCard = new SchemaCardView(frontBackSchemaCard.getBackSchemaCard(), scale);

        this.currentSchemaCard = this.frontSchemaCard;

        this.setRotationAxis(Rotate.Y_AXIS);

        this.setOnMouseEntered(this::onMouseEntered);
        this.setOnMouseExited(this::onMouseExited);
        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseDragged(this::onMouseDragged);
        this.rotateProperty().addListener(this::onRotateListener);
        this.setOnMouseReleased(this::onMouseReleased);

        this.getChildren().add(this.currentSchemaCard);
    }

    /**
     * Drop a shadow when the mouse gets over the front back schema cards
     *
     * @param event mouse over the schema card
     */
    private void onMouseEntered(MouseEvent event){
        DropShadow effect = new DropShadow(6, Color.AQUA);
        frontSchemaCard.getImageView().setEffect(effect);
        backSchemaCard.getImageView().setEffect(effect);
        event.consume();
    }

    /**
     * Disable the effects of the on mouse entered when the cursor exits the front back schema card
     *
     * @param event mouse cursor leaves the schema card
     */
    private void onMouseExited(MouseEvent event){
        frontSchemaCard.getImageView().setEffect(null);
        backSchemaCard.getImageView().setEffect(null);
        event.consume();
    }

    /**
     * Init point of the front back schema card rotation.
     *
     * @param event pressed mouse event
     */
    private void onMousePressed(MouseEvent event){
        if(transition != null)
            transition.stop();
        initialX = event.getSceneX();
        event.consume();
    }

    /**
     * Rotate the front back schema card
     *
     * @param event mouse drag event
     */
    private void onMouseDragged(MouseEvent event){
        double distance = event.getSceneX() - initialX;
        this.setRotate(this.getRotate() + distance * ROTATE_AMPLIFY);
        initialX = event.getSceneX();
        event.consume();
    }

    /**
     * Listen to the rotation of the schema card. Checks the angles in order to change the card image.
     *
     * @param observable not used
     * @param oldValue angle old value
     * @param newValue  angle new value
     */
    private void onRotateListener(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (oldValue.doubleValue() <= 90 && newValue.doubleValue() > 90) {
            this.setRotate(270);
            this.getChildren().clear();
            this.currentSchemaCard = (currentSchemaCard == frontSchemaCard) ? backSchemaCard : frontSchemaCard;
            this.getChildren().add(this.currentSchemaCard);
        } else if (oldValue.doubleValue() >= -90 && newValue.doubleValue() < -90) {
            this.setRotate(450);
            this.getChildren().clear();
            this.currentSchemaCard = (currentSchemaCard == frontSchemaCard) ? backSchemaCard : frontSchemaCard;
            this.getChildren().add(this.currentSchemaCard);
        } else if (oldValue.doubleValue() >= 270 && newValue.doubleValue() < 270) {
            this.setRotate(90);
            this.getChildren().clear();
            this.currentSchemaCard = (currentSchemaCard == frontSchemaCard) ? backSchemaCard : frontSchemaCard;
            this.getChildren().add(this.currentSchemaCard);
        } else if (oldValue.doubleValue() <= 450 && newValue.doubleValue() > 450) {
            this.setRotate(-90);
            this.getChildren().clear();
            this.currentSchemaCard = (currentSchemaCard == frontSchemaCard) ? backSchemaCard : frontSchemaCard;
            this.getChildren().add(this.currentSchemaCard);
        }
    }

    /**
     * Stop the rotation and start and automatic rotation that will make the card appear in a frontal way
     *
     * @param event released mouse event
     */
    private void onMouseReleased(MouseEvent event) {
        Node source = (Node) event.getSource();
        transition = new RotateTransition(Duration.millis(500), source);
        transition.setAxis(Rotate.Y_AXIS);
        transition.setFromAngle(source.getRotate());
        double toAngle = (source.getRotate() >= -90 && source.getRotate() <= 90) ? 0 : 360;
        transition.setToAngle(toAngle);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setCycleCount(1);
        transition.play();
    }

    /**
     * @return the schemaCard chosen
     */
    public SchemaCardWrapper getCurrentSchemaCardWrapper() {
        return currentSchemaCard.getSchemaCardWrapper();
    }
}
