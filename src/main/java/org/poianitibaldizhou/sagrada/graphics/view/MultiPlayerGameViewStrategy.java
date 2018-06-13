package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.graphics.controller.GameController;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.util.Map;

public class MultiPlayerGameViewStrategy implements IGameViewStrategy {

    private GameController gameController;
    private Pane corePane;
    private Pane notifyPane;

    private static final double SCHEMA_CARD_SCALE = 0.25;
    private static final double PRIVATE_OBJECTIVE_CARD_SCALE = 0.25;
    private static final double ROUND_TRACK_SCALE = 0.25;

    private static final double PADDING = 10;

    public MultiPlayerGameViewStrategy(GameController gameController, Pane corePane, Pane notifyPane) {
        this.gameController = gameController;
        this.corePane = corePane;
        this.notifyPane = notifyPane;
    }

    @Override
    public double getSchemaCardScale() {
        return SCHEMA_CARD_SCALE;
    }

    @Override
    public double getPrivateObjectiveCardScale() {
        return PRIVATE_OBJECTIVE_CARD_SCALE;
    }

    @Override
    public double getRoundTrackScale() {
        return ROUND_TRACK_SCALE;
    }

    @Override
    public DoubleBinding getRoundTrackCenterX() {
        return corePane.widthProperty().divide(2);
    }

    @Override
    public DoubleBinding getRoundTrackCenterY() {
        return corePane.heightProperty().divide(2);
    }

    @Override
    public DoubleBinding getSchemaCardCenterX(DoubleBinding offsetX) {
        return corePane.widthProperty().divide(2).add(offsetX);
    }

    @Override
    public DoubleBinding getSchemaCardCenterY(DoubleBinding offsetY) {
        return corePane.widthProperty().divide(2).add(offsetY);
    }

    @Override
    public SchemaCardView drawSchemaCardView(SchemaCardWrapper schemaCardWrapper, double angle) {
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
        schemaCardView.translateXProperty().bind(getPivotX(getCenterX().add(offsetX), schemaCardView.widthProperty(), 0.5)
                .add(distanceSchemaCard.multiply(Math.cos(angle))));
        schemaCardView.translateYProperty().bind(getPivotY(getCenterY().add(offsetY), schemaCardView.heightProperty(), 0.5)
                .add(distanceSchemaCard.multiply(Math.sin(angle))));
        corePane.getChildren().addAll(schemaCardView);
        return schemaCardView;
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

    public static DoubleBinding getPivotY(DoubleBinding y, ReadOnlyDoubleProperty height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }


}
