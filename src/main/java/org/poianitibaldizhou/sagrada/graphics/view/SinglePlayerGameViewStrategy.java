package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

/**
 * Single player gui strategy for draw the view.
 */
public class SinglePlayerGameViewStrategy implements IGameViewStrategy{

    private final Pane corePane;

    private static final double SCHEMA_CARD_SCALE = 1;
    private static final double PRIVATE_OBJECTIVE_CARD_SCALE = 0.6;
    private static final double PUBLIC_OBJECTIVE_CARD_SCALE = 0.85;
    private static final double TOOL_CARD_SCALE = 0.85;
    private static final double ROUND_TRACK_SCALE = 1;

    /**
     * Constructor.
     * Create a single player game view strategy, that defines the position and scale of most object inside the
     * game
     *
     * @param corePane the core view of the game
     */
    public SinglePlayerGameViewStrategy(Pane corePane) {
        this.corePane = corePane;
    }

    /**
     * @return the private objective card scale.
     */
    @Override
    public double getPrivateObjectiveCardScale() {
        return PRIVATE_OBJECTIVE_CARD_SCALE;
    }

    /**
     * @return the round track scale.
     */
    @Override
    public double getRoundTrackScale() {
        return ROUND_TRACK_SCALE;
    }

    /**
     * @return the center X of round track.
     */
    @Override
    public DoubleBinding getRoundTrackCenterX() {
        return corePane.widthProperty().divide(2);
    }

    /**
     * @return the center Y of round track.
     */
    @Override
    public DoubleBinding getRoundTrackCenterY() {
        return corePane.heightProperty().divide(5);
    }

    /**
     * draw the schema card in the correct position
     *
     * @param targetPane        the pane to draw on
     * @param schemaCardWrapper the model of the schemaCard to draw
     * @param angle             the angle to rotate
     * @return the schema card drown.
     */
    @Override
    public SchemaCardView drawSchemaCardView(Pane targetPane, SchemaCardWrapper schemaCardWrapper, double angle) {
        SchemaCardView schemaCardView = new SchemaCardView(schemaCardWrapper, SCHEMA_CARD_SCALE);
        schemaCardView.translateXProperty().bind(targetPane.widthProperty().divide(2)
                .subtract(schemaCardView.widthProperty().divide(2)));
        schemaCardView.translateYProperty().bind(targetPane.heightProperty().divide(1.6)
                .subtract(schemaCardView.heightProperty().divide(2)));
        targetPane.getChildren().add(schemaCardView);
        return schemaCardView;
    }

    /**
     * @return the public objective card scale.
     */
    @Override
    public double getPublicObjectiveCardScale() {
        return PUBLIC_OBJECTIVE_CARD_SCALE;
    }

    /**
     * @return the tool card scale.
     */
    @Override
    public double getToolCardScale() {
        return TOOL_CARD_SCALE;
    }

    /**
     * @return false because there aren't timeout in single player.
     */
    @Override
    public boolean hasTimeout() {
        return false;
    }

}
