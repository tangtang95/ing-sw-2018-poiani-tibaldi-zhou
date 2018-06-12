package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.beans.binding.DoubleBinding;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

public interface IGameViewStrategy {

    double getSchemaCardScale();

    double getPrivateObjectiveCardScale();

    double getRoundTrackScale();

    DoubleBinding getRoundTrackCenterX();

    DoubleBinding getRoundTrackCenterY();

    DoubleBinding getSchemaCardCenterX(DoubleBinding offsetX);

    DoubleBinding getSchemaCardCenterY(DoubleBinding offsetY);

    SchemaCardView drawSchemaCardView(SchemaCardWrapper schemaCardWrapper, double angle);


}
