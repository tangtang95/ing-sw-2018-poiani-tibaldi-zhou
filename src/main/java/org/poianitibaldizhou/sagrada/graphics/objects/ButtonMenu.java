package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class ButtonMenu extends Button {

    public ButtonMenu(){
        super();
        this.setOnMouseEntered(event -> {
            Button button = (Button) event.getSource();
            button.setTextFill(Color.TOMATO);
            button.setStyle("-fx-background-color: transparent; -fx-scale-x: 120%; -fx-scale-y: 120%");
        });

        this.setOnMouseExited(event -> {
            Button button = (Button) event.getSource();
            button.setTextFill(Color.BLACK);
            button.setStyle("-fx-background-color: transparent; -fx-scale-x: 100%; -fx-scale-y: 100%");
        });
    }


}
