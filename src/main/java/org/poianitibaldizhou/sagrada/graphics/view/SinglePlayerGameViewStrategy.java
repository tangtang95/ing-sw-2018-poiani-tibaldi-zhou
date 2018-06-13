package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.controller.GameController;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

public class SinglePlayerGameViewStrategy implements IGameViewStrategy{

    private final GameController gameController;
    private final Pane corePane;
    private final Pane notifyPane;

    private static final double SCHEMA_CARD_SCALE = 0.4;
    private static final double PRIVATE_OBJECTIVE_CARD_SCALE = 0.4;
    private static final double ROUND_TRACK_SCALE = 1;

    public SinglePlayerGameViewStrategy(GameController gameController, Pane corePane, Pane notifyPane) {
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
        return corePane.heightProperty().divide(4);
    }

    @Override
    public DoubleBinding getSchemaCardCenterX(DoubleBinding offsetX) {
        return corePane.widthProperty().divide(2).add(offsetX.divide(2));
    }

    @Override
    public DoubleBinding getSchemaCardCenterY(DoubleBinding offsetY) {
        return corePane.widthProperty().divide(2).add(offsetY.divide(2));
    }

    @Override
    public SchemaCardView drawSchemaCardView(SchemaCardWrapper schemaCardWrapper, double angle) {
        SchemaCardView schemaCardView = new SchemaCardView(schemaCardWrapper, SCHEMA_CARD_SCALE);
        schemaCardView.translateXProperty().bind(corePane.widthProperty().divide(2)
                .subtract(schemaCardView.widthProperty().divide(2)));
        schemaCardView.translateYProperty().bind(corePane.heightProperty().divide(1.6)
                .subtract(schemaCardView.heightProperty().divide(2)));
        corePane.getChildren().add(schemaCardView);
        return schemaCardView;
    }
}
