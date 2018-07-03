package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

/**
 * Multi player gui strategy for draw the view.
 */
public class MultiPlayerGameViewStrategy implements IGameViewStrategy {

    private Pane corePane;
    private Pane notifyPane;

    private static final double SCHEMA_CARD_SCALE = 0.625;
    private static final double PRIVATE_OBJECTIVE_CARD_SCALE = 0.34;
    private static final double ROUND_TRACK_SCALE = 0.5;
    private static final double TOOL_CARD_SCALE = 0.45;
    private static final double PUBLIC_OBJECTIVE_CARD_SCALE = 0.45;

    private static final double PADDING = 10;

    /**
     * Constructor.
     * Create a multi player game view strategy, that defines the position and scale of most object inside the
     * game
     *
     * @param corePane the core view of the game
     * @param notifyPane the view of the game to show the image on a greater size
     */
    public MultiPlayerGameViewStrategy(Pane corePane, Pane notifyPane) {
        this.corePane = corePane;
        this.notifyPane = notifyPane;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPrivateObjectiveCardScale() {
        return PRIVATE_OBJECTIVE_CARD_SCALE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRoundTrackScale() {
        return ROUND_TRACK_SCALE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoubleBinding getRoundTrackCenterX() {
        return corePane.widthProperty().divide(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoubleBinding getRoundTrackCenterY() {
        return corePane.heightProperty().divide(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchemaCardView drawSchemaCardView(Pane targetPane, SchemaCardWrapper schemaCardWrapper, double angle) {
        // CALCULATE SCHEMA CARD POSITION
        DoubleBinding distance;
        if (Math.abs(Math.abs(angle) - 2 * Math.PI) < 0.0001f || Math.abs(Math.abs(angle) - Math.PI) < 0.0001f)
            distance = getHeight().divide(2).subtract(getHeight().divide(3));
        else
            distance = getHeight().divide(2).subtract(getHeight().divide(1.8)).subtract(PADDING);
        DoubleBinding offsetX = distance.multiply(Math.cos(angle));
        DoubleBinding offsetY = distance.multiply(Math.sin(angle));

        // DRAW SCHEMA CARD
        SchemaCardView schemaCardView = new SchemaCardView(schemaCardWrapper, SCHEMA_CARD_SCALE);
        schemaCardView.setRotate(angle * 180.0 / Math.PI - 90);
        DoubleBinding distanceSchemaCard = getCenterY().subtract(schemaCardView.heightProperty().divide(1.8));
        schemaCardView.translateXProperty().bind(getPivotX(getCenterX().add(offsetX),
                schemaCardView.widthProperty(), 0.5).add(distanceSchemaCard.multiply(Math.cos(angle))));
        schemaCardView.translateYProperty().bind(getPivotY(getCenterY().add(offsetY),
                schemaCardView.heightProperty(), 0.5).add(distanceSchemaCard.multiply(Math.sin(angle))));
        targetPane.getChildren().addAll(schemaCardView);
        return schemaCardView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPublicObjectiveCardScale() {
       return PUBLIC_OBJECTIVE_CARD_SCALE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getToolCardScale() {
        return TOOL_CARD_SCALE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasTimeout() {
        return true;
    }

    /**
     * @return the width of pane.
     */
    private DoubleBinding getWidth() {
        return notifyPane.widthProperty().divide(1);
    }

    /**
     * @return the height of pane.
     */
    private DoubleBinding getHeight() {
        return notifyPane.heightProperty().divide(1);
    }

    /**
     * @return the coordinate X of center pane.
     */
    private DoubleBinding getCenterX() {
        return notifyPane.widthProperty().divide(2);
    }

    /**
     * @return the coordinate Y of center pane.
     */
    private DoubleBinding getCenterY() {
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
    private DoubleBinding getPivotX(DoubleBinding x, DoubleBinding width, double pivotX) {
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
    private DoubleBinding getPivotX(DoubleBinding x, ReadOnlyDoubleProperty width, double pivotX) {
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
    private DoubleBinding getPivotY(DoubleBinding y, DoubleBinding height, double pivotY) {
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
    private DoubleBinding getPivotY(DoubleBinding y, ReadOnlyDoubleProperty height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

}
