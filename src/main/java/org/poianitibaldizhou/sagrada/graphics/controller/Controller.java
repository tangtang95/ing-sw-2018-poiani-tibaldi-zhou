package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class Controller {

    protected Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void playSceneTransition(Node node, EventHandler<ActionEvent> eventHandler){
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(400));
        transition.setNode(node);
        transition.setToValue(0);
        transition.setByValue(1);
        transition.play();
        transition.setOnFinished(eventHandler);
    }

    protected void switchScene(Parent rootPane) {
        Scene scene = new Scene(rootPane, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }

}

