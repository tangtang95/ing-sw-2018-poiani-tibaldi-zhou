package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

public class FrontBackSchemaCardView extends Pane {

    private SchemaCardView frontSchemaCard;
    private SchemaCardView backSchemaCard;

    public FrontBackSchemaCardView(SchemaCardWrapper frontSchemaCard, SchemaCardWrapper backSchemaCard, double scale) {
        this.frontSchemaCard = new SchemaCardView(frontSchemaCard, scale);
        this.backSchemaCard = new SchemaCardView(backSchemaCard, scale);

        this.getChildren().add(this.frontSchemaCard);
    }

    public void flipCard(Duration duration){
        RotateTransition firstHalfRotation = new RotateTransition(duration.divide(2), this);
        firstHalfRotation.setAxis(Rotate.Y_AXIS);
        firstHalfRotation.setFromAngle(0);
        firstHalfRotation.setToAngle(90);
        firstHalfRotation.setInterpolator(Interpolator.LINEAR);
        firstHalfRotation.setCycleCount(1);
        firstHalfRotation.play();
        firstHalfRotation.setOnFinished(event -> {
            this.getChildren().clear();
            this.getChildren().add(this.backSchemaCard);
            RotateTransition secondHalfRotation = new RotateTransition(duration.divide(2), this);
            secondHalfRotation.setAxis(Rotate.Y_AXIS);
            secondHalfRotation.setFromAngle(270);
            secondHalfRotation.setToAngle(360);
            secondHalfRotation.setInterpolator(Interpolator.LINEAR);
            secondHalfRotation.setCycleCount(1);
            secondHalfRotation.play();
        });
    }



}
