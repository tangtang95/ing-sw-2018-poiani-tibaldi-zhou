package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

public interface IGameViewStrategy {

    double getSchemaCardScale();

    double getPrivateObjectiveCardScale();

    double getRoundTrackScale();

    DoubleBinding getRoundTrackCenterX();

    DoubleBinding getRoundTrackCenterY();

    SchemaCardView drawSchemaCardView(Pane corePane, SchemaCardWrapper schemaCardWrapper, double angle);

    double getPublicObjectiveCardScale();

    double getToolCardScale();

}
