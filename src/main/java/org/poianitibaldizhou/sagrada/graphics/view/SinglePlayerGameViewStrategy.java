package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

public class SinglePlayerGameViewStrategy implements IGameViewStrategy{

    private final GameGraphicsController gameController;
    private final Pane corePane;
    private final Pane notifyPane;

    private static final double SCHEMA_CARD_SCALE = 1;
    private static final double PRIVATE_OBJECTIVE_CARD_SCALE = 0.6;
    private static final double PUBLIC_OBJECTIVE_CARD_SCALE = 0.85;
    private static final double TOOL_CARD_SCALE = 0.85;
    private static final double ROUND_TRACK_SCALE = 1;

    public SinglePlayerGameViewStrategy(GameGraphicsController gameController, Pane corePane, Pane notifyPane) {
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
        return corePane.heightProperty().divide(5);
    }

    @Override
    public SchemaCardView drawSchemaCardView(Pane corePane, SchemaCardWrapper schemaCardWrapper, double angle) {
        SchemaCardView schemaCardView = new SchemaCardView(schemaCardWrapper, SCHEMA_CARD_SCALE);
        schemaCardView.translateXProperty().bind(corePane.widthProperty().divide(2)
                .subtract(schemaCardView.widthProperty().divide(2)));
        schemaCardView.translateYProperty().bind(corePane.heightProperty().divide(1.6)
                .subtract(schemaCardView.heightProperty().divide(2)));
        corePane.getChildren().add(schemaCardView);
        return schemaCardView;
    }

    @Override
    public double getPublicObjectiveCardScale() {
        return PUBLIC_OBJECTIVE_CARD_SCALE;
    }

    @Override
    public double getToolCardScale() {
        return TOOL_CARD_SCALE;
    }

    @Override
    public boolean hasTimeout() {
        return false;
    }

}
