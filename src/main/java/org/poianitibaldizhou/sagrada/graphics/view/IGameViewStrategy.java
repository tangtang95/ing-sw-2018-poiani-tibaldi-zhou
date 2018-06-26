package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

public interface IGameViewStrategy {

    /**
     * @return the private objective card scale used on the core pane
     */
    double getPrivateObjectiveCardScale();

    /**
     * @return the round track scale used on the core pane
     */
    double getRoundTrackScale();

    /**
     * @return the center x position of the roundTrack
     */
    DoubleBinding getRoundTrackCenterX();

    /**
     * @return the center y position of the roundTrack
     */
    DoubleBinding getRoundTrackCenterY();

    /**
     * Draw the schemaCardView on the targetPane, based on the schemaCard given, rotated by a certain angle given
     *
     * @param targetPane the pane to draw on
     * @param schemaCardWrapper the model of the schemaCard to draw
     * @param angle the angle to rotate
     * @return the schemaCardView drawn
     */
    SchemaCardView drawSchemaCardView(Pane targetPane, SchemaCardWrapper schemaCardWrapper, double angle);

    /**
     * @return the public objective card scale used on the core pane
     */
    double getPublicObjectiveCardScale();

    /**
     * @return the tool card scale used on the core pane
     */
    double getToolCardScale();

    /**
     * @return true if it has to show the timeout, otherwise false
     */
    boolean hasTimeout();
}
